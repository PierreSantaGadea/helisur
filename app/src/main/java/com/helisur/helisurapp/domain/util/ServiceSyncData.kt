package com.helisur.helisurapp.domain.util

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.provider.Settings.Secure
import android.util.Log
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneEstacionesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaFormatoCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaTareaCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosRealizadosDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneReportajesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneEmpleadosDataTableCloudResponse
import com.helisur.helisurapp.data.database.entities.DetalleFormatoRegistroEntity
import com.helisur.helisurapp.data.database.entities.FormatoRegistroEntity
import com.helisur.helisurapp.data.repository.AeronavesRepository
import com.helisur.helisurapp.data.repository.FormatosRepository
import com.helisur.helisurapp.data.repository.UsuarioRepository
import com.helisur.helisurapp.domain.model.Aeronave
import com.helisur.helisurapp.domain.model.Empleado
import com.helisur.helisurapp.domain.model.Estacion
import com.helisur.helisurapp.domain.model.Formato
import com.helisur.helisurapp.domain.model.ModeloAeronave
import com.helisur.helisurapp.domain.model.Reportaje
import com.helisur.helisurapp.domain.model.Sistema
import com.helisur.helisurapp.domain.model.Tarea
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject


@AndroidEntryPoint
class ServiceSyncData : Service() {

    @Inject
    lateinit var aeronavesRepository: AeronavesRepository

    @Inject
    lateinit var formatosRepository: FormatosRepository

    @Inject
    lateinit var usuarioRepository: UsuarioRepository

    companion object {
        private const val TAG = "ServiceSyncData"
    }

    private val binder = LocalBinder()
    private val coroutineScope = CoroutineScope(Job())
    private var syncJob: Job? = null
    private var sessionUserManager: SessionUserManager? = null

    private var modelosAeronaveListDB: ArrayList<ModeloAeronave>? = null
    private var modelosAeronaveListCloud: ArrayList<ObtieneAeronavesDataTableCloudResponse>? = null
    private var aeronaveListDB: ArrayList<Aeronave>? = null
    private var aeronaveListCloud: ArrayList<ObtieneModelosAeronaveDataTableCloudResponse>? = null
    private var estacionesListDB: ArrayList<Estacion>? = null
    private var estacionesListCloud: ArrayList<ObtieneEstacionesDataTableCloudResponse>? = null
    private var formatoListDB: ArrayList<Formato>? = null
    private var formatoListCloud: ArrayList<ObtieneFormatosDataTableCloudResponse>? = null
    private var sistemaListDB: ArrayList<Sistema>? = null
    private var sistemaListCloud: ArrayList<ObtieneSistemasDataTableCloudResponse>? = null
    private var tareaListDB: ArrayList<Tarea>? = null
    private var tareaListCloud: ArrayList<ObtieneTareasDataTableCloudResponse>? = null
    private var reportajeListDB: ArrayList<Reportaje>? = null
    private var reportajeListCloud: ArrayList<ObtieneReportajesDataTableCloudResponse>? = null
    private var empleadoListDB: ArrayList<Empleado>? = null
    private var empleadoListCloud: ArrayList<ObtieneEmpleadosDataTableCloudResponse>? = null
    private var formatosRealizadosListDb: ArrayList<FormatoRegistroEntity>? = null
    private var detalleFormatosRealizadosListDb: ArrayList<DetalleFormatoRegistroEntity>? = null
    private var formatosRealizadosListCloud: ArrayList<ObtieneFormatosRealizadosDataTableCloudResponse>? = null


    inner class LocalBinder : Binder() {
        fun getService(): ServiceSyncData = this@ServiceSyncData
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d(TAG, "onBind")
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        startSyncronization()
        return super.onStartCommand(intent, flags, startId)
    }

    enum class Actions {
        START, STOP
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        sessionUserManager = SessionUserManager(context = baseContext)
        val androidID = Secure.getString(baseContext.getContentResolver(), Secure.ANDROID_ID)
        //  Toast.makeText(this, "Foreground Service created", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        syncJob?.cancel()
        coroutineScope.coroutineContext.cancelChildren()
    }

    fun stopForegroundService() {
        stopSelf()
    }


    private fun startSyncronization() {

        syncJob?.cancel()
        syncJob = coroutineScope.launch {
            syncModeloAeronave()
            syncAeronave()
            syncEstaciones()
            syncFormatos()
            syncSistemas()
            syncTareas()
            syncReportajes()
         //   syncEmpleados()
            //  sendFormatosRegistrados()
        }

        /*
                coroutineScope.launch {
                    syncModeloAeronave()
                }
        */

    }


    suspend fun syncModeloAeronave() {
        try {
            withContext(Dispatchers.Main) {
                modelosAeronaveListDB = ArrayList(aeronavesRepository.getModelosAeronavesListDB())
                modelosAeronaveListCloud =
                    ArrayList(aeronavesRepository.getModeloAeronaveListCloud().data!!.table)
                if (modelosAeronaveListCloud != null) {
                    for (itemcloud in modelosAeronaveListCloud!!) {
                        for (itemDB in modelosAeronaveListDB!!) {
                            if (itemcloud.codigoModeloPuesto.equals(itemDB.id_cloud)) {
                                if (itemcloud.fechaModificacion!! > itemDB.fechaModificacion!!) {
                                    aeronavesRepository.updateModeloAeronave(
                                        itemcloud.codigoModeloPuesto,
                                        itemcloud.descripcion,
                                        itemcloud.fechaRegistro!!,
                                        itemcloud.fechaModificacion!!,
                                        true
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    suspend fun syncAeronave() {
        try {
            withContext(Dispatchers.Main) {
                aeronaveListDB = ArrayList(aeronavesRepository.getAeronavesListDB())
                aeronaveListCloud =
                    ArrayList(aeronavesRepository.getAeromaveListCloud("").data!!.table)
                if (aeronaveListCloud != null) {
                    for (itemcloud in aeronaveListCloud!!) {
                        for (itemDB in aeronaveListDB!!) {
                            if (itemcloud.codigoPuestoTecnico.equals(itemDB.id_cloud)) {
                                if (itemcloud.fechaModificacion!! > itemDB.fechaModificacion!!) {
                                    aeronavesRepository.updateAeronave(
                                        itemcloud.codigoPuestoTecnico,
                                        itemcloud.codigoModeloPuesto,
                                        itemcloud.codigoCliente,
                                        itemcloud.nombre,
                                        itemcloud.placa,
                                        itemcloud.comentario,
                                        itemcloud.html,
                                        itemcloud.fechaRegistro!!,
                                        itemcloud.fechaModificacion!!,
                                        true
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }


    suspend fun syncEstaciones() {
        try {
            withContext(Dispatchers.Main) {
                estacionesListDB = ArrayList(aeronavesRepository.getEstacionesListDB())
                estacionesListCloud =
                    ArrayList(aeronavesRepository.getEstacionesListCloud().data!!.table)
                if (estacionesListCloud != null) {
                    for (itemcloud in estacionesListCloud!!) {
                        for (itemDB in estacionesListDB!!) {
                            if (itemcloud.id.equals(itemDB.id_cloud)) {
                                if (itemcloud.fechaModificacion!! > itemDB.fechaModificacion!!) {
                                    aeronavesRepository.updateEstacion(
                                        itemcloud.id,
                                        itemcloud.nombre,
                                        itemcloud.siglas,
                                        itemcloud.fechaRegistro,
                                        itemcloud.fechaModificacion,
                                        true
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }


    suspend fun syncFormatos() {
        try {
            withContext(Dispatchers.Main) {
                formatoListDB = ArrayList(formatosRepository.getFormatosListDB())
                formatoListCloud = ArrayList(formatosRepository.obtieneFormatos().data!!.table)
                if (formatoListCloud != null) {
                    for (itemcloud in formatoListCloud!!) {
                        for (itemDB in formatoListDB!!) {
                            if (itemcloud.codigoFormato.equals(itemDB.id_cloud)) {
                                if (itemcloud.fechaModificacion!! > itemDB.fechaModificacion!!) {
                                    formatosRepository.updateFormato(
                                        itemcloud.codigoFormato,
                                        itemcloud.nombreFormato,
                                        itemcloud.codigoModeloAeronave,
                                        itemcloud.fechaRegistro,
                                        itemcloud.fechaModificacion,
                                        true
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    suspend fun syncSistemas() {
        try {
            withContext(Dispatchers.Main) {
                sistemaListDB = ArrayList(formatosRepository.getSistemasListDB())
                sistemaListCloud = ArrayList(formatosRepository.getobtieneSistemas(""))
                if (sistemaListCloud != null) {
                    for (itemcloud in sistemaListCloud!!) {
                        for (itemDB in sistemaListDB!!) {
                            if (itemcloud.codigoSistema.equals(itemDB.codigoSistema)) {
                                if (itemcloud.fechaModificacion!! > itemDB.fechaModificacion!!) {
                                    formatosRepository.updateSistema(
                                        itemcloud.codigoSistema,
                                        itemcloud.codigoFormato,
                                        itemcloud.nombrePosicion,
                                        itemcloud.fechaRegistro,
                                        itemcloud.fechaModificacion,
                                        true
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }


    suspend fun syncTareas() {
        try {
            withContext(Dispatchers.Main) {
                tareaListDB = ArrayList(formatosRepository.getTareasListDB())
                tareaListCloud = ArrayList(formatosRepository.getobtieneTareas(""))
                if (tareaListCloud != null) {
                    for (itemcloud in tareaListCloud!!) {
                        for (itemDB in tareaListDB!!) {
                            if (itemcloud.codigoTarea.equals(itemDB.codigoTarea)) {
                                if (itemcloud.fechaModificacion!! > itemDB.fechaModificacion!!) {
                                    formatosRepository.updateTarea(
                                        itemcloud.codigoTarea,
                                        itemcloud.codigoSistema,
                                        itemcloud.nombreTarea,
                                        itemcloud.fechaRegistro,
                                        itemcloud.fechaModificacion,
                                        true
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }


    suspend fun syncReportajes() {
        try {
            withContext(Dispatchers.Main) {
                reportajeListDB = ArrayList(formatosRepository.getReportajesListDB())
                reportajeListCloud = ArrayList(formatosRepository.obtieneReportajes())
                if (reportajeListCloud != null) {
                    for (itemcloud in reportajeListCloud!!) {
                        for (itemDB in reportajeListDB!!) {
                            if (itemcloud.codigoReportaje.equals(itemDB.id_cloud)) {
                                if (itemcloud.fechaModificacion!! > itemDB.fechaModificacion!!) {
                                    formatosRepository.updateReportaje(
                                        itemcloud.codigoTarea,
                                        itemcloud.codigoTarea,
                                        itemcloud.nombreReportaje,
                                        itemcloud.bloqueoFormato,
                                        itemcloud.defaultt,
                                        itemcloud.fechaRegistro,
                                        itemcloud.fechaModificacion,
                                        true
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }


    suspend fun syncEmpleados() {
        try {
            withContext(Dispatchers.Main) {
                empleadoListDB = ArrayList(usuarioRepository.getEmpleadosListDB())
                empleadoListCloud = ArrayList(usuarioRepository.obtieneEmpleados("").data!!.table)
                if (empleadoListCloud != null) {
                    for (itemcloud in empleadoListCloud!!) {
                        for (itemDB in empleadoListDB!!) {
                            if (itemcloud.codigoEmpleado.equals(itemDB.id_cloud)) {
                                if (itemcloud.fechaModificacion!! > itemDB.fechaModificacion!!) {
                                    usuarioRepository.updateEmpleado(
                                        itemcloud.codigoEmpleado,
                                        itemcloud.codigoArea,
                                        itemcloud.numeroDocumento,
                                        itemcloud.nombre,
                                        itemcloud.codigoUsuario,
                                        itemcloud.apellidoPaterno,
                                        itemcloud.apellidoMaterno,
                                        itemcloud.nombreCompleto,
                                        itemcloud.email,
                                        itemcloud.estado,
                                        itemcloud.cargo,
                                        itemcloud.fechaIngreso,
                                        itemcloud.licencia,
                                        itemcloud.fechaRegistro,
                                        itemcloud.fechaModificacion,
                                        true
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }


    suspend fun sendFormatosRegistrados() {
        formatosRealizadosListDb =
            ArrayList(formatosRepository.getFormatosRegistroListDataBaseSQlite())

        detalleFormatosRealizadosListDb =
            ArrayList(formatosRepository.getDetalleFormatosRegistroListDataBaseSqlite())

        for (item in formatosRealizadosListDb!!) {
            if (!item.sync!!) {
                var listaTareas: ArrayList<GuardaTareaCloudParameter>? = arrayListOf()

                for (itemTarea in detalleFormatosRealizadosListDb!!) {
                    if (itemTarea.idRegistroFormatoDB.equals(item.id_db)) {
                        listaTareas!!.add(
                            GuardaTareaCloudParameter(
                                itemTarea.codigoRegistroFormato,
                                itemTarea.codigoTarea,
                                itemTarea.codigoReportaje,
                                itemTarea.indicadorSN,
                                item.usuarioRegistro,
                                "",
                                itemTarea.nombreTarea,
                                itemTarea.nombreSistema
                            )
                        )
                    }
                }

                var nuevoFormatoCloud = GuardaFormatoCloudParameter(
                    item.codigoFormato,
                    item.codigoPuestoTecnico,
                    item.numeroRTV,
                    item.codigoEstacion,
                    item.existenDiscrepancias,
                    item.numeroRTVDiscrepancias,
                    item.accionesMantenimiento,
                    item.solicitaEncMotores,
                    item.idEmpleadoResponsable,
                    item.urlFirmaResponsable,
                    item.idEmpleadoPiloto,
                    item.urlFirmaPiloto,
                    item.idEmpleadoCoPiloto,
                    item.urlFirmaCoPiloto,
                    item.fechaHoraInicioRegistro,
                    item.fechaHoraFinRegistro,
                    item.usuarioRegistro,
                    listaTareas
                )
                formatosRepository.grabaFormato(nuevoFormatoCloud)
            }
        }
    }




    /*
        suspend fun syncFormatosRegistrados() {
            try {
                withContext(Dispatchers.Main) {

                    formatosRealizadosListDb = ArrayList(formatosRepository.getFormatosRegistroListDB())

                    formatosRealizadosListCloud = ArrayList(formatosRepository.obtieneFormatosRealizados("","").data!!.table)
                    //      aeronavesRepository.deleteTableModeloAeronaveDB()
                    if (formatosRealizadosListCloud != null) {
                        for (itemcloud in formatosRealizadosListCloud!!) {

                            for(itemDB in formatosRealizadosListDb!!)
                            {
                                if(itemcloud.codigoModeloPuesto.equals(itemDB.id_cloud))
                                {
                                    if(itemcloud.fechaModificacion!!>itemDB.fechaModificacion!!)
                                    {
                                        aeronavesRepository.updateModeloAeronave(itemcloud.codigoModeloPuesto,
                                            itemcloud.descripcion,itemcloud.fechaRegistro!!,itemcloud.fechaModificacion!!,true)
                                        //  modelosAeronaveListDB!!.add(itemcloud.toDomain())
                                    }
                                }
                            }
                            //  modelosAeronaveListDB!!.add(itemcloud.toDomain())
                        }
                    }
                    // aeronavesRepository.insertModeloAeronaveListDB(modelosAeronaveListDB!!)
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    */


    fun actualizaToken(ctx: Context) {

        var sessionUserManager = SessionUserManager(context = ctx)
        val idDevice = Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID)
        var usuario = sessionUserManager.getUser()!!
        var pass = sessionUserManager.getPass()!!

        var urlApi = "https://extranet.edusoft.pe/ws.concursos/api/appService/login"
        val payload =
            "{'user': '" + usuario + "','password': '" + pass + "','idDevice': '" + idDevice + "'}"

        val okHttpClient = OkHttpClient()
        val requestBody = payload.toRequestBody()

        val request = Request.Builder().post(requestBody).url(urlApi)
            .header("Content-Type", "application/json").build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ERROR LOGIN TOKEN", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                var responseData = response.body!!.string()
                try {
                    var json = JSONObject(responseData)
                    val responseObject = json.getJSONObject("data")
                    val token = responseObject.get("token")
                    println(json)
                    Log.i("LOGIN JSON RESPONSE: ", json.toString())
                    SessionUserManager(ctx).saveAuthToken(token.toString())
                    println("NEW TOKEN : " + SessionUserManager(ctx).getToken())
                    Log.i("NEW TOKEN : ", SessionUserManager(ctx).getToken()!!)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })


    }


}