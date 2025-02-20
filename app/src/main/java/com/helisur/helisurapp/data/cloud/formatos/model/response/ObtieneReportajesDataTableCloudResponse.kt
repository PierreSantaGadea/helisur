package com.helisur.helisurapp.data.cloud.formatos.model.response

import com.google.gson.annotations.SerializedName

class ObtieneReportajesDataTableCloudResponse (
    @SerializedName("codigoReportaje") var codigoReportaje: String,
    @SerializedName("codigoTarea") var codigoTarea: String,
    @SerializedName("nombreReportaje") var nombreReportaje: String,
    @SerializedName("bloqueoFormato") var bloqueoFormato: String,
    @SerializedName("fechaRegistro") var fechaRegistro: String,
    @SerializedName("fechaModificacion") var fechaModificacion: String
)
