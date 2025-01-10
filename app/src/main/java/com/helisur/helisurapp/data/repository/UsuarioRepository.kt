package com.helisur.helisurapp.data.repository

import com.helisur.helisurapp.data.cloud.usuario.apis.UsuarioService
import com.helisur.helisurapp.data.cloud.usuario.model.response.LoginCloudResponse
import com.helisur.helisurapp.data.database.dao.UsuarioDao
import com.helisur.helisurapp.domain.model.Usuario
import com.helisur.helisurapp.domain.model.toDomain
import javax.inject.Inject

class UsuarioRepository @Inject constructor (
    private val api: UsuarioService,
    private val usuarioDao: UsuarioDao
    )
{
    suspend fun login(usuario: String, contrasenia: String,token:String)
    : Usuario
    {
        val response: LoginCloudResponse = api.login(usuario, contrasenia)
        return response.toDomain()
    }

    suspend fun listaMenu(token: String)
 //   : ListaMenuCloudResponse
    {
    //    val response: ListaMenuCloudResponse = api.listaMenu(token)
   //     return response
    }




}