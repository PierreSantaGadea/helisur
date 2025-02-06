package com.helisur.helisurapp.data.cloud.formatos.model.response

import com.google.gson.annotations.SerializedName

class ObtieneReportajesFormatoCloudResponse (
    @SerializedName("success") var success: Int,
    @SerializedName("data") val data: ObtieneReportajesFormatoDataCloudResponse?,
    @SerializedName("message") var message: String,
)
{
    constructor() : this(
        0,
        null,
        ""
    )
}