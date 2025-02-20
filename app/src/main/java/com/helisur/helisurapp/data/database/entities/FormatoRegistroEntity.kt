package com.helisur.helisurapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosDataTableCloudResponse
import com.helisur.helisurapp.domain.model.Formato
import com.helisur.helisurapp.domain.model.FormatoRegistro
import com.helisur.helisurapp.domain.model.ModeloAeronave

@Entity(tableName = "FormatoRegistro")
class FormatoRegistroEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "id_db") val id_db: String?,
    @ColumnInfo(name = "id_cloud") val id_cloud: String?,

    @ColumnInfo(name = "codigoFormato") var codigoFormato : String,
    @ColumnInfo(name = "nombreAeronave") var nombreAeronave : String,
    @ColumnInfo(name = "codigoPuestoTecnico") var codigoPuestoTecnico : String,
    @ColumnInfo(name = "numeroRTV") var numeroRTV : String,
    @ColumnInfo(name = "codigoEstacion") var codigoEstacion : String,
    @ColumnInfo(name = "existenDiscrepancias") var existenDiscrepancias : String,
    @ColumnInfo(name = "numeroRTVDiscrepancias") var numeroRTVDiscrepancias : String,
    @ColumnInfo(name = "accionesMantenimiento") var accionesMantenimiento : String,
    @ColumnInfo(name = "solicitaEncMotores") var solicitaEncMotores: String,
    @ColumnInfo(name = "idEmpleadoResponsable") var idEmpleadoResponsable : String,
    @ColumnInfo(name = "urlFirmaResponsable") var urlFirmaResponsable : String,
    @ColumnInfo(name = "idEmpleadoPiloto") var idEmpleadoPiloto : String,
    @ColumnInfo(name = "urlFirmaPiloto") var urlFirmaPiloto : String,
    @ColumnInfo(name = "idEmpleadoCoPiloto") var idEmpleadoCoPiloto : String,
    @ColumnInfo(name = "urlFirmaCoPiloto") var urlFirmaCoPiloto : String,
    @ColumnInfo(name = "fechaHoraInicioRegistro") var fechaHoraInicioRegistro : String,
    @ColumnInfo(name = "fechaHoraFinRegistro") var fechaHoraFinRegistro : String,
    @ColumnInfo(name = "usuarioRegistro") var usuarioRegistro : String,

    @ColumnInfo(name = "sync") val sync: Boolean?,
    @ColumnInfo(name = "fechaRegistro") val fechaRegistro: String?,
    @ColumnInfo(name = "fechaModificacion") val fechaModificacion: String?,

    )

fun FormatoRegistro.toDB() = FormatoRegistroEntity(id_db = id_db, id_cloud = id_cloud, codigoFormato = codigoFormato, nombreAeronave = nombreAeronave, codigoPuestoTecnico = codigoPuestoTecnico,numeroRTV = numeroRTV,
    codigoEstacion = codigoEstacion, existenDiscrepancias = existenDiscrepancias, numeroRTVDiscrepancias = numeroRTVDiscrepancias,
    accionesMantenimiento = accionesMantenimiento, solicitaEncMotores = solicitaEncMotores,
    idEmpleadoResponsable = idEmpleadoResponsable, urlFirmaResponsable = urlFirmaResponsable, idEmpleadoPiloto = idEmpleadoPiloto,
    urlFirmaPiloto = urlFirmaPiloto, idEmpleadoCoPiloto = idEmpleadoCoPiloto, urlFirmaCoPiloto = urlFirmaCoPiloto,
    fechaHoraInicioRegistro = fechaHoraInicioRegistro, fechaHoraFinRegistro = fechaHoraFinRegistro, usuarioRegistro = usuarioRegistro,
    sync = true,fechaRegistro = fechaRegistro, fechaModificacion = fechaModificacion)


/*
fun ObtieneFormatosDataTableCloudResponse.toDB() = FormatoEntity(id_cloud = codigoFormato, codigoModeloAeronave = codigoModeloAeronave, nombreFormato = nombreFormato, sync = true, fechaRegistro = fechaRegistro, fechaModificacion = fechaModificacion)


 */

