package com.helisur.helisurapp.data.cloud.gestionotos.model.response

import com.google.gson.annotations.SerializedName

class ObtieneOTOSDataTableCloudResponse (
    @SerializedName("codigoOrden") var codigoOrden: String, //codigoOT
    @SerializedName("rubro") var rubro: String,
    @SerializedName("codigoTipoOrden") var codigoTipoOrden: String,
    @SerializedName("descripcionTipoOrden") var descripcionTipoOrden: String,
    @SerializedName("descripcionTipoTrabajo") var descripcionTipoTrabajo: String,
    @SerializedName("descripcionIntervencion") var descripcionIntervencion: String,
    @SerializedName("descripcionTrabajo") var descripcionTrabajo: String,
    @SerializedName("categoria") var categoria: String,
    @SerializedName("modeloparte") var modeloparte: String,
    @SerializedName("matriculanomenclatura") var matriculanomenclatura: String,
    @SerializedName("codigoSituacion") var codigoSituacion: String, //estado
    @SerializedName("descripcionSituacion") var descripcionSituacion: String,
    @SerializedName("iconoSituacion") var iconoSituacion: String,
    @SerializedName("codigoTrabajo") var codigoTrabajo: String,
    @SerializedName("numeroOrden") var numeroOrden: String, //numeroOT
    @SerializedName("tituloOrden") var tituloOrden: String,
    @SerializedName("codigoCliente") var codigoCliente: String,
    @SerializedName("codigoPuestoTecnico") var codigoPuestoTecnico: String,
    @SerializedName("codigoEquipo") var codigoEquipo: String,
    @SerializedName("fechaCreacion") var fechaCreacion: String, // fecha Creacion
    @SerializedName("fechaEstimadaInicio") var fechaEstimadaInicio: String,
    @SerializedName("fechaInicioReal") var fechaInicioReal: String,
    @SerializedName("fechatermino") var fechatermino: String,
    @SerializedName("duracionEstimada") var duracionEstimada: Int,
    @SerializedName("fechaCierreReal") var fechaCierreReal: String,
    @SerializedName("fechaCierreSistema") var fechaCierreSistema: String,
    @SerializedName("codigoPuestoTrabajo") var codigoPuestoTrabajo: String,
    @SerializedName("codigoOTPadre") var codigoOTPadre: String,
    @SerializedName("codigoAreaEmisor") var codigoAreaEmisor: String,
    @SerializedName("codigoAreaDirigido") var codigoAreaDirigido: String,
    @SerializedName("areaEmisor") var areaEmisor: String,
    @SerializedName("comentario") var comentario: String,
    @SerializedName("limiteTermino") var limiteTermino: String,
    @SerializedName("trabajoITV") var trabajoITV: String,
    @SerializedName("usuarioRegistro") var usuarioRegistro: String,
    @SerializedName("fechaRegistro") var fechaRegistro: String,
    @SerializedName("usuarioModificacion") var usuarioModificacion: String,
    @SerializedName("fechaModificacion") var fechaModificacion: String,
    @SerializedName("ordenSit") var ordenSit: Int,
    @SerializedName("nombreEstacion") var nombreEstacion: String

)
{

}