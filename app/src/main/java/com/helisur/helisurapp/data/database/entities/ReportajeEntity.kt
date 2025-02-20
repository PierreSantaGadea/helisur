package com.helisur.helisurapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneReportajesDataTableCloudResponse
import com.helisur.helisurapp.domain.model.Aeronave
import com.helisur.helisurapp.domain.model.ModeloAeronave
import com.helisur.helisurapp.domain.model.Reportaje

@Entity(tableName = "Reportaje")
class ReportajeEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "id_cloud") val id_cloud: String?,
    @ColumnInfo(name = "codigoTarea") val codigoTarea: String?,
    @ColumnInfo(name = "nombreReportaje") val nombreReportaje: String?,
    @ColumnInfo(name = "bloqueoFormato") val bloqueoFormato: String?,
    @ColumnInfo(name = "sync") val sync: Boolean?,
    @ColumnInfo(name = "fechaRegistro") val fechaRegistro: String?,
    @ColumnInfo(name = "fechaModificacion") val fechaModificacion: String?,
)

fun Reportaje.toDB() = ReportajeEntity(id_cloud = id_cloud, codigoTarea = codigoTarea, nombreReportaje = nombreReportaje, bloqueoFormato = bloqueoFormato, sync = true,fechaRegistro = fechaRegistro, fechaModificacion = fechaModificacion)

fun ObtieneReportajesDataTableCloudResponse.toDB() = ReportajeEntity(id_cloud = codigoReportaje,  codigoTarea = codigoTarea, nombreReportaje = nombreReportaje, bloqueoFormato = bloqueoFormato, sync = true,fechaRegistro = fechaRegistro, fechaModificacion = fechaModificacion)


