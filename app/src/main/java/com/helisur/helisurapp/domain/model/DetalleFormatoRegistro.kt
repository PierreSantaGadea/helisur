package com.helisur.helisurapp.domain.model

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveDataTableCloudResponse
import com.helisur.helisurapp.data.database.entities.AeronaveEntity
import com.helisur.helisurapp.data.database.entities.DetalleFormatoRegistroEntity
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity

class DetalleFormatoRegistro(
    var id_cloud: String?,
    var idRegistroFormatoDB: String,
    var codigoRegistroFormato: String,
    var codigoTarea: String,
    var nombreTarea: String,
    var codigoReportaje: String,
    var nombreReportaje: String,
    var indicadorSN: String,
    var indicadorBloqueo: String,
    val fechaRegistro: String?,
    val fechaModificacion: String?,
) {
    constructor() : this(
         "","","","","","","",
        "","","",""
    )
}



//entidadDB pasa modelo
fun DetalleFormatoRegistroEntity.toDomain() = DetalleFormatoRegistro(
     id_cloud,idRegistroFormatoDB!!, codigoRegistroFormato, codigoTarea, nombreTarea, codigoReportaje, nombreReportaje, indicadorSN, indicadorBloqueo, fechaRegistro, fechaModificacion
)

/*
//entidadCLOUD pasa a modelo
fun ObtieneModelosAeronaveDataTableCloudResponse.toDomain() = Aeronave(
     codigoModeloPuesto, codigoPuestoTecnico,codigoCliente,nombre,placa,comentario,html,fechaRegistro,fechaModificacion
)


 */






