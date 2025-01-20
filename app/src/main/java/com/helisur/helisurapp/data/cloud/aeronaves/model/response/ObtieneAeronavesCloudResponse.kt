package com.helisur.helisurapp.data.cloud.aeronaves.model.response

import com.google.gson.annotations.SerializedName

class ObtieneAeronavesCloudResponse (
    @SerializedName("success") var success: Int,
    @SerializedName("data") val data: ObtieneAeronavesDataCloudResponse?,
    @SerializedName("message") var message: String,
)
{
    constructor() : this(
        0,
        null,
        ""
    )
}