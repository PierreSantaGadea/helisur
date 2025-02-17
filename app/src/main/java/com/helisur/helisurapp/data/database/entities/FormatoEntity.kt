package com.helisur.helisurapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosDataTableCloudResponse
import com.helisur.helisurapp.domain.model.Formato
import com.helisur.helisurapp.domain.model.ModeloAeronave

@Entity(tableName = "Formato")
class FormatoEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "id_cloud") val id_cloud: String?,
    @ColumnInfo(name = "codigoModeloAeronave") val codigoModeloAeronave: String?,
    @ColumnInfo(name = "nombreFormato") val nombreFormato: String?,
    @ColumnInfo(name = "sync") val sync: Boolean?,
    @ColumnInfo(name = "fechaRegistro") val fechaRegistro: String?,
    @ColumnInfo(name = "fechaModificacion") val fechaModificacion: String?,
)

fun Formato.toDB() = FormatoEntity(id_cloud = id_cloud, codigoModeloAeronave = codigoModeloAeronave, nombreFormato = nombreFormato, sync = true, fechaRegistro = fechaRegistro, fechaModificacion = fechaModificacion)

fun ObtieneFormatosDataTableCloudResponse.toDB() = FormatoEntity(id_cloud = codigoFormato, codigoModeloAeronave = codigoModeloAeronave, nombreFormato = nombreFormato, sync = true, fechaRegistro = fechaRegistro, fechaModificacion = fechaModificacion)


