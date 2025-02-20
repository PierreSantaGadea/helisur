package com.helisur.helisurapp.domain.model

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveDataTableCloudResponse
import com.helisur.helisurapp.data.database.entities.AeronaveEntity
import com.helisur.helisurapp.data.database.entities.FormatoRegistroEntity
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity

class FormatoRegistro(
    var id_db: String?,
    var id_cloud: String?,
    var codigoFormato : String,
    var nombreAeronave: String,
    var codigoPuestoTecnico : String,
    var numeroRTV : String,
    var codigoEstacion : String,
    var existenDiscrepancias : String,
    var numeroRTVDiscrepancias : String,
    var accionesMantenimiento : String,
    var solicitaEncMotores: String,
    var idEmpleadoResponsable : String,
    var urlFirmaResponsable : String,
    var idEmpleadoPiloto : String,
    var urlFirmaPiloto : String,
    var idEmpleadoCoPiloto : String,
    var urlFirmaCoPiloto : String,
    var fechaHoraInicioRegistro : String,
    var fechaHoraFinRegistro : String,
    var usuarioRegistro : String,
    val fechaRegistro: String?,
    val fechaModificacion: String?,
) {
    constructor() : this(
         "","","","","","","","",
        "","","","","","","",
        "","","","","","",""
    )
}



//entidadDB pasa modelo
fun FormatoRegistroEntity.toDomain() = FormatoRegistro(
    id_db, id_cloud, codigoFormato,nombreAeronave, codigoPuestoTecnico, numeroRTV, codigoEstacion, existenDiscrepancias, numeroRTVDiscrepancias, accionesMantenimiento,
    solicitaEncMotores, idEmpleadoResponsable, urlFirmaResponsable, idEmpleadoPiloto, urlFirmaPiloto, idEmpleadoCoPiloto, urlFirmaCoPiloto, fechaHoraInicioRegistro,
    fechaHoraFinRegistro, usuarioRegistro, fechaRegistro, fechaModificacion
)

/*
//entidadCLOUD pasa a modelo
fun ObtieneModelosAeronaveDataTableCloudResponse.toDomain() = Aeronave(
     codigoModeloPuesto, codigoPuestoTecnico,codigoCliente,nombre,placa,comentario,html,fechaRegistro,fechaModificacion
)


 */






