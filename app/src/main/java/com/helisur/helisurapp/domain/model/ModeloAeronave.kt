package com.helisur.helisurapp.domain.model

import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity

class ModeloAeronave(
    var id_cloud: String?,
    var nombre: String?,
    var fechaRegistro:String?,
    var fechaModificacion:String?
) {
    constructor() : this(
         "", "","",""
    )
}

//entidadDB pasa modelo
fun ModeloAeronaveEntity.toDomain() = ModeloAeronave(
     id_cloud, nombre,fechaRegistro,fechaModificacion
)

//entidadCLOUD pasa a modelo
fun ObtieneAeronavesDataTableCloudResponse.toDomain() = ModeloAeronave(
     codigoModeloPuesto, descripcion,fechaRegistro,fechaModificacion
)







