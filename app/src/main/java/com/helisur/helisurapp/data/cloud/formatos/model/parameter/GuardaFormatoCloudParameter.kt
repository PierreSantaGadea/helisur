package com.helisur.helisurapp.data.cloud.formatos.model.parameter

import com.google.gson.annotations.SerializedName

class GuardaFormatoCloudParameter (
    @SerializedName("codigoFormato") var codigoFormato : String,
    @SerializedName("codigoPuestoTecnico") var codigoPuestoTecnico : String,
    @SerializedName("numeroRTV") var numeroRTV : String,
    @SerializedName("codigoEstacion") var codigoEstacion : String,
    @SerializedName("existenDiscrepancias") var existenDiscrepancias : String,
    @SerializedName("numeroRTVDiscrepancias") var numeroRTVDiscrepancias : String,
    @SerializedName("accionesMantenimiento") var accionesMantenimiento : String,
    @SerializedName("solicitaEncMotores") var solicitaEncMotores: String,
    @SerializedName("idEmpleadoResponsable") var idEmpleadoResponsable : String,
    @SerializedName("urlFirmaResponsable") var urlFirmaResponsable : String,
    @SerializedName("idEmpleadoPiloto") var idEmpleadoPiloto : String,
    @SerializedName("urlFirmaPiloto") var urlFirmaPiloto : String,
    @SerializedName("idEmpleadoCoPiloto") var idEmpleadoCoPiloto : String,
    @SerializedName("urlFirmaCoPiloto") var urlFirmaCoPiloto : String,
    @SerializedName("fechaHoraInicioRegistro") var fechaHoraInicioRegistro : String,
    @SerializedName("fechaHoraFinRegistro") var fechaHoraFinRegistro : String,
    @SerializedName("usuarioRegistro") var usuarioRegistro : String,
    @SerializedName("listaDetalleFormato") var listaTareas:List<GuardaTareaCloudParameter>?
)

{
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        null
    )
}
