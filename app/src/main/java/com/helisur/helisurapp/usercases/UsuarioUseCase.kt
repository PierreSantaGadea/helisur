package com.helisur.helisurapp.usercases

import android.util.Log
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneDatosUsuarioCloudResponse
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneTokenCloudResponse
import com.helisur.helisurapp.data.repository.UsuarioRepository
import com.helisur.helisurapp.domain.model.Usuario
import com.helisur.helisurapp.domain.util.Constants
import javax.inject.Inject

class UsuarioUseCase  @Inject constructor
    (private val repository: UsuarioRepository) {

        val TAG = "UsuarioUseCase"

    suspend fun obtieneTokenCloud(usuario: String, contrasenia: String): ObtieneTokenCloudResponse {
        try {
            val respuesta = repository.obtieneTokenCloud(usuario, contrasenia)
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            val userFail = ObtieneTokenCloudResponse()
            userFail.success = Constants.ERROR.ERROR_ENTERO
            userFail.message = e.message.toString()
            return userFail
        }
    }


    suspend fun obtieneDatosUsuarioCloud(usuario:String): ObtieneDatosUsuarioCloudResponse {
        try {
            val respuesta = repository.obtieneDatosUsuarioCloud(usuario)
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            val userFail = ObtieneDatosUsuarioCloudResponse()
            userFail.success = Constants.ERROR.ERROR_ENTERO
            userFail.message = e.message.toString()
            return userFail
        }
    }





}