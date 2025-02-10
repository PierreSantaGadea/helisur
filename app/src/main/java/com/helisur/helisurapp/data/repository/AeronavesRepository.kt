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
    private val cloudData: AeronavesService,
    private val localData: ModeloAeronaveDao
) {

    var className = "AeronavesRepository"

    suspend fun getModeloAeronaveListCloud(
    ): ObtieneAeronavesCloudResponse {
        val response: ObtieneAeronavesCloudResponse = cloudData.getModeloAeronaveList()
        return response
    }

    suspend fun getAeromaveListCloud(
        aeronave: String
    ): ObtieneModelosAeronaveCloudResponse {
        val response: ObtieneModelosAeronaveCloudResponse = cloudData.getAeromaveList(aeronave)
        return response
    }

    suspend fun getEstacionesListCloud(
    ): ObtieneEstacionesCloudResponse {
        val response: ObtieneEstacionesCloudResponse = cloudData.getEstacionesList()
        return response
    }


    suspend fun getModelosAeronavesListDB(): List<ModeloAeronave> {
        try {
           return withContext(Dispatchers.IO) {
                val response: List<ModeloAeronaveEntity> = localData.getAll()
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
                localData.insertList(modeloAeronaveEntityList)
                var error : Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error : Boolean = false
            }
        }
    }


    suspend fun deleteTableModeloAeronaveDB() {
        try {
            return withContext(Dispatchers.IO) {
                localData.deleteAll()
                var error : Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error : Boolean = false
            }
        }

    }


}