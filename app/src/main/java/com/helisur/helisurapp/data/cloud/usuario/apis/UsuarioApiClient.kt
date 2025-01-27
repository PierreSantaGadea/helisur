package com.helisur.helisurapp.data.cloud.usuario.apis

import com.helisur.helisurapp.data.cloud.usuario.model.parameter.LoginCloudParameter
import com.helisur.helisurapp.data.cloud.usuario.model.parameter.ObtieneEmpleadosCloudParameter
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneDatosUsuarioCloudResponse
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneEmpleadoCloudResponse
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneTokenCloudResponse
import com.helisur.helisurapp.domain.util.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface UsuarioApiClient {

    @POST(Constants.URLS.OBTIENE_TOKEN)
    suspend fun obtieneTokenCloud(@Body parameterBody: LoginCloudParameter): Response<ObtieneTokenCloudResponse>

    @GET
    suspend fun obtieneDatosUsuario(@Url url:String):Response<ObtieneDatosUsuarioCloudResponse>


    @POST(Constants.URLS.OBTIENE_EMPLEADOS)
    suspend fun obtieneEmpleados(@Body parameterBody: ObtieneEmpleadosCloudParameter): Response<ObtieneEmpleadoCloudResponse>
}