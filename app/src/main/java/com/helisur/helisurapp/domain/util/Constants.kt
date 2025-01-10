package com.helisur.helisurapp.domain.util

class Constants {
    object URLS {
        //  const val URL: String = "https://extranet.edusoft.pe"
        const val URL: String = "http://38.199.4.100:81"
           const val LOGIN: String = "ws.concursos/api/appService/login"
        const val OBTIENE_TOKEN: String = "serviceintranetHLS/api/LoginApi"

        const val OBTIENE_DATOS_USUARIO: String = "serviceintranetHLS/api/Usuario/GetByUsuario?usuarioAcceso=chroman"

        const val LISTA_MENU: String = "ws.identity/api/identity/getMenu"
        const val LISTA_PERIODOS: String = "ws.concursos/api/appService/getAllPeriodo"
        const val LISTA_CONCURSOS: String = "ws.concursos/api/appService/getAllConcurso"
        const val LISTA_LINEAS: String = "ws.concursos/api/appService/getAllLinea"
        const val LISTA_CATEGORIAS: String = "ws.concursos/api/appService/getAllCategoria"
        const val LISTA_CANDADOS: String = "ws.concursos/api/appService/getAllCandado"

        const val LISTA_CARTERA_CLIENTES: String = ""
    }

    object ERROR {
        const val ERROR: String = "ERROR INESPERADO"
        const val FAILURE: String = "FAILURE"
        const val SUCCESS: String = "SUCCESS"

        const val MESSAGE_400: String = "Request invalido"
        const val MESSAGE_401: String = "Usuario no autorizado"
        const val MESSAGE_403: String = ""
        const val MESSAGE_404: String = "Servicio no encontrado o no disponible"
        const val MESSAGE_500: String = "Error interno del servidor"
        const val MESSAGE_OTHER: String = "Error desconocido"
    }

    object RESPONSE_CODE {
        const val FAILED = 0
        const val SUCCESS = 1
        const val _200: Int = 200
        const val _400: Int = 400
        const val _401: Int = 401
        const val _403: Int = 403
        const val _404: Int = 404
        const val _500: Int = 500
    }

    object RESPONSE_MESSAGES {

    }

    object TABS_VENDEDOR {
        const val INICIO: Int = 0
        const val AVANCES: Int = 1
        const val CARTERA_CLIENTES: Int = 2
        const val PERFIL: Int = 3
        const val TAB_INICIAL: Int = AVANCES

        const val NOMBRE_INICIO: String = "INICIO"
        const val NOMBRE_AVANCES: String = "AVANCES"
        const val NOMBRE_CARTERA_CLIENTES: String = "CARTERA_CLIENTES"
        const val NOMBRE_PERFIL: String = "PERFIL"
    }

    object TABS_PRE_VUELO
    {
        const val AERONAVE_ANTECEDENTE_REQUERIMIENTO: Int = 0
        const val SISTEMAS: Int = 1
        const val ANOTACIONES: Int = 2
        const val FIRMA_RESPONSABLE: Int = 3
        const val FIRMA_PILOTO_COPILOTO: Int = 4
    }
}