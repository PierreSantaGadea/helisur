package com.helisur.helisurapp.data.cloud.formatos.model.parameter

import com.google.gson.annotations.SerializedName

class ActualizaReportajeFormatoCloudParameter (
    @SerializedName("codigoRegistroFormato") var codigoRegistroFormato: String,
    @SerializedName("numeroRTV") var numeroRTV: String,
    @SerializedName("codigoEstacion") var codigoEstacion: String,
    @SerializedName("listaDetalleFormato") var listaDetalleFormato: List<ActualizaReportajeFormatoDetalleCloudParameter>
)
