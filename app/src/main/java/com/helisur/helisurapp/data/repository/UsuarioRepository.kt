package com.helisur.helisurapp.data.repository

import com.helisur.helisurapp.data.cloud.usuario.apis.UsuarioService
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneDatosUsuarioCloudResponse
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneEmpleadoCloudResponse
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneTokenCloudResponse
import com.helisur.helisurapp.data.database.dao.UsuarioDao
import com.helisur.helisurapp.domain.model.Usuario
import com.helisur.helisurapp.domain.model.toDomain
import javax.inject.Inject

class UsuarioRepository @Inject constructor(
    private val api: UsuarioService, private val usuarioDao: UsuarioDao
) {

    suspend fun obtieneTokenCloud(
        usuario: String, contrasenia: String
    ): ObtieneTokenCloudResponse {
        val response: ObtieneTokenCloudResponse = api.obtieneTokenCloud(usuario, contrasenia)
        return response
    }

    suspend fun obtieneDatosUsuarioCloud(
        usuario: String
    ): ObtieneDatosUsuarioCloudResponse {
        val response: ObtieneDatosUsuarioCloudResponse = api.obtieneDatosUsuarioCloud(usuario)
        return response
    }

    suspend fun obtieneEmpleados(
        area: String
    ): ObtieneEmpleadoCloudResponse {
        val response: ObtieneEmpleadoCloudResponse = api.obtieneEmpleados(area)
        return response
    }



}