package com.helisur.helisurapp.domain.model

import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneEstacionesDataTableCloudResponse
import com.helisur.helisurapp.data.database.entities.EstacionEntity
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity

class Estacion(
    var id_cloud: String?,
    var nombre: String?,
    var siglas: String?,
    var fechaRegistro:String?,
    var fechaModificacion:String?
) {
    constructor() : this(
         "", "","","",""
    )
}

//entidadDB pasa modelo
fun EstacionEntity.toDomain() = Estacion(
     id_cloud, nombre,siglas,fechaRegistro,fechaModificacion
)

//entidadCLOUD pasa a modelo
fun ObtieneEstacionesDataTableCloudResponse.toDomain() = Estacion(
     id, nombre,siglas,fechaRegistro,fechaModificacion
)







