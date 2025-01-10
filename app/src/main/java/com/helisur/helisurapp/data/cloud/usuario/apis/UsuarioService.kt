package com.helisur.helisurapp.data.cloud.usuario.apis

import com.helisur.helisurapp.data.cloud.usuario.model.parameter.LoginCloudParameter
import com.helisur.helisurapp.data.cloud.usuario.model.response.LoginCloudResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsuarioService @Inject constructor
    ( private val api: UsuarioApiClient ) {

    suspend fun login(
        usuario: String, contrasenia: String
    ): LoginCloudResponse {
        val parameterBody = LoginCloudParameter(usuario, contrasenia)
        return withContext(Dispatchers.IO) {
            val response = api.login(parameterBody)
            response.body()!!
        }
    }



}