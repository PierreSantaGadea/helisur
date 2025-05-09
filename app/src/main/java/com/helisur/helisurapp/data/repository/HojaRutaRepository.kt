package com.helisur.helisurapp.data.repository

import android.util.Log
import com.helisur.helisurapp.data.cloud.formatos.apis.FormatosService
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ActualizaReportajeFormatoCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaFormatoCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ObtieneFormatosRealizadosCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ObtieneReportajesFormatoParameter
import com.helisur.helisurapp.data.cloud.formatos.model.response.ActualizaReportajeFormatoCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.EnviaPdfCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.GrabaFormatoCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosRealizadosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneReportajesCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneReportajesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneReportajesFormatoCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.hojaruta.apis.HojaRutaService
import com.helisur.helisurapp.data.cloud.hojaruta.model.response.ObtieneHojasRutaCloudResponse
import com.helisur.helisurapp.data.cloud.hojaruta.model.response.ObtieneListaActividadesPorHojaRutaCloudResponse
import com.helisur.helisurapp.data.cloud.hojaruta.model.response.ObtieneListaResponsableHojaRutaCloudResponse
import com.helisur.helisurapp.data.database.dao.DetalleFormatoRegistroDao
import com.helisur.helisurapp.data.database.dao.EstacionDao
import com.helisur.helisurapp.data.database.dao.FormatoDao
import com.helisur.helisurapp.data.database.dao.FormatoRegistroDao
import com.helisur.helisurapp.data.database.dao.ReportajeDao
import com.helisur.helisurapp.data.database.dao.SistemaDao
import com.helisur.helisurapp.data.database.dao.TareaDao
import com.helisur.helisurapp.data.database.entities.AeronaveEntity
import com.helisur.helisurapp.data.database.entities.DetalleFormatoRegistroEntity
import com.helisur.helisurapp.data.database.entities.FormatoEntity
import com.helisur.helisurapp.data.database.entities.FormatoRegistroEntity
import com.helisur.helisurapp.data.database.entities.ReportajeEntity
import com.helisur.helisurapp.data.database.entities.TareaEntity
import com.helisur.helisurapp.data.database.entities.response.SistemaEntity
import com.helisur.helisurapp.data.database.entities.response.toDB
import com.helisur.helisurapp.data.database.entities.toDB
import com.helisur.helisurapp.domain.model.Aeronave
import com.helisur.helisurapp.domain.model.DetalleFormatoRegistro
import com.helisur.helisurapp.domain.model.Formato
import com.helisur.helisurapp.domain.model.FormatoRegistro
import com.helisur.helisurapp.domain.model.Reportaje
import com.helisur.helisurapp.domain.model.Sistema
import com.helisur.helisurapp.domain.model.Tarea
import com.helisur.helisurapp.domain.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import javax.inject.Inject

class HojaRutaRepository @Inject constructor(
    private val hojaRutaCloudData: HojaRutaService

) {

    var className = "HojaRutaRepository"

    suspend fun obtieneHojasRuta(
    ): ObtieneHojasRutaCloudResponse {
        val response: ObtieneHojasRutaCloudResponse = hojaRutaCloudData.obtieneHojasRuta()
        return response
    }


    suspend fun obtieneListaActividadesPorHojaRuta(idHojaRuta: String
    ): ObtieneListaActividadesPorHojaRutaCloudResponse {
        val response: ObtieneListaActividadesPorHojaRutaCloudResponse = hojaRutaCloudData.obtieneListaActividadesPorHojaRuta(idHojaRuta)
        return response
    }


    suspend fun obtieneListaResponsableHojaRuta(cadena1: String, cadena2: String
    ): ObtieneListaResponsableHojaRutaCloudResponse {
        val response: ObtieneListaResponsableHojaRutaCloudResponse = hojaRutaCloudData.obtieneListaResponsableHojaRuta(cadena1,cadena2)
        return response
    }


}