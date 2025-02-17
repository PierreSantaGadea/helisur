package com.helisur.helisurapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveDataTableCloudResponse
import com.helisur.helisurapp.domain.model.Aeronave
import com.helisur.helisurapp.domain.model.ModeloAeronave

@Entity(tableName = "Aeronave")
class AeronaveEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "id_cloud") val id_cloud: String?,
    @ColumnInfo(name = "codigoPuestoTecnico") val codigoPuestoTecnico: String?,
    @ColumnInfo(name = "codigoCliente") val codigoCliente: String?,
    @ColumnInfo(name = "nombre") val nombre: String?,
    @ColumnInfo(name = "placa") val placa: String?,
    @ColumnInfo(name = "comentario") val comentario: String?,
    @ColumnInfo(name = "html") val html: String?,
    @ColumnInfo(name = "sync") val sync: Boolean?,
    @ColumnInfo(name = "fechaRegistro") val fechaRegistro: String?,
    @ColumnInfo(name = "fechaModificacion") val fechaModificacion: String?,
)

fun Aeronave.toDB() = AeronaveEntity(id_cloud = id_cloud, codigoPuestoTecnico = codigoPuestoTecnico, codigoCliente = codigoCliente, nombre = nombre, placa = placa, comentario = comentario, html = html, sync = true,fechaRegistro = fechaRegistro, fechaModificacion = fechaModificacion)

fun ObtieneModelosAeronaveDataTableCloudResponse.toDB() = AeronaveEntity(id_cloud = codigoModeloPuesto,  codigoPuestoTecnico = codigoPuestoTecnico, codigoCliente = codigoCliente, nombre = nombre, placa = placa, comentario = comentario, html = html, sync = true,fechaRegistro = fechaRegistro, fechaModificacion = fechaModificacion)


