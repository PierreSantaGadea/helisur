package com.helisur.helisurapp.data.cloud.gestionotos.model.response

import com.google.gson.annotations.SerializedName

class ObtieneOTOSCloudResponse (
    @SerializedName("success") var success: Int,
    @SerializedName("data") val data: ObtieneOTOSDataCloudResponse?,
    @SerializedName("message") var message: String,
)
{
    constructor() : this(
        0,
        null,
        ""
    )
}