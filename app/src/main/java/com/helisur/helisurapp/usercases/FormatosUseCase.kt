package com.helisur.helisurapp.usercases

import android.util.Log
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaFormatoCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.response.GrabaFormatoCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosRealizadosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneReportajesFormatoCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasCloudResponse
import com.helisur.helisurapp.data.repository.FormatosRepository
import com.helisur.helisurapp.domain.model.Aeronave
import com.helisur.helisurapp.domain.model.DetalleFormatoRegistro
import com.helisur.helisurapp.domain.model.Formato
import com.helisur.helisurapp.domain.model.FormatoRegistro
import com.helisur.helisurapp.domain.model.Sistema
import com.helisur.helisurapp.domain.model.Tarea
import com.helisur.helisurapp.domain.model.toDomain
import com.helisur.helisurapp.domain.util.Constants
import javax.inject.Inject

class FormatosUseCase @Inject constructor(private val repository: FormatosRepository) {

    val TAG = "FormatosUseCase"

    suspend fun obtieneFormatos(): ObtieneFormatosCloudResponse {
        try {
            val respuesta = repository.obtieneFormatos()
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            val responseFailed = ObtieneFormatosCloudResponse()
            responseFailed.success = Constants.ERROR.ERROR_ENTERO
            responseFailed.message = e.toString()
            return responseFailed
        }
    }

/*
    suspend fun obtieneSistemas(codigoFormato: String): ObtieneSistemasCloudResponse {
        try {
            val respuesta = repository.obtieneSistemas(codigoFormato)
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            val responseFailed = ObtieneSistemasCloudResponse()
            responseFailed.success = Constants.ERROR.ERROR_ENTERO
            responseFailed.message = e.message.toString()
            return responseFailed
        }
    }


 */
    suspend fun obtieneSistemas(codigoFormato: String): ArrayList<Sistema> {
        try {
            val respuesta = repository.obtieneSistemas(codigoFormato)
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            val sistemaFailed = Sistema()
            sistemaFailed.messageFailed = e.message.toString()
            var lista:ArrayList<Sistema> = arrayListOf()
            lista.add(sistemaFailed)
            return lista
        }
    }


    suspend fun obtieneTareas(codigoSistema: String): ArrayList<Tarea> {
        try {
            val respuesta = repository.obtieneTareas(codigoSistema)
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            val tareaFailed = Tarea()
            tareaFailed.messageFailed = e.message.toString()
            var lista:ArrayList<Tarea> = arrayListOf()
            lista.add(tareaFailed)
            return lista
        }
    }


    suspend fun grabaFormato(parameter: GuardaFormatoCloudParameter): GrabaFormatoCloudResponse {
        try {
            val respuesta = repository.grabaFormato(parameter)
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            val grabacionFailed = GrabaFormatoCloudResponse()
            grabacionFailed.message = e.message.toString()
            return grabacionFailed
        }
    }


    suspend fun obtieneFormatosRealizados(codigoFormato:String,formatosHoy:String): ObtieneFormatosRealizadosCloudResponse {
        try {
            val respuesta = repository.obtieneFormatosRealizados(codigoFormato,formatosHoy)
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            val grabacionFailed = ObtieneFormatosRealizadosCloudResponse()
            grabacionFailed.message = e.message.toString()
            return grabacionFailed
        }
    }

    suspend fun obtieneReportajesFormato(codigoFormato:String): ObtieneReportajesFormatoCloudResponse {
        try {
            val respuesta = repository.obtieneReportajesFormato(codigoFormato)
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            val grabacionFailed = ObtieneReportajesFormatoCloudResponse()
            grabacionFailed.message = e.message.toString()
            return grabacionFailed
        }
    }


    suspend fun getFormatosListDB(): List<Formato>? {
        try {
            val respuesta = repository.getFormatosListDB()
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            return null
        }
    }

    suspend fun getSistemasListDB(): List<Sistema>? {
        try {
            val respuesta = repository.getSistemasListDB()
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            return null
        }
    }

    suspend fun getSistemnasByFormato(idFormato: String): List<Sistema>? {
        try {
            val respuesta = repository.getSistemnasByFormato(idFormato)
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            return null
        }
    }

    suspend fun getTareasListDB(): List<Tarea>? {
        try {
            val respuesta = repository.getTareasListDB()
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            return null
        }
    }


    suspend fun getTareasBySistema(idSistema: String): List<Tarea>? {
        try {
            val respuesta = repository.getTareasBySistema(idSistema)
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            return null
        }
    }



    suspend fun insertFormatoRegistroDB(formatoRegistro: FormatoRegistro): Boolean? {
        try {
            val respuesta = repository.insertFormatoRegistroDB(formatoRegistro)
            return true
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            return null
        }
    }

    suspend fun getFormatosRegistroListDB(): List<FormatoRegistro>? {
        try {
            val respuesta = repository.getFormatosRegistroListDB()
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            return null
        }
    }


    suspend fun insertDetalleFormatoRegistroDB(detalles: List<DetalleFormatoRegistro>): Boolean? {
        try {
            val respuesta = repository.insertDetalleFormatoRegistroListDB(detalles)
            return true
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            return null
        }
    }

    suspend fun getDetalleFormatosRegistroListDB(): List<DetalleFormatoRegistro>? {
        try {
            val respuesta = repository.getDetalleFormatosRegistroListDB()
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            return null
        }
    }


    suspend fun getDetalleFormatoRegistroByFormatoRegistroDB(idFormatoRegistroDb: String): List<DetalleFormatoRegistro>? {
        try {
            val respuesta = repository.getDetalleFormatoRegistroByFormatoRegistroDB(idFormatoRegistroDb)
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            return null
        }
    }




}