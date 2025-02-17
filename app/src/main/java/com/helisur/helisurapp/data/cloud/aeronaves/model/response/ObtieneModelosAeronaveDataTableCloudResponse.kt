package com.helisur.helisurapp.data.cloud.aeronaves.model.response

import com.google.gson.annotations.SerializedName

class ObtieneModelosAeronaveDataTableCloudResponse (
    @SerializedName("codigoPuestoTecnico") var codigoPuestoTecnico: String,
    @SerializedName("codigoModeloPuesto") var codigoModeloPuesto: String,
    @SerializedName("codigoCliente") var codigoCliente: String,
    @SerializedName("nombre") var nombre: String,
    @SerializedName("placa") var placa: String,
    @SerializedName("comentario") var comentario: String,
    @SerializedName("html") var html: String,
    @SerializedName("fechaRegistro") var fechaRegistro: String,
    @SerializedName("fechaModificacion") var fechaModificacion: String
)
