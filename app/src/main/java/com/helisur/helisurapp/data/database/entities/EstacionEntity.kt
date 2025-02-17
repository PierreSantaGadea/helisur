package com.helisur.helisurapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneEstacionesDataTableCloudResponse
import com.helisur.helisurapp.domain.model.Estacion
import com.helisur.helisurapp.domain.model.ModeloAeronave

@Entity(tableName = "Estacion")
class EstacionEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "id_cloud") val id_cloud: String?,
    @ColumnInfo(name = "nombre") val nombre: String?,
    @ColumnInfo(name = "siglas") val siglas: String?,
    @ColumnInfo(name = "sync") val sync: Boolean?,
    @ColumnInfo(name = "fechaRegistro") val fechaRegistro: String?,
    @ColumnInfo(name = "fechaModificacion") val fechaModificacion: String?,
)

fun Estacion.toDB() = EstacionEntity(id_cloud = id_cloud, nombre =  nombre, siglas = siglas, sync = true, fechaRegistro = fechaRegistro, fechaModificacion = fechaModificacion)

fun ObtieneEstacionesDataTableCloudResponse.toDB() = EstacionEntity(id_cloud = id, nombre =  nombre, siglas = siglas, sync = true, fechaRegistro = fechaRegistro, fechaModificacion = fechaModificacion)


