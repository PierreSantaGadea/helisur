package com.helisur.helisurapp.data.cloud.usuario.model.response

import com.google.gson.annotations.SerializedName

class ObtieneDatosUsuarioCloudResponse (
    @SerializedName("success") var success: Int,
    @SerializedName("data") val data: ObtieneDatosUsuarioDataCloudResponse?,
    @SerializedName("message") var message: String,
)
{
    constructor() : this(
        0,
        null,
        ""
    )
}