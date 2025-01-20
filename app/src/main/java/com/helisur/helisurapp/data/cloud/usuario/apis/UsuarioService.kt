package com.helisur.helisurapp.data.cloud.usuario.apis

import com.helisur.helisurapp.data.cloud.usuario.model.parameter.LoginCloudParameter
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneDatosUsuarioCloudResponse
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneTokenCloudResponse
import com.helisur.helisurapp.domain.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsuarioService @Inject constructor
    ( private val api: UsuarioApiClient ) {

    suspend fun obtieneTokenCloud(
        usuario: String, contrasenia: String
    ): ObtieneTokenCloudResponse {
        val parameterBody = LoginCloudParameter(usuario, contrasenia)
        return withContext(Dispatchers.IO) {
            val response = api.obtieneTokenCloud(parameterBody)
            when (response.code()) {
                Constants.RESPONSE_CODE._200 ->
                    response.body()!!
                Constants.RESPONSE_CODE._400 ->
                    ObtieneTokenCloudResponse(Constants.RESPONSE_CODE.FAILED,"",Constants.ERROR.MESSAGE_400)
                Constants.RESPONSE_CODE._401 ->
                    ObtieneTokenCloudResponse(Constants.RESPONSE_CODE.FAILED,"",Constants.ERROR.MESSAGE_401)
                Constants.RESPONSE_CODE._403 ->
                    ObtieneTokenCloudResponse(Constants.RESPONSE_CODE.FAILED,"",Constants.ERROR.MESSAGE_403)
                Constants.RESPONSE_CODE._404 ->
                    ObtieneTokenCloudResponse(Constants.RESPONSE_CODE.FAILED,"",Constants.ERROR.MESSAGE_404)
                Constants.RESPONSE_CODE._500 ->
                    ObtieneTokenCloudResponse(Constants.RESPONSE_CODE.FAILED,"",Constants.ERROR.MESSAGE_500)
                else -> { // Note the block
                    ObtieneTokenCloudResponse(Constants.RESPONSE_CODE.FAILED,"",Constants.ERROR.MESSAGE_OTHER)
                }
            }
        }
    }


    suspend fun obtieneDatosUsuarioCloud(
        usuario: String
    ): ObtieneDatosUsuarioCloudResponse {
        var url:String = Constants.URLS.OBTIENE_DATOS_USUARIO+usuario
        return withContext(Dispatchers.IO) {
            val response = api.obtieneDatosUsuario(url)
            when (response.code()) {
                Constants.RESPONSE_CODE._200 ->
                    response.body()!!
                Constants.RESPONSE_CODE._400 ->
                    ObtieneDatosUsuarioCloudResponse(Constants.RESPONSE_CODE.FAILED,null,Constants.ERROR.MESSAGE_400)
                Constants.RESPONSE_CODE._401 ->
                    ObtieneDatosUsuarioCloudResponse(Constants.RESPONSE_CODE.FAILED,null,Constants.ERROR.MESSAGE_401)
                Constants.RESPONSE_CODE._403 ->
                    ObtieneDatosUsuarioCloudResponse(Constants.RESPONSE_CODE.FAILED,null,Constants.ERROR.MESSAGE_403)
                Constants.RESPONSE_CODE._404 ->
                    ObtieneDatosUsuarioCloudResponse(Constants.RESPONSE_CODE.FAILED,null,Constants.ERROR.MESSAGE_404)
                Constants.RESPONSE_CODE._500 ->
                    ObtieneDatosUsuarioCloudResponse(Constants.RESPONSE_CODE.FAILED,null,Constants.ERROR.MESSAGE_500)
                else -> { // Note the block
                    ObtieneDatosUsuarioCloudResponse(Constants.RESPONSE_CODE.FAILED,null,Constants.ERROR.MESSAGE_OTHER)
                }
            }
        }
    }



}