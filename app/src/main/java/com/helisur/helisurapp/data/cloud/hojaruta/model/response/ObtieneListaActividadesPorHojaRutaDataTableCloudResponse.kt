package com.helisur.helisurapp.data.cloud.hojaruta.model.response

import com.google.gson.annotations.SerializedName

class ObtieneListaActividadesPorHojaRutaDataTableCloudResponse (
    @SerializedName("id") var id: String,
    @SerializedName("codigoFase") var codigoFase: String,
    @SerializedName("nombreFase") var nombreFase: String,
    @SerializedName("codigoGrupo") var codigoGrupo: String,
    @SerializedName("nombreGrupo") var nombreGrupo: String,
    @SerializedName("codigoActividad") var codigoActividad: String,
    @SerializedName("nombreActividad") var nombreActividad: String,
    @SerializedName("comentario") var comentario: String,
    @SerializedName("codigoResponsable") var codigoResponsable: String,
    @SerializedName("fechaSeleccion") var fechaSeleccion: String,
    @SerializedName("indicadorCumplimiento") var indicadorCumplimiento: String,
    @SerializedName("indicadorHabilitado") var indicadorHabilitado: String,

)
