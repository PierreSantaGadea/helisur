package com.helisur.helisurapp.usercases

import android.util.Log
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneEstacionesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveCloudResponse
import com.helisur.helisurapp.data.repository.AeronavesRepository
import com.helisur.helisurapp.domain.util.Constants
import javax.inject.Inject

class AeronavesUseCase @Inject constructor(private val repository: AeronavesRepository) {

    val TAG = "AeronavesUseCase"

    suspend fun obtieneAeronaves(): ObtieneAeronavesCloudResponse {
        try {
            val respuesta = repository.obtieneAeronaves()
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            val responseFailed = ObtieneAeronavesCloudResponse()
            responseFailed.success = Constants.ERROR.ERROR_ENTERO
            responseFailed.message = e.toString()
            return responseFailed
        }
    }


    suspend fun obtieneModelosAeronave(aeronave: String): ObtieneModelosAeronaveCloudResponse {
        try {
            val respuesta = repository.obtieneModelosAeronave(aeronave)
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            val responseFailed = ObtieneModelosAeronaveCloudResponse()
            responseFailed.success = Constants.ERROR.ERROR_ENTERO
            responseFailed.message = e.toString()
            return responseFailed
        }
    }


    suspend fun obtieneEstaciones(): ObtieneEstacionesCloudResponse {
        try {
            val respuesta = repository.obtieneEstaciones()
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            val responseFailed = ObtieneEstacionesCloudResponse()
            responseFailed.success = Constants.ERROR.ERROR_ENTERO
            responseFailed.message = e.toString()
            return responseFailed
        }
    }


}