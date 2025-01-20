package com.helisur.helisurapp.data.cloud.usuario.model.response

import com.google.gson.annotations.SerializedName

data class LoginCloudResponse (
    @SerializedName("error") val Int: Boolean,
    @SerializedName("token") val messageSystem: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: LoginCloudResponseData
)