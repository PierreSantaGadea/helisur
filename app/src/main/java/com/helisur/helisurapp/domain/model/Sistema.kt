package com.helisur.helisurapp.domain.model

import com.google.gson.annotations.SerializedName
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasDataTableCloudResponse
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity
import com.helisur.helisurapp.data.database.entities.response.SistemaEntity

class Sistema(
    var codigoFormato: String?,
    var codigoSistema: String?,
    var nombrePosicion: String?,
    var tareas: ArrayList<Tarea>?,
    var messageFailed: String?,
    var isSelected: Boolean? = false,
    var fechaRegistro: String,
    var fechaModificacion: String
) {
    constructor() : this(
        "", "", "", null, "", false,"",""
    )
}

fun SistemaEntity.toDomain() = Sistema(
    codigoFormato, id_cloud, nombrePosicion, null, "", false,fechaRegistro!!, fechaModificacion!!
)

fun ObtieneSistemasDataTableCloudResponse.toDomain() = Sistema(
    codigoFormato, codigoSistema, nombrePosicion, null, "", false,fechaRegistro, fechaModificacion
)







