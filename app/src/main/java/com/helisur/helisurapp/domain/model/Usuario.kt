package com.helisur.helisurapp.domain.model

import com.helisur.helisurapp.data.cloud.usuario.model.response.LoginCloudResponse
import com.helisur.helisurapp.data.cloud.usuario.model.response.LoginCloudResponseDataOpciones
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneDatosUsuarioCloudResponse
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneDatosUsuarioDataCloudResponse
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneTokenCloudResponse

class Usuario(

    var success:Int?,
    var message:String?,
    var codigoUsuario: String?,
    var codigoEmpleado: String?,
    var codigoPerfil: String?,
    var nombreUsuario: String?,
    var apellidoPaterno: String?,
    var apellidoMaterno: String?,
    var usuarioAcceso: String?,
    var claveAcceso: String?,
    var indicadorExterno: String?,
    var codigoEstacion: String?,
    var nombreEstacion: String?,
    var cargo: String?,
    var dni: String?,
    var superior: String?,
    var codigoArea: String?,
    var nombreArea: String?,
    var ubigeoDireccion: String?,
    var direccion: String?,
    var referencia: String?,
    var codUsuarioReq: String?,

) {
    constructor() : this(
        0,
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )
}


fun ObtieneDatosUsuarioCloudResponse.toDomain() = Usuario(
    success,
    message,
    data!!.table.get(0).codigoUsuario,
    data!!.table.get(0).codigoEmpleado,
    data!!.table.get(0).codigoPerfil,
    data!!.table.get(0).nombreUsuario,
    data!!.table.get(0).apellidoPaterno,
    data!!.table.get(0).apellidoMaterno,
    data!!.table.get(0).usuarioAcceso,
    data!!.table.get(0).claveAcceso,
    data!!.table.get(0).indicadorExterno,
    data!!.table.get(0).codigoEstacion,
    data!!.table.get(0).nombreEstacion,
    data!!.table.get(0).cargo,
    data!!.table.get(0).dni,
    data!!.table.get(0).superior,
    data!!.table.get(0).codigoArea,
    data!!.table.get(0).nombreArea,
    data!!.table.get(0).ubigeoDireccion,
    data!!.table.get(0).direccion,
    data!!.table.get(0).referencia,
    data!!.table.get(0).codUsuarioReq
)
