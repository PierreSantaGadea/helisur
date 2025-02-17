package com.helisur.helisurapp.data.cloud.formatos.model.response

import com.google.gson.annotations.SerializedName

class ObtieneFormatosDataTableCloudResponse (
    @SerializedName("codigoFormato") var codigoFormato: String,
    @SerializedName("codigoModeloAeronave") var codigoModeloAeronave: String,
    @SerializedName("nombreFormato") var nombreFormato: String,
    @SerializedName("fechaRegistro") var fechaRegistro: String,
    @SerializedName("fechaModificacion") var fechaModificacion: String
)
