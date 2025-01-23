package com.helisur.helisurapp.data.cloud.formatos.model.response

import com.google.gson.annotations.SerializedName

class ObtienePrevuelosRealizadosCloudResponse (
    @SerializedName("success") var success: Int,
    @SerializedName("message") var message: String,
)
{
    constructor() : this(
        0,
        ""
    )
}