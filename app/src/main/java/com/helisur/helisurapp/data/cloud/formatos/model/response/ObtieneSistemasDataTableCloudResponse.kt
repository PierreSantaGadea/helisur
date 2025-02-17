package com.helisur.helisurapp.data.cloud.formatos.model.response

import com.google.gson.annotations.SerializedName

class ObtieneSistemasDataTableCloudResponse (
    @SerializedName("codigoFormato") var codigoFormato: String,
    @SerializedName("codigoSistema") var codigoSistema: String,
    @SerializedName("nombrePosicion") var nombrePosicion: String,
    @SerializedName("fechaRegistro") var fechaRegistro: String,
    @SerializedName("fechaModificacion") var fechaModificacion: String
)
