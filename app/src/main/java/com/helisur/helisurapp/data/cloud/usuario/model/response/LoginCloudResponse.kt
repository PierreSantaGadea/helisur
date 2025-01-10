package com.helisur.helisurapp.data.cloud.usuario.model.response

import com.google.gson.annotations.SerializedName

data class LoginCloudResponse (
    @SerializedName("error") val error: Boolean,
    @SerializedName("messageSystem") val messageSystem: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: LoginCloudResponseData
)