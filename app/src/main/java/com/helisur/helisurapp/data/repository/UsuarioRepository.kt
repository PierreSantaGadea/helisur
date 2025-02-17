package com.helisur.helisurapp.data.repository

import android.util.Log
import com.helisur.helisurapp.data.cloud.usuario.apis.UsuarioService
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneDatosUsuarioCloudResponse
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneEmpleadoCloudResponse
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneTokenCloudResponse
import com.helisur.helisurapp.data.database.dao.EmpleadoDao
import com.helisur.helisurapp.data.database.dao.UsuarioDao
import com.helisur.helisurapp.data.database.entities.EmpleadoEntity
import com.helisur.helisurapp.data.database.entities.FormatoEntity
import com.helisur.helisurapp.data.database.entities.toDB
import com.helisur.helisurapp.domain.model.Empleado
import com.helisur.helisurapp.domain.model.Formato
import com.helisur.helisurapp.domain.model.Usuario
import com.helisur.helisurapp.domain.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsuarioRepository @Inject constructor(
    private val usuarioCloudData: UsuarioService,
    private val usuarioLocalData: EmpleadoDao
) {

    var className = "UsuarioRepository"

    suspend fun obtieneTokenCloud(
        usuario: String, contrasenia: String
    ): ObtieneTokenCloudResponse {
        val response: ObtieneTokenCloudResponse = usuarioCloudData.obtieneTokenCloud(usuario, contrasenia)
        return response
    }

    suspend fun obtieneDatosUsuarioCloud(
        usuario: String
    ): ObtieneDatosUsuarioCloudResponse {
        val response: ObtieneDatosUsuarioCloudResponse = usuarioCloudData.obtieneDatosUsuarioCloud(usuario)
        return response
    }

    suspend fun obtieneEmpleados(
        area: String
    ): ObtieneEmpleadoCloudResponse {
        val response: ObtieneEmpleadoCloudResponse = usuarioCloudData.obtieneEmpleados(area)
        return response
    }



    // empleados

    suspend fun getEmpleadosListDB(): List<Empleado> {
        try {
            return withContext(Dispatchers.IO) {
                val response: List<EmpleadoEntity> = usuarioLocalData.getAll()
                response.map { it.toDomain() }
            }
        } catch (e: Exception) {
            val response: List<Empleado> = arrayListOf()
            Log.e(className, e.toString())
            return response
        }
    }

    suspend fun insertEmpleadoListDB(formatos: List<Empleado>) {
        try {
            return withContext(Dispatchers.IO) {
                var modeloAeronaveEntityList: ArrayList<EmpleadoEntity> = arrayListOf()
                for (modeloAeronave in formatos) {
                    modeloAeronaveEntityList.add(modeloAeronave.toDB())
                }
                usuarioLocalData.insertList(modeloAeronaveEntityList)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }


    suspend fun deleteTableEmpleadoDB() {
        try {
            return withContext(Dispatchers.IO) {
                usuarioLocalData.deleteAll()
                usuarioLocalData.deleteIndexEmpleado()
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }

    }

    /*
        suspend fun updateFormato(idCloud: String,codigoPuestoTecnico:String,codigoCliente:String,nombre:String,placa:String,comentario:String,html:String,fechaRegistro:String,fechaModificacion:String, sync: Boolean) {
            try {
                return withContext(Dispatchers.IO) {
                    //     aeronaveslocalData.updateItem(idCloud,codigoPuestoTecnico,codigoCliente,nombre,placa,comentario,html,fechaRegistro,fechaModificacion, sync)
                    var error: Boolean = true
                }
            } catch (e: Exception) {
                Log.e(className, e.toString())
                return withContext(Dispatchers.IO) {
                    var error: Boolean = false
                }
            }
        }


     */

    suspend fun deleteEmpleado(idCloud: String) {
        try {
            return withContext(Dispatchers.IO) {
                usuarioLocalData.deleteItem(idCloud)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }

    suspend fun updateSyncEmpleado(idCloud: String, sync: Boolean) {
        try {
            return withContext(Dispatchers.IO) {
                usuarioLocalData.updateItemSync(idCloud = idCloud, sync = sync)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }




}