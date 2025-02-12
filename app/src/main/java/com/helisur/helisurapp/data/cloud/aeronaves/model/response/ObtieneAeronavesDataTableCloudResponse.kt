package com.helisur.helisurapp.data.cloud.aeronaves.model.response

import com.google.gson.annotations.SerializedName
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity
import com.helisur.helisurapp.domain.model.ModeloAeronave

class ObtieneAeronavesDataTableCloudResponse (
    @SerializedName("codigoModeloPuesto") var codigoModeloPuesto: String,
    @SerializedName("codigoTipoPuesto") var codigoTipoPuesto: String,
    @SerializedName("descripcion") var descripcion: String,
    @SerializedName("fechaRegistro") var fechaRegistro: String? ,
    @SerializedName("fechaModificacion") var fechaModificacion: String?
)

fun ModeloAeronaveEntity.toDB() = ObtieneAeronavesDataTableCloudResponse(codigoModeloPuesto = id_cloud!!,"" , descripcion = nombre!!, fechaRegistro = fechaRegistro, fechaModificacion = fechaModificacion)
