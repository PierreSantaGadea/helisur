package com.helisur.helisurapp.domain.model

import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasDataTableCloudResponse

class Tarea(
    var codigoTarea: String?,
    var codigoSistema: String?,
    var nombreTarea: String?,
    var messageFailed: String?,

    var reportaje_NoAplica: Boolean = false,
    var reportaje_RTV: Boolean = false,
    var reportaje_DanosMenores: Boolean = false,
    var reportaje_MELMDS: Boolean = false,
    var reportaje_Motivo: String? = ""
) {
    constructor() : this(
        "", "", "", "", false, false, false, false, ""
    )
}

fun ObtieneTareasDataTableCloudResponse.toDomain() = Tarea(
    codigoTarea, codigoSistema, nombreTarea, "", false, false, false, false, ""
)
