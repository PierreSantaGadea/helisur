package com.helisur.helisurapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosDataTableCloudResponse
import com.helisur.helisurapp.domain.model.DetalleFormatoRegistro
import com.helisur.helisurapp.domain.model.Formato
import com.helisur.helisurapp.domain.model.ModeloAeronave

@Entity(tableName = "DetalleFormatoRegistro")
class DetalleFormatoRegistroEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "id_cloud") val id_cloud: String?,
    @ColumnInfo(name = "idRegistroFormatoDB") val idRegistroFormatoDB: String?,
    @ColumnInfo(name = "codigoRegistroFormato") var codigoRegistroFormato: String,
    @ColumnInfo(name = "codigoTarea") var codigoTarea: String,
    @ColumnInfo(name = "nombreTarea") var nombreTarea: String,
    @ColumnInfo(name = "codigoReportaje") var codigoReportaje: String,
    @ColumnInfo(name = "nombreReportaje") var nombreReportaje: String,
    @ColumnInfo(name = "indicadorSN") var indicadorSN: String,
    @ColumnInfo(name = "indicadorBloqueo") var indicadorBloqueo: String,

    @ColumnInfo(name = "sync") val sync: Boolean?,
    @ColumnInfo(name = "fechaRegistro") val fechaRegistro: String?,
    @ColumnInfo(name = "fechaModificacion") val fechaModificacion: String?



    )

fun DetalleFormatoRegistro.toDB() = DetalleFormatoRegistroEntity(id_cloud = id_cloud, idRegistroFormatoDB = idRegistroFormatoDB, codigoRegistroFormato = codigoRegistroFormato,
    codigoTarea = codigoTarea, nombreTarea = nombreTarea, codigoReportaje = codigoReportaje, nombreReportaje = nombreReportaje,
    indicadorSN = indicadorSN,indicadorBloqueo = indicadorBloqueo, sync = true,fechaRegistro = fechaRegistro, fechaModificacion = fechaModificacion)


/*
fun ObtieneFormatosDataTableCloudResponse.toDB() = FormatoEntity(id_cloud = codigoFormato, codigoModeloAeronave = codigoModeloAeronave, nombreFormato = nombreFormato, sync = true, fechaRegistro = fechaRegistro, fechaModificacion = fechaModificacion)


 */

