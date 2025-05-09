package com.helisur.helisurapp.data.cloud.hojaruta.apis

import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ActualizaReportajeFormatoCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.EnviaPdfCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaFormatoCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ObtieneFormatosRealizadosCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ObtieneReportajesFormatoParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ObtieneReportajesParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ObtieneSistemasCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ObtieneTareasCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.response.ActualizaReportajeFormatoCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.EnviaPdfCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.GrabaFormatoCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosRealizadosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneReportajesCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneReportajesFormatoCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasCloudResponse
import com.helisur.helisurapp.data.cloud.hojaruta.model.parameter.ObtieneListaActividadesPorHojaRutaCloudParameter
import com.helisur.helisurapp.data.cloud.hojaruta.model.parameter.ObtieneListaResponsableHojaRutaCloudParameter
import com.helisur.helisurapp.data.cloud.hojaruta.model.response.ObtieneHojasRutaCloudResponse
import com.helisur.helisurapp.data.cloud.hojaruta.model.response.ObtieneListaActividadesPorHojaRutaCloudResponse
import com.helisur.helisurapp.data.cloud.hojaruta.model.response.ObtieneListaResponsableHojaRutaCloudResponse

import com.helisur.helisurapp.domain.util.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface HojaRutaApiClient {

    @GET(Constants.URLS.OBTIENE_LISTA_HOJAS_RUTA)
    suspend fun obtieneHojasRuta(): Response<ObtieneHojasRutaCloudResponse>


    @POST(Constants.URLS.OBTIENE_LISTA_LINEAS_POR_HOJA_RUTA)
    suspend fun obtieneListaActividadesPorHojaRuta(@Body parameterBody: ObtieneListaActividadesPorHojaRutaCloudParameter): Response<ObtieneListaActividadesPorHojaRutaCloudResponse>


    @POST(Constants.URLS.OBTIENE_LISTA_RESPONSABLE_HOJA_RUTA)
    suspend fun obtieneListaResponsableHojaRuta(@Body parameterBody: ObtieneListaResponsableHojaRutaCloudParameter): Response<ObtieneListaResponsableHojaRutaCloudResponse>


}