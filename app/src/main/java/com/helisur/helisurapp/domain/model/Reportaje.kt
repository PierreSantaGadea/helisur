package com.helisur.helisurapp.domain.model

class Reportaje(

    var reportaje_NoAplica: Boolean = false,
    var reportaje_RTV: Boolean = false,
    var reportaje_DanosMenores: Boolean = false,
    var reportaje_MELMDS: Boolean = false,
    var reportaje_Motivo: String? = ""
) {
    constructor() : this(
      false, false, false, false, ""
    )
}

