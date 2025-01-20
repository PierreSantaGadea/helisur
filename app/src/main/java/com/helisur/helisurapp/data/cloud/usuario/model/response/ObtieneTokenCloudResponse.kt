package com.helisur.helisurapp.data.cloud.usuario.model.response

import com.google.gson.annotations.SerializedName

class ObtieneTokenCloudResponse (
    @SerializedName("success") var success: Int,
    @SerializedName("token") val token: String,
    @SerializedName("message") var message: String,
)
{
    constructor() : this(
        0,
        "",
        ""
    )
}