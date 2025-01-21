package com.helisur.helisurapp.data.cloud.usuario.model.response

import com.google.gson.annotations.SerializedName

class ObtieneDatosUsuarioDataTableCloudResponse (

    @SerializedName("codigoUsuario") var codigoUsuario: String,
    @SerializedName("codigoEmpleado") var codigoEmpleado: String,
    @SerializedName("codigoPerfil") var codigoPerfil: String,
    @SerializedName("nombreUsuario") var nombreUsuario: String,
    @SerializedName("apellidoPaterno") var apellidoPaterno: String,
    @SerializedName("apellidoMaterno") var apellidoMaterno: String,
    @SerializedName("usuarioAcceso") var usuarioAcceso: String,
    @SerializedName("claveAcceso") var claveAcceso: String,
    @SerializedName("indicadorExterno") var indicadorExterno: String,
    @SerializedName("codigoEstacion") var codigoEstacion: String,
    @SerializedName("nombreEstacion") var nombreEstacion: String,
    @SerializedName("cargo") var cargo: String,
    @SerializedName("dni") var dni: String,
    @SerializedName("superior") var superior: String,
    @SerializedName("codigoArea") var codigoArea: String,
    @SerializedName("nombreArea") var nombreArea: String,
    @SerializedName("ubigeoDireccion") var ubigeoDireccion: String,
    @SerializedName("direccion") var direccion: String,
    @SerializedName("referencia") var referencia: String,
    @SerializedName("codUsuarioReq") var codUsuarioReq: String
)
