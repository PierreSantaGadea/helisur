package com.helisur.helisurapp.domain.model

import com.google.gson.annotations.SerializedName
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneReportajesDataTableCloudResponse
import com.helisur.helisurapp.data.database.entities.AeronaveEntity
import com.helisur.helisurapp.data.database.entities.ReportajeEntity

class Reportaje(

    var id_cloud: String?,
    var codigoTarea: String,
    var nombreReportaje: String,
    var bloqueoFormato: String,
    var fechaRegistro:String?,
    var fechaModificacion:String?

) {
    constructor() : this(
      "","","","","", ""
    )
}



//entidadDB pasa modelo
fun ReportajeEntity.toDomain() = Reportaje(
    id_cloud, codigoTarea!!,nombreReportaje!!,bloqueoFormato!!,fechaRegistro,fechaModificacion
)

//entidadCLOUD pasa a modelo
fun ObtieneReportajesDataTableCloudResponse.toDomain() = Reportaje(
    id_cloud = codigoReportaje,codigoTarea = codigoTarea,nombreReportaje = nombreReportaje,bloqueoFormato = bloqueoFormato,fechaRegistro = fechaRegistro,fechaModificacion = fechaModificacion
)


