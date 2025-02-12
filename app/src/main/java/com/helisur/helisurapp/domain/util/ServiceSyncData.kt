package com.helisur.helisurapp.domain.util

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.provider.Settings.Secure
import android.util.Log
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.data.repository.AeronavesRepository
import com.helisur.helisurapp.domain.model.ModeloAeronave
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
class ServiceSyncData : Service() {

    @Inject
    lateinit var aeronavesRepository: AeronavesRepository

    companion object {
        private const val TAG = "ServiceSyncData"
    }

    private val binder = LocalBinder()
    private val coroutineScope = CoroutineScope(Job())
    private var syncJob: Job? = null
    private var sessionUserManager: SessionUserManager? = null


    private var modelosAeronaveListDB: ArrayList<ModeloAeronave>? = null
    private var modelosAeronaveListCloud: ArrayList<ObtieneAeronavesDataTableCloudResponse>? = null


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
                modelosAeronaveListCloud = ArrayList(aeronavesRepository.getModeloAeronaveListCloud().data!!.table)
          //      aeronavesRepository.deleteTableModeloAeronaveDB()
                if (modelosAeronaveListCloud != null) {
                    for (itemcloud in modelosAeronaveListCloud!!) {

                        for(itemDB in modelosAeronaveListDB!!)
                        {
                            if(itemcloud.codigoModeloPuesto.equals(itemDB.id_cloud))
                            {
                                if(itemcloud.fechaModificacion!!>itemDB.fechaModificacion!!)
                                {
                                    modelosAeronaveListDB!!.add(itemcloud.toDomain())
                                }
                            }
                        }
                      //  modelosAeronaveListDB!!.add(itemcloud.toDomain())
                    }
                }
                aeronavesRepository.insertModeloAeronaveListDB(modelosAeronaveListDB!!)
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