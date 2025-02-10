package com.helisur.helisurapp.data.cloud.aeronaves.model.response

import com.google.gson.annotations.SerializedName
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity
import com.helisur.helisurapp.domain.model.ModeloAeronave

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