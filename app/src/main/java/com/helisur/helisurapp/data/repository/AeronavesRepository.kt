package com.helisur.helisurapp.data.repository

import com.helisur.helisurapp.data.cloud.aeronaves.apis.AeronavesService
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneEstacionesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveCloudResponse

import javax.inject.Inject

class AeronavesRepository @Inject constructor(
    private val api: AeronavesService
) {

    suspend fun obtieneAeronaves(
    ): ObtieneAeronavesCloudResponse {
        val response: ObtieneAeronavesCloudResponse = api.obtieneAeronaves()
        return response
    }

    suspend fun obtieneModelosAeronave(
        aeronave: String
    ): ObtieneModelosAeronaveCloudResponse {
        val response: ObtieneModelosAeronaveCloudResponse = api.obtieneModelosAeronave(aeronave)
        return response
    }

    suspend fun obtieneEstaciones(
    ): ObtieneEstacionesCloudResponse {
        val response: ObtieneEstacionesCloudResponse = api.obtieneEstaciones()
        return response
    }


}