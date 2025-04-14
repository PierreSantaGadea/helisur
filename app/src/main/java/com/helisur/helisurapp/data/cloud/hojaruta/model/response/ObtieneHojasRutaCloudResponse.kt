package com.helisur.helisurapp.data.cloud.hojaruta.model.response

import com.google.gson.annotations.SerializedName

class ObtieneHojasRutaCloudResponse (
    @SerializedName("success") var success: Int,
    @SerializedName("data") val data: ObtieneHojasRutaDataCloudResponse?,
    @SerializedName("message") var message: String,
)
{
    constructor() : this(
        0,
        null,
        ""
    )
}