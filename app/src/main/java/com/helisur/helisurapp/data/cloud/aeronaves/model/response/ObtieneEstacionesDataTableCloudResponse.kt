package com.helisur.helisurapp.data.cloud.aeronaves.model.response

import com.google.gson.annotations.SerializedName
import com.helisur.helisurapp.data.database.entities.EstacionEntity
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity

class ObtieneEstacionesDataTableCloudResponse (
    @SerializedName("id") var id: String,
    @SerializedName("nombre") var nombre: String,
    @SerializedName("siglas") var siglas: String,
    @SerializedName("fechaRegistro") var fechaRegistro: String,
    @SerializedName("fechaModificacion") var fechaModificacion: String,
)

fun EstacionEntity.toDB() = ObtieneEstacionesDataTableCloudResponse(id = id_cloud!!, nombre = nombre!!, siglas = siglas!!, fechaRegistro = fechaRegistro!!, fechaModificacion = fechaModificacion!!)

