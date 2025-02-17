package com.helisur.helisurapp.domain.model

import com.google.gson.annotations.SerializedName
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveDataTableCloudResponse
import com.helisur.helisurapp.data.database.entities.AeronaveEntity
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity

class Aeronave(
    var id_cloud: String?,
    var codigoPuestoTecnico: String,
    var codigoCliente: String,
    var nombre: String,
    var placa: String,
    var comentario: String,
    var html: String,
    var fechaRegistro:String?,
    var fechaModificacion:String?
) {
    constructor() : this(
         "","","","","","","","",""
    )
}

//entidadDB pasa modelo
fun AeronaveEntity.toDomain() = Aeronave(
     id_cloud, codigoPuestoTecnico!!,codigoCliente!!,nombre!!,placa!!,comentario!!,html!!,fechaRegistro,fechaModificacion
)

//entidadCLOUD pasa a modelo
fun ObtieneModelosAeronaveDataTableCloudResponse.toDomain() = Aeronave(
     codigoModeloPuesto, codigoPuestoTecnico,codigoCliente,nombre,placa,comentario,html,fechaRegistro,fechaModificacion
)







