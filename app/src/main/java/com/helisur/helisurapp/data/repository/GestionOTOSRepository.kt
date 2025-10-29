package com.helisur.helisurapp.data.repository


import com.helisur.helisurapp.data.cloud.gestionotos.apis.GestionOTOSService
import com.helisur.helisurapp.data.cloud.gestionotos.model.response.ObtieneOTOSCloudResponse


import javax.inject.Inject

class GestionOTOSRepository @Inject constructor(
    private val gestionOTOSService: GestionOTOSService
) {

    var className = "GestionOTOSRepository"

    suspend fun obtieneOTOS(
    ): ObtieneOTOSCloudResponse {
        val response: ObtieneOTOSCloudResponse = gestionOTOSService.obtieneOTOS()
        return response
    }

    /*
    suspend fun obtieneSistemas(
        codigoFormato: String
    ): ObtieneSistemasCloudResponse {
        val response: ObtieneSistemasCloudResponse = api.obtieneSistemas(codigoFormato)
        return response
    }

     */












}