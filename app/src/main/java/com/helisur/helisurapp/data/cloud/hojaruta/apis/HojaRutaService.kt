package com.helisur.helisurapp.data.cloud.hojaruta.apis

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HojaRutaService @Inject constructor(private val api: HojaRutaApiClient) {

    suspend fun obtieneHojasRuta(
    ): ObtieneHojasRutaCloudResponse {
        return withContext(Dispatchers.IO) {
            val response = api.obtieneHojasRuta()
            when (response.code()) {
                Constants.RESPONSE_CODE._200 -> response.body()!!

                Constants.RESPONSE_CODE._400 -> ObtieneHojasRutaCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_400
                )

                Constants.RESPONSE_CODE._401 -> ObtieneHojasRutaCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_401
                )

                Constants.RESPONSE_CODE._403 -> ObtieneHojasRutaCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_403
                )

                Constants.RESPONSE_CODE._404 -> ObtieneHojasRutaCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_404
                )

                Constants.RESPONSE_CODE._500 -> ObtieneHojasRutaCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_500
                )

                Constants.RESPONSE_CODE._503 -> ObtieneHojasRutaCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_503
                )

                else -> {
                    ObtieneHojasRutaCloudResponse(
                        Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_OTHER
                    )
                }
            }
        }
    }



    suspend fun obtieneListaActividadesPorHojaRuta(
        idHojaRuta: String
    ): ObtieneListaActividadesPorHojaRutaCloudResponse {
        val parameterBody = ObtieneListaActividadesPorHojaRutaCloudParameter(idHojaRuta)
        return withContext(Dispatchers.IO) {
            val response = api.obtieneListaActividadesPorHojaRuta(parameterBody)
            when (response.code()) {
                Constants.RESPONSE_CODE._200 -> response.body()!!

                Constants.RESPONSE_CODE._400 -> ObtieneListaActividadesPorHojaRutaCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_400
                )

                Constants.RESPONSE_CODE._401 -> ObtieneListaActividadesPorHojaRutaCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_401
                )

                Constants.RESPONSE_CODE._403 -> ObtieneListaActividadesPorHojaRutaCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_403
                )

                Constants.RESPONSE_CODE._404 -> ObtieneListaActividadesPorHojaRutaCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_404
                )

                Constants.RESPONSE_CODE._500 -> ObtieneListaActividadesPorHojaRutaCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_500
                )

                Constants.RESPONSE_CODE._503 -> ObtieneListaActividadesPorHojaRutaCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_503
                )

                else -> { // Note the block
                    ObtieneListaActividadesPorHojaRutaCloudResponse(
                        Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_OTHER
                    )
                }
            }
        }
    }




    suspend fun obtieneListaResponsableHojaRuta(
        cadena1: String, cadena2: String
    ): ObtieneListaResponsableHojaRutaCloudResponse {
        val parameterBody = ObtieneListaResponsableHojaRutaCloudParameter(cadena1,cadena2)
        return withContext(Dispatchers.IO) {
            val response = api.obtieneListaResponsableHojaRuta(parameterBody)
            when (response.code()) {
                Constants.RESPONSE_CODE._200 -> response.body()!!

                Constants.RESPONSE_CODE._400 -> ObtieneListaResponsableHojaRutaCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_400
                )

                Constants.RESPONSE_CODE._401 -> ObtieneListaResponsableHojaRutaCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_401
                )

                Constants.RESPONSE_CODE._403 -> ObtieneListaResponsableHojaRutaCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_403
                )

                Constants.RESPONSE_CODE._404 -> ObtieneListaResponsableHojaRutaCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_404
                )

                Constants.RESPONSE_CODE._500 -> ObtieneListaResponsableHojaRutaCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_500
                )

                Constants.RESPONSE_CODE._503 -> ObtieneListaResponsableHojaRutaCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_503
                )

                else -> { // Note the block
                    ObtieneListaResponsableHojaRutaCloudResponse(
                        Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_OTHER
                    )
                }
            }
        }
    }




}