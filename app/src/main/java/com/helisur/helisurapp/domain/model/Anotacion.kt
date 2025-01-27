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
    var reportaje_Motivo: String? = "",
    var id_NoAplica:String = "00001",
    var id_RTV:String = "00002",
    var id_DanosMenores:String = "00003",
    var id_MELMDS:String = "00004",
) {
    constructor() : this(
        "", "", "", "",
        false, false, false, false, "",
        "00001","00002","00003","00004"
    )
}
