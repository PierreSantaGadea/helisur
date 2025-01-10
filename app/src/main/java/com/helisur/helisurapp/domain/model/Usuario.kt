package com.helisur.helisurapp.domain.model

import com.helisur.helisurapp.data.cloud.usuario.model.response.LoginCloudResponse
import com.helisur.helisurapp.data.cloud.usuario.model.response.LoginCloudResponseDataOpciones

class Usuario(
    var error: String?,
    val changePassword: Boolean?,
    val token: String?,
    var nombres: String?,
    val apellidoPaterno: String?,
    var apellidoMaterno: String?,
    val rol: String?,
    var companiaNombre: String?,
    val razonSocial: String?,
    var usuario: String?,
    val perfilId: String?,
    var controladorPrincipal: String?,
    val isAdmin: Boolean?,
    var vistaPrincipal: String?,
    val compania: String?,
    var ruta: String?,
    val perfil: String?,
    var id: String?,
    val opciones: List<LoginCloudResponseDataOpciones>?,
    var linea: String?
) {
    constructor() : this(
        "",
        false,
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
        false,
        "",
        "",
        "",
        "",
        "",
        null,
        ""
    )
}

fun LoginCloudResponse.toDomain() = Usuario(
    message,
    data.changePassword,
    data.token,
    data.nombres,
    data.apellidoPaterno,
    data.apellidoMaterno,
    data.rol,
    data.companiaNombre,
    data.razonSocial,
    data.usuario,
    data.perfilId,
    data.controladorPrincipal,
    data.isAdmin,
    data.vistaPrincipal,
    data.compania,
    data.ruta,
    data.perfil,
    data.id,
    data.opciones,
    data.linea
)
