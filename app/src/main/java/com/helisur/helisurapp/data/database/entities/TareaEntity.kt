package com.helisur.helisurapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasDataTableCloudResponse
import com.helisur.helisurapp.domain.model.ModeloAeronave
import com.helisur.helisurapp.domain.model.Tarea

@Entity(tableName = "Tarea")
class TareaEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "id_cloud") val id_cloud: String?,
    @ColumnInfo("codigoSistema") var codigoSistema: String,
    @ColumnInfo("nombreTarea") var nombreTarea: String,
    @ColumnInfo(name = "sync") val sync: Boolean?,
    @ColumnInfo(name = "fechaRegistro") val fechaRegistro: String?,
    @ColumnInfo(name = "fechaModificacion") val fechaModificacion: String?,
)

fun Tarea.toDB() = TareaEntity(id_cloud = codigoTarea, codigoSistema = codigoSistema!!, nombreTarea = nombreTarea!!, sync = true, fechaRegistro = fechaRegistro, fechaModificacion = fechaModificacion)

fun ObtieneTareasDataTableCloudResponse.toDB() = TareaEntity(id_cloud = codigoTarea, codigoSistema = codigoSistema, nombreTarea = nombreTarea!!, sync = true, fechaRegistro = fechaRegistro, fechaModificacion = fechaModificacion)


