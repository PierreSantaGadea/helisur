package com.helisur.helisurapp.data.cloud.gestionotos.apis

import com.helisur.helisurapp.data.cloud.gestionotos.model.parameter.ObtieneInsumosOTOSCloudParameter
import com.helisur.helisurapp.data.cloud.gestionotos.model.response.ObtieneInsumosByOTOSCloudResponse
import com.helisur.helisurapp.data.cloud.gestionotos.model.response.ObtieneOTOSCloudResponse

import com.helisur.helisurapp.domain.util.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface GestionOTOSApiClient {

    @GET(Constants.URLS.OBTIENE_OTOS)
    suspend fun obtieneOTOS(): Response<ObtieneOTOSCloudResponse>

    @POST(Constants.URLS.OBTIENE_STATUS_INSUMOS_OTOS)
    suspend fun obtieneInsumosByOT(@Body parameterBody: ObtieneInsumosOTOSCloudParameter): Response<ObtieneInsumosByOTOSCloudResponse>


}