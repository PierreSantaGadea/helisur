package com.helisur.helisurapp.data.cloud.formatos.apis

import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveCloudResponse
import com.helisur.helisurapp.domain.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface FormatosApiClient {

    @GET(Constants.URLS.OBTIENE_AERONAVES)
    suspend fun obtieneAeronaves(): Response<ObtieneAeronavesCloudResponse>


}