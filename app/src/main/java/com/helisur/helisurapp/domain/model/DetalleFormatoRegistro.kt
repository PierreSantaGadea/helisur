package com.helisur.helisurapp.domain.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveDataTableCloudResponse
import com.helisur.helisurapp.data.database.entities.AeronaveEntity
import com.helisur.helisurapp.data.database.entities.DetalleFormatoRegistroEntity
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity

class DetalleFormatoRegistro(
    var id_db: String?,
    var id_cloud: String?,
    var idRegistroFormatoDB: String,
    var codigoRegistroFormato: String,
    var codigoTarea: String,
    var nombreTarea: String,
    var nombreSistema: String,
    var codigoReportaje: String,
    var nombreReportaje: String,
    var motivoReportaje: String,
    var indicadorSN: String,
    var indicadorBloqueo: String,
    val fechaRegistro: String?,
    val fechaModificacion: String?,
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString()
    ) {
    }

    constructor() : this(
         "","","","","","","","","",
        "","","","",""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id_db)
        parcel.writeString(id_cloud)
        parcel.writeString(idRegistroFormatoDB)
        parcel.writeString(codigoRegistroFormato)
        parcel.writeString(codigoTarea)
        parcel.writeString(nombreTarea)
        parcel.writeString(nombreSistema)
        parcel.writeString(codigoReportaje)
        parcel.writeString(nombreReportaje)
        parcel.writeString(motivoReportaje)
        parcel.writeString(indicadorSN)
        parcel.writeString(indicadorBloqueo)
        parcel.writeString(fechaRegistro)
        parcel.writeString(fechaModificacion)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DetalleFormatoRegistro> {
        override fun createFromParcel(parcel: Parcel): DetalleFormatoRegistro {
            return DetalleFormatoRegistro(parcel)
        }

        override fun newArray(size: Int): Array<DetalleFormatoRegistro?> {
            return arrayOfNulls(size)
        }
    }
}


//entidadDB pasa modelo
fun DetalleFormatoRegistroEntity.toDomain() = DetalleFormatoRegistro(
    id_db, id_cloud,idRegistroFormatoDB!!, codigoRegistroFormato, codigoTarea, nombreTarea, nombreSistema,codigoReportaje, nombreReportaje,motivoReportaje, indicadorSN, indicadorBloqueo, fechaRegistro, fechaModificacion
)

/*
//entidadCLOUD pasa a modelo
fun ObtieneModelosAeronaveDataTableCloudResponse.toDomain() = Aeronave(
     codigoModeloPuesto, codigoPuestoTecnico,codigoCliente,nombre,placa,comentario,html,fechaRegistro,fechaModificacion
)


 */






