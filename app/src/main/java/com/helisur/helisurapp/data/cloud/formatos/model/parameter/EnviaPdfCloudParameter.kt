package com.helisur.helisurapp.data.cloud.formatos.model.parameter

import com.google.gson.annotations.SerializedName

class EnviaPdfCloudParameter (
    @SerializedName("FileName") var fileName: String,
    @SerializedName("Base64Data") var base64Data: String

    )