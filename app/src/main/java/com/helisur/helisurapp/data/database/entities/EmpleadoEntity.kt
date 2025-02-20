package com.helisur.helisurapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneEmpleadosDataTableCloudResponse
import com.helisur.helisurapp.domain.model.Aeronave
import com.helisur.helisurapp.domain.model.Empleado
import com.helisur.helisurapp.domain.model.ModeloAeronave

@Entity(tableName = "Empleado")
class EmpleadoEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "id_cloud") val id_cloud: String?,
    @ColumnInfo(name = "codigoArea") val codigoArea: String?,
    @ColumnInfo(name = "numeroDocumento") val numeroDocumento: String?,
    @ColumnInfo(name = "nombre") val nombre: String?,
    @ColumnInfo(name = "codigoUsuario") val codigoUsuario: String?,
    @ColumnInfo(name = "apellidoPaterno") val apellidoPaterno: String?,
    @ColumnInfo(name = "apellidoMaterno") val apellidoMaterno: String?,
    @ColumnInfo(name = "nombreCompleto") val nombreCompleto: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "estado") val estado: String?,
    @ColumnInfo(name = "cargo") val cargo: String?,
    @ColumnInfo(name = "fechaIngreso") val fechaIngreso: String?,
    @ColumnInfo(name = "licencia") val licencia: String?,
    @ColumnInfo(name = "sync") val sync: Boolean?,
    @ColumnInfo(name = "fechaRegistro") val fechaRegistro: String?,
    @ColumnInfo(name = "fechaModificacion") val fechaModificacion: String?

)

fun Empleado.toDB() = EmpleadoEntity(id_cloud = id_cloud, codigoArea = codigoArea, numeroDocumento = numeroDocumento, nombre = nombre, apellidoPaterno = apellidoPaterno, apellidoMaterno = apellidoMaterno, nombreCompleto = nombreCompleto, email = email,
    estado = estado, cargo = cargo, fechaIngreso = fechaIngreso, licencia = licencia, sync = sync, fechaRegistro = fechaRegistro, fechaModificacion = fechaModificacion,codigoUsuario = codigoUsuario)

fun ObtieneEmpleadosDataTableCloudResponse.toDB() = EmpleadoEntity(id_cloud = codigoEmpleado, codigoArea = codigoArea, numeroDocumento = numeroDocumento, nombre = nombre, apellidoPaterno = apellidoPaterno, apellidoMaterno = apellidoMaterno, nombreCompleto = nombreCompleto, email = email,
    estado = estado, cargo = cargo, fechaIngreso = fechaIngreso, licencia = licencia, sync = true, fechaRegistro = fechaRegistro, fechaModificacion = fechaModificacion,codigoUsuario = codigoUsuario)


