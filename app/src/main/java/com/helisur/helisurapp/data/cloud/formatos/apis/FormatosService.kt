package com.helisur.helisurapp.data.cloud.formatos.apis

import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaFormatoCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ObtieneFormatosRealizadosCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ObtieneReportajesFormatoParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ObtieneReportajesParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ObtieneSistemasCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ObtieneTareasCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.response.GrabaFormatoCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosRealizadosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneReportajesCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneReportajesFormatoCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasCloudResponse
import com.helisur.helisurapp.domain.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FormatosService @Inject constructor(private val api: FormatosApiClient) {

    suspend fun obtieneFormatos(
    ): ObtieneFormatosCloudResponse {
        return withContext(Dispatchers.IO) {
            val response = api.obtieneFormatos()
            when (response.code()) {
                Constants.RESPONSE_CODE._200 -> response.body()!!

                Constants.RESPONSE_CODE._400 -> ObtieneFormatosCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_400
                )

                Constants.RESPONSE_CODE._401 -> ObtieneFormatosCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_401
                )

                Constants.RESPONSE_CODE._403 -> ObtieneFormatosCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_403
                )

                Constants.RESPONSE_CODE._404 -> ObtieneFormatosCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_404
                )

                Constants.RESPONSE_CODE._500 -> ObtieneFormatosCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_500
                )

                Constants.RESPONSE_CODE._503 -> ObtieneFormatosCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_503
                )

                else -> { // Note the block
                    ObtieneFormatosCloudResponse(
                        Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_OTHER
                    )
                }
            }
        }
    }


    suspend fun obtieneSistemas(
        codigoFormato: String
    ): ObtieneSistemasCloudResponse {
        val parameterBody = ObtieneSistemasCloudParameter(codigoFormato)
        return withContext(Dispatchers.IO) {
            val response = api.obtieneSistemas(parameterBody)
            when (response.code()) {
                Constants.RESPONSE_CODE._200 -> response.body()!!

                Constants.RESPONSE_CODE._400 -> ObtieneSistemasCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_400
                )

                Constants.RESPONSE_CODE._401 -> ObtieneSistemasCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_401
                )

                Constants.RESPONSE_CODE._403 -> ObtieneSistemasCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_403
                )

                Constants.RESPONSE_CODE._404 -> ObtieneSistemasCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_404
                )

                Constants.RESPONSE_CODE._500 -> ObtieneSistemasCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_500
                )

                Constants.RESPONSE_CODE._503 -> ObtieneSistemasCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_503
                )

                else -> { // Note the block
                    ObtieneSistemasCloudResponse(
                        Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_OTHER
                    )
                }
            }
        }
    }


    suspend fun obtieneTareas(
        codigoSistema: String
    ): ObtieneTareasCloudResponse {
        val parameterBody = ObtieneTareasCloudParameter(codigoSistema)
        return withContext(Dispatchers.IO) {
            val response = api.obtieneTareas(parameterBody)
            when (response.code()) {
                Constants.RESPONSE_CODE._200 -> response.body()!!

                Constants.RESPONSE_CODE._400 -> ObtieneTareasCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_400
                )

                Constants.RESPONSE_CODE._401 -> ObtieneTareasCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_401
                )

                Constants.RESPONSE_CODE._403 -> ObtieneTareasCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_403
                )

                Constants.RESPONSE_CODE._404 -> ObtieneTareasCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_404
                )

                Constants.RESPONSE_CODE._500 -> ObtieneTareasCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_500
                )

                Constants.RESPONSE_CODE._503 -> ObtieneTareasCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_503
                )

                else -> { // Note the block
                    ObtieneTareasCloudResponse(
                        Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_OTHER
                    )
                }
            }
        }
    }



    suspend fun obtieneReportajes(
    ): ObtieneReportajesCloudResponse {
        val parameterBody = ObtieneReportajesParameter("")
        return withContext(Dispatchers.IO) {
            val response = api.obtieneReportajes(parameterBody)
            when (response.code()) {
                Constants.RESPONSE_CODE._200 -> response.body()!!

                Constants.RESPONSE_CODE._400 -> ObtieneReportajesCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_400
                )

                Constants.RESPONSE_CODE._401 -> ObtieneReportajesCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_401
                )

                Constants.RESPONSE_CODE._403 -> ObtieneReportajesCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_403
                )

                Constants.RESPONSE_CODE._404 -> ObtieneReportajesCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_404
                )

                Constants.RESPONSE_CODE._500 -> ObtieneReportajesCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_500
                )

                Constants.RESPONSE_CODE._503 -> ObtieneReportajesCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_503
                )

                else -> { // Note the block
                    ObtieneReportajesCloudResponse(
                        Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_OTHER
                    )
                }
            }
        }
    }



    suspend fun grabaFormato(
        parameterBody: GuardaFormatoCloudParameter
    ): GrabaFormatoCloudResponse {
        return withContext(Dispatchers.IO) {
            val response = api.grabaFormato(parameterBody)
            when (response.code()) {
                Constants.RESPONSE_CODE._200 -> response.body()!!

                Constants.RESPONSE_CODE._400 -> GrabaFormatoCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_400
                )

                Constants.RESPONSE_CODE._401 -> GrabaFormatoCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_401
                )

                Constants.RESPONSE_CODE._403 -> GrabaFormatoCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_403
                )

                Constants.RESPONSE_CODE._404 -> GrabaFormatoCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_404
                )

                Constants.RESPONSE_CODE._500 -> GrabaFormatoCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_500
                )

                Constants.RESPONSE_CODE._503 -> GrabaFormatoCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_503
                )

                else -> { // Note the block
                    GrabaFormatoCloudResponse(
                        Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_OTHER
                    )
                }
            }
        }
    }


    suspend fun obtieneFormatosRealizados(
        parameterBody: ObtieneFormatosRealizadosCloudParameter
    ): ObtieneFormatosRealizadosCloudResponse {
        return withContext(Dispatchers.IO) {
            val response = api.obtieneFormatosRealizados(parameterBody)
            when (response.code()) {
                Constants.RESPONSE_CODE._200 -> response.body()!!

                Constants.RESPONSE_CODE._400 -> ObtieneFormatosRealizadosCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_400
                )

                Constants.RESPONSE_CODE._401 -> ObtieneFormatosRealizadosCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_401
                )

                Constants.RESPONSE_CODE._403 -> ObtieneFormatosRealizadosCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_403
                )

                Constants.RESPONSE_CODE._404 -> ObtieneFormatosRealizadosCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_404
                )

                Constants.RESPONSE_CODE._500 -> ObtieneFormatosRealizadosCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_500
                )

                Constants.RESPONSE_CODE._503 -> ObtieneFormatosRealizadosCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_503
                )

                else -> { // Note the block
                    ObtieneFormatosRealizadosCloudResponse(
                        Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_OTHER
                    )
                }
            }
        }
    }


    suspend fun obtieneReportajesFormato(
        parameterBody: ObtieneReportajesFormatoParameter
    ): ObtieneReportajesFormatoCloudResponse {
        return withContext(Dispatchers.IO) {
            val response = api.obtieneReportajesFormato(parameterBody)
            when (response.code()) {
                Constants.RESPONSE_CODE._200 -> response.body()!!

                Constants.RESPONSE_CODE._400 -> ObtieneReportajesFormatoCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_400
                )

                Constants.RESPONSE_CODE._401 -> ObtieneReportajesFormatoCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_401
                )

                Constants.RESPONSE_CODE._403 -> ObtieneReportajesFormatoCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_403
                )

                Constants.RESPONSE_CODE._404 -> ObtieneReportajesFormatoCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_404
                )

                Constants.RESPONSE_CODE._500 -> ObtieneReportajesFormatoCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_500
                )

                Constants.RESPONSE_CODE._503 -> ObtieneReportajesFormatoCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_503
                )

                else -> { // Note the block
                    ObtieneReportajesFormatoCloudResponse(
                        Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_OTHER
                    )
                }
            }
        }
    }




}