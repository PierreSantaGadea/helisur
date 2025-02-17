package com.helisur.helisurapp.data.database.entities.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasDataTableCloudResponse
import com.helisur.helisurapp.domain.model.Aeronave
import com.helisur.helisurapp.domain.model.ModeloAeronave
import com.helisur.helisurapp.domain.model.Sistema

@Entity(tableName = "Sistema")
class SistemaEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "id_cloud") val id_cloud: String?,
    @ColumnInfo(name = "codigoFormato") val codigoFormato: String?,
    @ColumnInfo(name = "nombrePosicion") val nombrePosicion: String?,
    @ColumnInfo(name = "sync") val sync: Boolean?,
    @ColumnInfo(name = "fechaRegistro") val fechaRegistro: String?,
    @ColumnInfo(name = "fechaModificacion") val fechaModificacion: String?,
)

fun Sistema.toDB() = SistemaEntity(id_cloud = codigoSistema, codigoFormato = codigoFormato, nombrePosicion = nombrePosicion, sync = true, fechaRegistro = fechaRegistro, fechaModificacion = fechaModificacion)

fun ObtieneSistemasDataTableCloudResponse.toDB() = SistemaEntity(id_cloud = codigoSistema, codigoFormato = codigoFormato, nombrePosicion = nombrePosicion, sync = true, fechaRegistro = fechaRegistro, fechaModificacion = fechaModificacion)


