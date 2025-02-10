package com.helisur.helisurapp.usercases

import android.util.Log
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneEstacionesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveCloudResponse
import com.helisur.helisurapp.data.database.entities.response.DeleteModeloAeronaveListResponse
import com.helisur.helisurapp.data.database.entities.response.InsertModeloAeronaveListResponse
import com.helisur.helisurapp.data.database.entities.response.ListModeloAeronaveResponse
import com.helisur.helisurapp.data.repository.AeronavesRepository
import com.helisur.helisurapp.domain.model.ModeloAeronave
import com.helisur.helisurapp.domain.util.Constants
import javax.inject.Inject

class AeronavesUseCase @Inject constructor(private val repository: AeronavesRepository) {

    val TAG = "AeronavesUseCase"

    suspend fun getModeloAeronaveListCloud(): ObtieneAeronavesCloudResponse {
        try {
            val respuesta = repository.getModeloAeronaveListCloud()
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            val responseFailed = ObtieneAeronavesCloudResponse()
            responseFailed.success = Constants.ERROR.ERROR_ENTERO
            responseFailed.message = e.toString()
            return responseFailed
        }
    }


    suspend fun getAeromaveListCloud(aeronave: String): ObtieneModelosAeronaveCloudResponse {
        try {
            val respuesta = repository.getAeromaveListCloud(aeronave)
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            val responseFailed = ObtieneModelosAeronaveCloudResponse()
            responseFailed.success = Constants.ERROR.ERROR_ENTERO
            responseFailed.message = e.toString()
            return responseFailed
        }
    }


    suspend fun getEstacionesListCloud(): ObtieneEstacionesCloudResponse {
        try {
            val respuesta = repository.getEstacionesListCloud()
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            val responseFailed = ObtieneEstacionesCloudResponse()
            responseFailed.success = Constants.ERROR.ERROR_ENTERO
            responseFailed.message = e.toString()
            return responseFailed
        }
    }



    suspend fun getModelosAeronavesListDB(): ListModeloAeronaveResponse {
        try {
            val respuesta = repository.getModelosAeronavesListDB()
            return ListModeloAeronaveResponse(true,respuesta,"")
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            return ListModeloAeronaveResponse(false,null,e.toString())
        }
    }


    suspend fun insertModeloAeronaveListDB(aeronavesList: List<ModeloAeronave>): InsertModeloAeronaveListResponse {
        try {
            val respuesta = repository.insertModeloAeronaveListDB(aeronavesList)
            val response= InsertModeloAeronaveListResponse(true,"")
            return response
        } catch (e: Exception) {
            val response= InsertModeloAeronaveListResponse(false,e.toString())
            return response
        }
    }

    suspend fun deleteTableModeloAeronaveDB(): DeleteModeloAeronaveListResponse {
        try {
            val respuesta = repository.deleteTableModeloAeronaveDB()
            val response= DeleteModeloAeronaveListResponse(true,"")
            return response
        } catch (e: Exception) {
            val response= DeleteModeloAeronaveListResponse(false,e.toString())
            return response
        }
    }


}