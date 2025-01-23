package com.helisur.helisurapp.data.cloud.formatos.model.response

import com.google.gson.annotations.SerializedName

class ObtieneSistemasCloudResponse (
    @SerializedName("success") var success: Int,
    @SerializedName("data") val data: ObtieneSistemasDataCloudResponse?,
    @SerializedName("message") var message: String,
)
{
    constructor() : this(
        0,
        null,
        ""
    )
}