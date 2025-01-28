package com.helisur.helisurapp.data.repository

import com.helisur.helisurapp.data.cloud.formatos.apis.FormatosService
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaFormatoCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ObtieneFormatosRealizadosCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.response.GrabaFormatoCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosRealizadosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasCloudResponse
import com.helisur.helisurapp.domain.model.Sistema
import com.helisur.helisurapp.domain.model.Tarea
import com.helisur.helisurapp.domain.model.toDomain

import javax.inject.Inject

class FormatosRepository @Inject constructor(
    private val api: FormatosService
) {

    suspend fun obtieneFormatos(
    ): ObtieneFormatosCloudResponse {
        val response: ObtieneFormatosCloudResponse = api.obtieneFormatos()
        return response
    }

    /*
    suspend fun obtieneSistemas(
        codigoFormato: String
    ): ObtieneSistemasCloudResponse {
        val response: ObtieneSistemasCloudResponse = api.obtieneSistemas(codigoFormato)
        return response
    }

     */


    suspend fun obtieneSistemas(
        codigoFormato: String
    ): ArrayList<Sistema> {
        val response: ObtieneSistemasCloudResponse = api.obtieneSistemas(codigoFormato)

        var lista:ArrayList<Sistema> = arrayListOf()

        for(item in response.data!!.table)
        {
            lista.add(item.toDomain())
        }
        return lista
    }



/*
    suspend fun obtieneTareas(
        codigoSistema: String
    ): ObtieneTareasCloudResponse {
        val response: ObtieneTareasCloudResponse = api.obtieneTareas(codigoSistema)
        return response
    }

 */

    suspend fun obtieneTareas(
        obtieneTareas: String
    ): ArrayList<Tarea> {
        val response: ObtieneTareasCloudResponse = api.obtieneTareas(obtieneTareas)

        var lista:ArrayList<Tarea> = arrayListOf()

        for(item in response.data!!.table)
        {
            lista.add(item.toDomain())
        }
        return lista
    }



    suspend fun grabaFormato(
        parameter: GuardaFormatoCloudParameter
    ): GrabaFormatoCloudResponse {
        val response: GrabaFormatoCloudResponse = api.grabaFormato(parameter)
        return response
    }

    suspend fun obtieneFormatosRealizados(
        codigoFormato:String,formatosHoy:String
    ): ObtieneFormatosRealizadosCloudResponse {
        var parameterBody = ObtieneFormatosRealizadosCloudParameter(codigoFormato,formatosHoy)
        val response: ObtieneFormatosRealizadosCloudResponse = api.obtieneFormatosRealizados(parameterBody)
        return response
    }

}