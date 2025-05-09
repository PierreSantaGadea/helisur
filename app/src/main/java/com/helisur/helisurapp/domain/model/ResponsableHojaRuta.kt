package com.helisur.helisurapp.domain.model

import com.google.gson.annotations.SerializedName
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasDataTableCloudResponse
import com.helisur.helisurapp.data.database.entities.TareaEntity
import com.helisur.helisurapp.data.database.entities.response.SistemaEntity

class ResponsableHojaRuta(
    var id: String?,
    var codigoResponsable: String?,
    var empleado: String?,
    var indicadorTitular: String?,
    var usuarioRegistro: String? = "",
   var fechaRegistro: String,
    var usuarioModificacion: String? = "",
  var fechaModificacion: String
) {
    constructor() : this(
        "", "", "", "",
        "","","",""
    )
}

