package com.helisur.helisurapp.data.repository

import android.util.Log
import com.helisur.helisurapp.data.cloud.aeronaves.apis.AeronavesService
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneEstacionesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveCloudResponse
import com.helisur.helisurapp.data.database.dao.ModeloAeronaveDao
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity
import com.helisur.helisurapp.data.database.entities.toDB
import com.helisur.helisurapp.domain.model.ModeloAeronave
import com.helisur.helisurapp.domain.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import javax.inject.Inject

class AeronavesRepository @Inject constructor(
    private val aeronavescloudData: AeronavesService,
    private val modeloAeronavelocalData: ModeloAeronaveDao
) {

    var className = "AeronavesRepository"

    suspend fun getModeloAeronaveListCloud(
    ): ObtieneAeronavesCloudResponse {
        val response: ObtieneAeronavesCloudResponse = aeronavescloudData.getModeloAeronaveList()
        return response
    }

    suspend fun getAeromaveListCloud(
        aeronave: String
    ): ObtieneModelosAeronaveCloudResponse {
        val response: ObtieneModelosAeronaveCloudResponse =
            aeronavescloudData.getAeromaveList(aeronave)
        return response
    }

    suspend fun getEstacionesListCloud(
    ): ObtieneEstacionesCloudResponse {
        val response: ObtieneEstacionesCloudResponse = aeronavescloudData.getEstacionesList()
        return response
    }


    suspend fun getModelosAeronavesListDB(): List<ModeloAeronave> {
        try {
            return withContext(Dispatchers.IO) {
                val response: List<ModeloAeronaveEntity> = modeloAeronavelocalData.getAll()
                response.map { it.toDomain() }
            }
        } catch (e: Exception) {
            val response: List<ModeloAeronave> = arrayListOf()
            Log.e(className, e.toString())
            return response
        }
    }

    suspend fun insertModeloAeronaveListDB(modelosAeronave: List<ModeloAeronave>) {
        try {
            return withContext(Dispatchers.IO) {
                var modeloAeronaveEntityList: ArrayList<ModeloAeronaveEntity> = arrayListOf()
                for (modeloAeronave in modelosAeronave) {
                    modeloAeronaveEntityList.add(modeloAeronave.toDB())
                }
                modeloAeronavelocalData.insertList(modeloAeronaveEntityList)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }


    suspend fun deleteTableModeloAeronaveDB() {
        try {
            return withContext(Dispatchers.IO) {
                modeloAeronavelocalData.deleteAll()
                modeloAeronavelocalData.deleteIndexModeloAeronave()
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }

    }


    suspend fun updateModeloAeronave(idCloud: String,nombre:String,fechaRegistro:String,fechaModificacion:String, sync: Boolean) {
        try {
            return withContext(Dispatchers.IO) {
                modeloAeronavelocalData.updateItem(idCloud,nombre,fechaRegistro,fechaModificacion, sync)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }


    suspend fun deleteModeloAeronave(idCloud: String) {
        try {
            return withContext(Dispatchers.IO) {
                modeloAeronavelocalData.deleteItem(idCloud)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }

    suspend fun updateSyncModeloAeronave(idCloud: String, sync: Boolean) {
        try {
            return withContext(Dispatchers.IO) {
                modeloAeronavelocalData.updateItemSync(idCloud = idCloud, sync = sync)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }


}