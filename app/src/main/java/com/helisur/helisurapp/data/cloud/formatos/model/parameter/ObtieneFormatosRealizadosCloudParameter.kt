package com.helisur.helisurapp.data.cloud.formatos.model.parameter

import com.google.gson.annotations.SerializedName

class ObtieneFormatosRealizadosCloudParameter (
    @SerializedName("cadena") var codigoFormato : String,
    @SerializedName("cadena2") var formatosHoy : String,
)

{
    constructor() : this(
        "",
        ""
    )
}
