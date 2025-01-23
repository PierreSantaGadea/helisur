package com.helisur.helisurapp.data.repository

import com.helisur.helisurapp.data.cloud.formatos.apis.FormatosService
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasCloudResponse

import javax.inject.Inject

class FormatosRepository @Inject constructor(
    private val api: FormatosService
) {

    suspend fun obtieneFormatos(
    ): ObtieneFormatosCloudResponse {
        val response: ObtieneFormatosCloudResponse = api.obtieneFormatos()
        return response
    }

    suspend fun obtieneSistemas(
        codigoFormato: String
    ): ObtieneSistemasCloudResponse {
        val response: ObtieneSistemasCloudResponse = api.obtieneSistemas(codigoFormato)
        return response
    }

    suspend fun obtieneTareas(
        codigoSistema: String
    ): ObtieneTareasCloudResponse {
        val response: ObtieneTareasCloudResponse = api.obtieneTareas(codigoSistema)
        return response
    }


}