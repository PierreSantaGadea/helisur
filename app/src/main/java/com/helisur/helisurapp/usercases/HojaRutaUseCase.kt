package com.helisur.helisurapp.usercases

import android.util.Log
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ActualizaReportajeFormatoCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaFormatoCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.response.ActualizaReportajeFormatoCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.EnviaPdfCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.GrabaFormatoCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosRealizadosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneReportajesFormatoCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasCloudResponse
import com.helisur.helisurapp.data.cloud.hojaruta.model.response.ObtieneHojasRutaCloudResponse
import com.helisur.helisurapp.data.repository.FormatosRepository
import com.helisur.helisurapp.data.repository.HojaRutaRepository
import com.helisur.helisurapp.domain.model.Aeronave
import com.helisur.helisurapp.domain.model.DetalleFormatoRegistro
import com.helisur.helisurapp.domain.model.Formato
import com.helisur.helisurapp.domain.model.FormatoRegistro
import com.helisur.helisurapp.domain.model.Reportaje
import com.helisur.helisurapp.domain.model.Sistema
import com.helisur.helisurapp.domain.model.Tarea
import com.helisur.helisurapp.domain.model.toDomain
import com.helisur.helisurapp.domain.util.Constants
import javax.inject.Inject

class HojaRutaUseCase @Inject constructor(private val repository: HojaRutaRepository) {

    val TAG = "HojaRutaUseCase"

    suspend fun obtieneHojasRuta(): ObtieneHojasRutaCloudResponse {
        try {
            val respuesta = repository.obtieneHojasRuta()
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            val responseFailed = ObtieneHojasRutaCloudResponse()
            responseFailed.success = Constants.ERROR.ERROR_ENTERO
            responseFailed.message = e.toString()
            return responseFailed
        }
    }



}