package com.helisur.helisurapp.data.cloud.formatos.model.response

import com.google.gson.annotations.SerializedName

class ObtieneReportajesFormatoDataTableCloudResponse (
    @SerializedName("codigoRegistroFormato") var codigoRegistroFormato: String,
    @SerializedName("codigoTarea") var codigoTarea: String,
    @SerializedName("nombreTarea") var nombreTarea: String,
    @SerializedName("codigoReportaje") var codigoReportaje: String,
    @SerializedName("nombreReportaje") var nombreReportaje: String,
    @SerializedName("indicadorSN") var indicadorSN: String,
    @SerializedName("indicadorBloqueo") var indicadorBloqueo: String,
)
