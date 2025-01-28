package com.helisur.helisurapp.data.cloud.formatos.model.response

import com.google.gson.annotations.SerializedName

class ObtieneFormatosRealizadosDataTableCloudResponse (
    @SerializedName("id") var id: String,
    @SerializedName("codigoFormato") var codigoFormato: String,
    @SerializedName("nombreFormato") var nombreFormato: String,
    @SerializedName("codigoPuestoTecnico") var codigoPuestoTecnico: String,
    @SerializedName("aeronave") var aeronave: String,
    @SerializedName("modeloAeronave") var modeloAeronave: String,
    @SerializedName("numeroRTV") var numeroRTV: String,
    @SerializedName("codigoEstacion") var codigoEstacion: String,
    @SerializedName("existenDiscrepancias") var existenDiscrepancias: String,
    @SerializedName("numeroRTVDiscrepancias") var numeroRTVDiscrepancias: String,
    @SerializedName("accionesMantenimiento") var accionesMantenimiento: String,
    @SerializedName("solicitaEncMotores") var solicitaEncMotores: String,
    @SerializedName("fechaHoraInicioRegistro") var fechaHoraInicioRegistro: String,
    @SerializedName("fechaHoraFinRegistro") var fechaHoraFinRegistro: String,
    @SerializedName("tiempoRegistro") var tiempoRegistro: String

)
