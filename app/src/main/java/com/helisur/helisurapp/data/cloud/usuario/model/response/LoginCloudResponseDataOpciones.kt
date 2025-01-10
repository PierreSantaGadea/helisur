package com.helisur.helisurapp.data.cloud.usuario.model.response

import com.google.gson.annotations.SerializedName

data class LoginCloudResponseDataOpciones (
    @SerializedName("id") val id: String,
    @SerializedName("orden") val orden: Int,
    @SerializedName("controlador") val controlador: String,
    @SerializedName("vista") val vista: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("icono") val icono: String,
    @SerializedName("esBoton") val esBoton: Boolean,
    @SerializedName("agrupacion") val agrupacion: String
)