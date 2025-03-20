package com.helisur.helisurapp.data.cloud.formatos.model.response

import com.google.gson.annotations.SerializedName
import com.helisur.helisurapp.domain.util.Constants

class EnviaPdfCloudResponse (
    @SerializedName("error") var error: Int,
    @SerializedName("path") var path: String?,
    @SerializedName("message") var message: String,
)
{
    constructor() : this(
        Constants.RESPONSE_CODE.SUCCESS,
        "",
        ""
    )
}