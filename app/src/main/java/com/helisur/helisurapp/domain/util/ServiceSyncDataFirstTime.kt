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
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneEmpleadosDataTableCloudResponse
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity
import com.helisur.helisurapp.data.database.entities.toDB
import com.helisur.helisurapp.data.repository.AeronavesRepository
import com.helisur.helisurapp.data.repository.FormatosRepository
import com.helisur.helisurapp.data.repository.UsuarioRepository
import com.helisur.helisurapp.domain.model.Aeronave
import com.helisur.helisurapp.domain.model.Empleado
import com.helisur.helisurapp.domain.model.Estacion
import com.helisur.helisurapp.domain.model.Formato
import com.helisur.helisurapp.domain.model.ModeloAeronave
import com.helisur.helisurapp.domain.model.Sistema
import com.helisur.helisurapp.domain.model.Tarea
import com.helisur.helisurapp.domain.model.toDomain
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
class ServiceSyncDataFirstTime : Service() {

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

    private var estacionListDB: ArrayList<Estacion>? = null
    private var estacionListCloud: ArrayList<ObtieneEstacionesDataTableCloudResponse>? = null

    private var formatoListDB: ArrayList<Formato>? = null
    private var formatoListCloud: ArrayList<ObtieneFormatosDataTableCloudResponse>? = null

    private var sistemaListDB: ArrayList<Sistema>? = null
    private var sistemaListCloud: ArrayList<ObtieneSistemasDataTableCloudResponse>? = null

    private var tareaListDB: ArrayList<Tarea>? = null
    private var tareaListCloud: ArrayList<ObtieneTareasDataTableCloudResponse>? = null

    private var empleadoListDB: ArrayList<Empleado>? = null
    private var empleadoListCloud: ArrayList<ObtieneEmpleadosDataTableCloudResponse>? = null


    inner class LocalBinder : Binder() {
        fun getService(): ServiceSyncDataFirstTime = this@ServiceSyncDataFirstTime
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
            syncEstacion()
            syncFormatos()
            syncSistemas()
            syncTareas()
            syncEmpleados()
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
                modelosAeronaveListDB = arrayListOf()
                modelosAeronaveListCloud = ArrayList(aeronavesRepository.getModeloAeronaveListCloud().data!!.table)
                if (modelosAeronaveListCloud != null) {
                    for (itemcloud in modelosAeronaveListCloud!!) {
                        modelosAeronaveListDB!!.add(itemcloud.toDomain())
                    }
                     aeronavesRepository.insertModeloAeronaveListDB(modelosAeronaveListDB!!)
                }

            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    suspend fun syncAeronave() {
        try {
            withContext(Dispatchers.Main) {
                aeronaveListDB = arrayListOf()
                aeronaveListCloud = ArrayList(aeronavesRepository.getAeromaveListCloud("").data!!.table)
                if (aeronaveListCloud != null) {
                    for (itemcloud in aeronaveListCloud!!) {
                        aeronaveListDB!!.add(itemcloud.toDomain())
                    }
                    aeronavesRepository.insertAeronaveListDB(aeronaveListDB!!)
                }

            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }


    suspend fun syncEstacion() {
        try {
            withContext(Dispatchers.Main) {
                estacionListDB = arrayListOf()
                estacionListCloud = ArrayList(aeronavesRepository.getEstacionesListCloud().data!!.table)
                if (estacionListCloud != null) {
                    for (itemcloud in estacionListCloud!!) {
                        estacionListDB!!.add(itemcloud.toDomain())
                    }
                    aeronavesRepository.insertEstacionListDB(estacionListDB!!)
                }

            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }


    suspend fun syncFormatos() {
        try {
            withContext(Dispatchers.Main) {
                formatoListDB = arrayListOf()
                formatoListCloud = ArrayList(formatosRepository.obtieneFormatos().data!!.table)
                if (formatoListCloud != null) {
                    for (itemcloud in formatoListCloud!!) {
                        formatoListDB!!.add(itemcloud.toDomain())
                    }
                    formatosRepository.insertFormatoListDB(formatoListDB!!)
                }

            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }


    suspend fun syncSistemas() {
        try {
            withContext(Dispatchers.Main) {
                sistemaListDB = arrayListOf()
                sistemaListCloud = ArrayList(formatosRepository.getobtieneSistemas(""))
                if (sistemaListCloud != null) {
                    for (itemcloud in sistemaListCloud!!) {
                        sistemaListDB!!.add(itemcloud.toDomain())
                    }
                    formatosRepository.insertSistemaListDB(sistemaListDB!!)
                }

            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }


    suspend fun syncTareas() {
        try {
            withContext(Dispatchers.Main) {
                tareaListDB = arrayListOf()
                tareaListCloud = ArrayList(formatosRepository.getobtieneTareas(""))
                if (tareaListCloud != null) {
                    for (itemcloud in tareaListCloud!!) {
                        tareaListDB!!.add(itemcloud.toDomain())
                    }
                    formatosRepository.insertTareaListDB(tareaListDB!!)
                }

            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    suspend fun syncEmpleados() {
        try {
            withContext(Dispatchers.Main) {
                empleadoListDB = arrayListOf()
                empleadoListCloud = ArrayList(usuarioRepository.obtieneEmpleados("").data!!.table)
                if (tareaListCloud != null) {
                    for (itemcloud in empleadoListCloud!!) {
                        empleadoListDB!!.add(itemcloud.toDomain())
                    }
                    usuarioRepository.insertEmpleadoListDB(empleadoListDB!!)
                }

            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }


    //PRIMERO ENVIAR DATA
    fun sendDataToCloud() {
        //ORDEN
        //GRABA FORMATO
        //ACTUALIZA FORMATO


    }


    //LUEGO RECIBIR DATA
    fun getAllDataFromCloud() {
        // binding.progressBar.progress = 50

        //ORDEN

        //MODELOS AERONAVE
        //AERONAVES
        //ESTACIONES

        //FORMATOS
        //SISTEMAS
        //TAREAS
        //REPORTAJES

        //FORMATOS REALIZADOS
        //REPORTAJES FORMATO REALIZADO


    }


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