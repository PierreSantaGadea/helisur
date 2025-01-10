package com.helisur.helisurapp.data.cloud.usuario.model.parameter

import com.google.gson.annotations.SerializedName

class LoginCloudParameter (
    @SerializedName("user") val user: String,
    @SerializedName("password") val password: String
)