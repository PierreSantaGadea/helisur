package com.helisur.helisurapp.data.cloud.hojaruta.model.response

import com.google.gson.annotations.SerializedName

class ObtieneHojasRutaDataTableCloudResponse (
    @SerializedName("id") var id: String,
    @SerializedName("codigoCliente") var codigoCliente: String,
    @SerializedName("codigoModeloPuesto") var codigoModeloPuesto: String,
    @SerializedName("modeloAeronave") var modeloAeronave: String,
    @SerializedName("codigoEstacion") var codigoEstacion: String,
    @SerializedName("nombreEstacion") var nombreEstacion: String,
    @SerializedName("codigoTareaInspeccion") var codigoTareaInspeccion: String,
    @SerializedName("numeroRequerimientoMnto") var numeroRequerimientoMnto: String,
    @SerializedName("codigoPuestoTecnico") var codigoPuestoTecnico: String,
    @SerializedName("aeronave") var aeronave: String,
    @SerializedName("tsnPlazo") var tsnPlazo: String,
    @SerializedName("fechaPlazo") var fechaPlazo: String,
    @SerializedName("porcentajeAvance") var porcentajeAvance: Double,
    @SerializedName("recomendaciones") var recomendaciones: String,
    @SerializedName("usuarioRegistro") var usuarioRegistro: String,
    @SerializedName("fechaRegistro") var fechaRegistro: String,
    @SerializedName("usuarioModificacion") var usuarioModificacion: String,
    @SerializedName("fechaModificacion") var fechaModificacion: String

)
