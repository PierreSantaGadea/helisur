package com.helisur.helisurapp.domain.model

import com.google.gson.annotations.SerializedName
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasDataTableCloudResponse
import com.helisur.helisurapp.data.database.entities.TareaEntity
import com.helisur.helisurapp.data.database.entities.response.SistemaEntity

class Tarea(
    var codigoTarea: String?,
    var codigoSistema: String?,
    var nombreTarea: String?,
    var messageFailed: String?,

    var reportaje_NoAplica: Boolean = false,
    var reportaje_RTV: Boolean = false,
    var reportaje_DanosMenores: Boolean = false,
    var reportaje_MELMDS: Boolean = false,
    var reportaje_Motivo: String? = "",
   var fechaRegistro: String,
  var fechaModificacion: String
) {
    constructor() : this(
        "", "", "", "", false, false, false, false, "","",""
    )
}

fun TareaEntity.toDomain() = Tarea(
    id_cloud, codigoSistema, nombreTarea, "", false, false, false, false, "",fechaRegistro!!, fechaModificacion!!
)

fun ObtieneTareasDataTableCloudResponse.toDomain() = Tarea(
    codigoTarea, codigoSistema, nombreTarea, "", false, false, false, false, "",fechaRegistro, fechaModificacion
)
