package com.helisur.helisurapp.data.cloud.formatos.model.parameter

import com.google.gson.annotations.SerializedName

class GuardaTareaCloudParameter (
    @SerializedName("codigoRegistroFormato") var codigoRegistroFormato : String,
    @SerializedName("codigoTarea") var codigoTarea : String,
    @SerializedName("codigoReportaje") var codigoReportaje : String,
    @SerializedName("indicadorSN") var indicadorSN : String,
    @SerializedName("usuarioRegistro") var usuarioRegistro : String,
    @SerializedName("motivoReportaje") var motivoReportaje : String,
    var nombreTarea:String,
    //var nombreReportaje:String
)
