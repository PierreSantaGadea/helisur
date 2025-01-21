package com.helisur.helisurapp.data.cloud.aeronaves.apis

import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveCloudResponse
import com.helisur.helisurapp.domain.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface AeronavesApiClient {

    @GET(Constants.URLS.OBTIENE_AERONAVES)
    suspend fun obtieneAeronaves(): Response<ObtieneAeronavesCloudResponse>

    @GET
    suspend fun obtieneModelosAeronave(@Url url:String):Response<ObtieneModelosAeronaveCloudResponse>
}