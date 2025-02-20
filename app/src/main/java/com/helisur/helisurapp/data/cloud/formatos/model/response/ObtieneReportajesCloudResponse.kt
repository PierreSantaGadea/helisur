package com.helisur.helisurapp.data.cloud.formatos.model.response

import com.google.gson.annotations.SerializedName

class ObtieneReportajesCloudResponse (
    @SerializedName("success") var success: Int,
    @SerializedName("data") val data: ObtieneReportajesDataCloudResponse?,
    @SerializedName("message") var message: String,
)
{
    constructor() : this(
        0,
        null,
        ""
    )
}