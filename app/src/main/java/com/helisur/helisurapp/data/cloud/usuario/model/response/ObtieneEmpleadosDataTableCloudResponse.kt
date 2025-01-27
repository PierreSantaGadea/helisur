package com.helisur.helisurapp.data.cloud.usuario.model.response

import com.google.gson.annotations.SerializedName

class ObtieneEmpleadosDataTableCloudResponse (

    @SerializedName("codigoEmpleado") var codigoEmpleado: String,
    @SerializedName("codigoArea") var codigoArea: String,
    @SerializedName("numeroDocumento") var numeroDocumento : String,
    @SerializedName("nombre") var nombre: String,
    @SerializedName("apellidoPaterno") var apellidoPaterno: String,
    @SerializedName("apellidoMaterno") var apellidoMaterno: String,
    @SerializedName("nombreCompleto") var nombreCompleto: String,
    @SerializedName("email") var email: String,
    @SerializedName("estado") var estado: String,
    @SerializedName("cargo") var cargo: String,
    @SerializedName("fechaIngreso") var fechaIngreso: String,
    @SerializedName("licencia") var licencia: String
)
