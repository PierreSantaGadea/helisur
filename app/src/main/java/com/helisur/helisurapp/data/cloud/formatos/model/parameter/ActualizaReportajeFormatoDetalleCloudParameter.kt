package com.helisur.helisurapp.data.cloud.formatos.model.parameter

import com.google.gson.annotations.SerializedName

class ActualizaReportajeFormatoDetalleCloudParameter (
    @SerializedName("codigoTarea") var codigoTarea: String,
    @SerializedName("codigoReportaje") var codigoReportaje: String,
    @SerializedName("indicadorSN") var indicadorSN: String,
    @SerializedName("motivoReportaje") var motivoReportaje: String,
    @SerializedName("usuarioRegistro") var usuarioRegistro: String,
)
