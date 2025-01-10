package com.helisur.helisurapp.data.cloud.usuario.model.response

import com.google.gson.annotations.SerializedName

data class LoginCloudResponseData (
    @SerializedName("changePassword") val changePassword: Boolean?,
    @SerializedName("token") val token: String?,
    @SerializedName("nombres") val nombres: String?,
    @SerializedName("apellidoPaterno") val apellidoPaterno: String?,
    @SerializedName("apellidoMaterno") val apellidoMaterno: String?,
    @SerializedName("rol") val rol: String?,
    @SerializedName("companiaNombre") val companiaNombre: String?,
    @SerializedName("razonSocial") val razonSocial: String?,
    @SerializedName("usuario") val usuario: String?,
    @SerializedName("perfilId") val perfilId: String?,
    @SerializedName("controladorPrincipal") val controladorPrincipal: String?,
    @SerializedName("isAdmin") val isAdmin: Boolean?,
    @SerializedName("vistaPrincipal") val vistaPrincipal: String?,
    @SerializedName("compania") val compania: String?,
    @SerializedName("ruta") val ruta: String?,
    @SerializedName("perfil") val perfil: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("opciones") val opciones: List<LoginCloudResponseDataOpciones>?,
    @SerializedName("linea") val linea: String?
)