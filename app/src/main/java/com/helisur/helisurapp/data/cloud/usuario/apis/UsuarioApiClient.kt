package com.helisur.helisurapp.data.cloud.usuario.apis

import com.helisur.helisurapp.data.cloud.usuario.model.parameter.LoginCloudParameter
import com.helisur.helisurapp.data.cloud.usuario.model.response.LoginCloudResponse
import com.helisur.helisurapp.domain.util.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UsuarioApiClient {

    @POST(Constants.URLS.LOGIN)
    suspend fun login(@Body parameterBody: LoginCloudParameter): Response<LoginCloudResponse>

  //  @GET(Constants.URLS.LISTA_MENU)
  //  suspend fun listaMenu(@Header("Authorization") token: String): Response<ListaMenuCloudResponse>

}