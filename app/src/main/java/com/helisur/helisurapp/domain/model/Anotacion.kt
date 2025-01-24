package com.helisur.helisurapp.domain.model

import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasDataTableCloudResponse

class Anotacion(
    var codigoSistema: String?,
    var nombreSistema: String?,
    var codigoTarea: String?,
    var nombreTarea: String?,
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
