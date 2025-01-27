package com.helisur.helisurapp.data.cloud.formatos.model.response

import com.google.gson.annotations.SerializedName

class GrabaFormatoCloudResponse (
    @SerializedName("success") var success: Int,
    @SerializedName("data") val data: ObtieneFormatosDataCloudResponse?,
    @SerializedName("message") var message: String,
)
{
    constructor() : this(
        0,
        null,
        ""
    )
}