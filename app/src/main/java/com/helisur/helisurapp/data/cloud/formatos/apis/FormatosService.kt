package com.helisur.helisurapp.data.cloud.formatos.apis

import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ObtieneSistemasCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ObtieneTareasCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosCloudResponse
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


}