package com.helisur.helisurapp.usercases

import android.util.Log
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasCloudResponse
import com.helisur.helisurapp.data.repository.FormatosRepository
import com.helisur.helisurapp.domain.util.Constants
import javax.inject.Inject

class FormatosUseCase @Inject constructor(private val repository: FormatosRepository) {

    val TAG = "FormatosUseCase"

    suspend fun obtieneFormatos(): ObtieneFormatosCloudResponse {
        try {
            val respuesta = repository.obtieneFormatos()
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            val responseFailed = ObtieneFormatosCloudResponse()
            responseFailed.success = Constants.ERROR.ERROR_ENTERO
            responseFailed.message = e.toString()
            return responseFailed
        }
    }


    suspend fun obtieneSistemas(codigoFormato: String): ObtieneSistemasCloudResponse {
        try {
            val respuesta = repository.obtieneSistemas(codigoFormato)
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            val responseFailed = ObtieneSistemasCloudResponse()
            responseFailed.success = Constants.ERROR.ERROR_ENTERO
            responseFailed.message = e.message.toString()
            return responseFailed
        }
    }


    suspend fun obtieneTareas(codigoSistema: String): ObtieneTareasCloudResponse {
        try {
            val respuesta = repository.obtieneTareas(codigoSistema)
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            val responseFailed = ObtieneTareasCloudResponse()
            responseFailed.success = Constants.ERROR.ERROR_ENTERO
            responseFailed.message = e.message.toString()
            return responseFailed
        }
    }



}