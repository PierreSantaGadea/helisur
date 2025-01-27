package com.helisur.helisurapp.data.cloud.usuario.model.response

import com.google.gson.annotations.SerializedName

data class ObtieneEmpleadoCloudResponse (
    @SerializedName("success") var success: Int,
    @SerializedName("message") var message: String,
    @SerializedName("data") var data: ObtieneEmpleadosDataCloudResponse?
)
{
    constructor() : this(
        0,
        "",
        null
    )
}