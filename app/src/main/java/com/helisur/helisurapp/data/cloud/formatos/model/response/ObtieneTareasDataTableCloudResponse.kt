package com.helisur.helisurapp.data.cloud.formatos.model.response

import com.google.gson.annotations.SerializedName

class ObtieneTareasDataTableCloudResponse (
    @SerializedName("codigoTarea") var codigoTarea: String,
    @SerializedName("codigoSistema") var codigoSistema: String,
    @SerializedName("nombreTarea") var nombreTarea: String
)
