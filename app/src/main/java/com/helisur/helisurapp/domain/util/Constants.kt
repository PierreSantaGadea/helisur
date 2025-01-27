package com.helisur.helisurapp.domain.util

class Constants {
    object URLS {
        //  const val URL: String = "https://extranet.edusoft.pe"
        const val URL: String = "http://38.199.4.100:81"
        const val OBTIENE_TOKEN: String = "serviceintranetHLS/api/LoginApi"
        const val OBTIENE_DATOS_USUARIO: String = "http://38.199.4.100:81/serviceintranetHLS/api/Usuario/GetByUsuario?usuarioAcceso="
        const val OBTIENE_AERONAVES: String = "serviceintranetHLS/api/ModeloPuestoTecnico/GetAll"
        const val OBTIENE_MODELO_AERONAVES: String = "http://38.199.4.100:81/serviceintranetHLS/api/PuestoTecnico/GetByModeloAeronave?codigoModeloAeronave="
        const val OBTIENE_ESTACIONES: String = "serviceintranetHLS/api/Estacion/GetAll"
        const val OBTIENE_FORMATOS: String = "serviceintranetHLS/api/Formato/GetAll"

        const val OBTIENE_SISTEMAS: String = "serviceintranetHLS/api/FormatoSistema/GetFormatoSistemaBy"
        const val OBTIENE_TAREAS: String = "serviceintranetHLS/api/FormatoTarea/GetFormatoTareaBy"
        const val OBTIENE_REPORTAJES: String = "serviceintranetHLS/api/Formato/GetAll"

        const val OBTIENE_EMPLEADOS: String = "serviceintranetHLS/api/Empleado/GetEmpleadoAreaBy"

        const val GRABA_FORMATO: String = "serviceintranetHLS/api/FormatoRegistro/Insertar"
    }

    object ERROR {
        const val ERROR: String = "ERROR INESPERADO"
        const val FAILURE: String = "Error"
        const val SUCCESS: String = "SUCCESS"
        const val ERROR_EN_CODIGO: String = "ERROR : "

        const val ERROR_ENTERO :Int = 0

        const val MESSAGE_400: String = "Request inválido"
        const val MESSAGE_401: String = "Usuario no autorizado"
        const val MESSAGE_403: String = ""
        const val MESSAGE_404: String = "Servicio no encontrado o no disponible"
        const val MESSAGE_500: String = "Error interno del servidor"
        const val MESSAGE_503: String = "Servicio no disponible"
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
        const val _503: Int = 503
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

    object SHARED_PREFERENCES
    {
        const val AERONAVE: String = "AERONAVE"
        const val ID_AERONAVE: String = "ID_AERONAVE"
        const val NOMBRE_AERONAVE: String = "NOMBRE_AERONAVE"

        const val RTV: String = "RTV"

        const val FORMATO: String = "FORMATO"
        const val ID_FORMATO: String = "ID_FORMATO"
        const val NOMBRE_FORMATO: String = "NOMBRE_FORMATO"
    }
}