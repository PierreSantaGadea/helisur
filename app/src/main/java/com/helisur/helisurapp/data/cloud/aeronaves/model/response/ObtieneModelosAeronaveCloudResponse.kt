package com.helisur.helisurapp.data.cloud.aeronaves.model.response

import com.google.gson.annotations.SerializedName

class ObtieneModelosAeronaveCloudResponse (
    @SerializedName("success") var success: Int,
    @SerializedName("data") val data: ObtieneModelosAeronaveDataCloudResponse?,
    @SerializedName("message") var message: String,
)
{
    constructor() : this(
        0,
        null,
        ""
    )
}