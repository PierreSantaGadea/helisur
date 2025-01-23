package com.helisur.helisurapp.domain.model

import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasDataTableCloudResponse

class Sistema(
    var codigoFormato: String?,
    var codigoSistema: String?,
    var nombrePosicion: String?,
    var tareas: ArrayList<Tarea>?,
    var messageFailed: String?,
    var isSelected: Boolean? = false
) {
    constructor() : this(
        "", "", "", null, "", false
    )
}

fun ObtieneSistemasDataTableCloudResponse.toDomain() = Sistema(
    codigoFormato, codigoSistema, nombrePosicion, null, "", false
)







