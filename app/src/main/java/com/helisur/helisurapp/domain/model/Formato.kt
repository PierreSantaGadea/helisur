package com.helisur.helisurapp.domain.model

import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosDataTableCloudResponse
import com.helisur.helisurapp.data.database.entities.FormatoEntity
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity
import java.text.Normalizer.Form

class Formato(
    var id_cloud: String?,
    var codigoModeloAeronave: String?,
    var nombreFormato: String?,
    var fechaRegistro:String?,
    var fechaModificacion:String?
) {
    constructor() : this(
         "", "","","",""
    )
}

//entidadDB pasa modelo
fun FormatoEntity.toDomain() = Formato(
     id_cloud, codigoModeloAeronave,nombreFormato,fechaRegistro,fechaModificacion
)

//entidadCLOUD pasa a modelo
fun ObtieneFormatosDataTableCloudResponse.toDomain() = Formato(
     id_cloud = codigoFormato, codigoModeloAeronave = codigoModeloAeronave, nombreFormato = nombreFormato, fechaRegistro = fechaRegistro, fechaModificacion = fechaModificacion
)







