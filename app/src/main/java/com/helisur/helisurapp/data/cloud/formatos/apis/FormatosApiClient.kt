package com.helisur.helisurapp.data.cloud.formatos.apis

import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaFormatoCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ObtieneSistemasCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ObtieneTareasCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.response.GrabaFormatoCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasCloudResponse

import com.helisur.helisurapp.domain.util.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface FormatosApiClient {

    @GET(Constants.URLS.OBTIENE_FORMATOS)
    suspend fun obtieneFormatos(): Response<ObtieneFormatosCloudResponse>

    @POST(Constants.URLS.OBTIENE_SISTEMAS)
    suspend fun obtieneSistemas(@Body parameterBody: ObtieneSistemasCloudParameter): Response<ObtieneSistemasCloudResponse>

    @POST(Constants.URLS.OBTIENE_TAREAS)
    suspend fun obtieneTareas(@Body parameterBody: ObtieneTareasCloudParameter): Response<ObtieneTareasCloudResponse>

    @POST(Constants.URLS.GRABA_FORMATO)
    suspend fun grabaFormato(@Body parameterBody: GuardaFormatoCloudParameter): Response<GrabaFormatoCloudResponse>


}