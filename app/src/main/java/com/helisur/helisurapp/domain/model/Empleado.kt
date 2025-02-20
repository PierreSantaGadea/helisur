package com.helisur.helisurapp.domain.model

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneEmpleadosDataTableCloudResponse
import com.helisur.helisurapp.data.database.entities.AeronaveEntity
import com.helisur.helisurapp.data.database.entities.EmpleadoEntity
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity

class Empleado(
    val id_cloud: String?,
    val codigoArea: String?,
    val numeroDocumento: String?,
    val nombre: String?,
    val apellidoPaterno: String?,
    val apellidoMaterno: String?,
    val nombreCompleto: String?,
    val email: String?,
    val estado: String?,
    val cargo: String?,
    val fechaIngreso: String?,
    val licencia: String?,
    val sync: Boolean?,
    val fechaRegistro: String?,
    val fechaModificacion: String?,
    val codigoUsuario:String?
) {
    constructor() : this(
         "","","","","","","","","","","","",null,"","",""
    )
}

//entidadDB pasa modelo
fun EmpleadoEntity.toDomain() = Empleado(
     id_cloud, codigoArea, numeroDocumento, nombre, apellidoPaterno, apellidoMaterno, nombreCompleto, email, estado, cargo, fechaIngreso, licencia, sync, fechaRegistro, fechaModificacion,codigoUsuario
)

//entidadCLOUD pasa a modelo
fun ObtieneEmpleadosDataTableCloudResponse.toDomain() = Empleado(
     id_cloud = codigoEmpleado, codigoArea = codigoArea, numeroDocumento = numeroDocumento, nombre = nombre, apellidoPaterno = apellidoPaterno, apellidoMaterno = apellidoMaterno, nombreCompleto = nombreCompleto, email = email,estado = estado,
    cargo = cargo, fechaIngreso = fechaIngreso, licencia = licencia, sync = true, fechaRegistro = fechaRegistro, fechaModificacion = fechaModificacion,codigoUsuario = codigoUsuario
)







