package com.helisur.helisurapp.data.cloud.gestionotos.apis

import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ObtieneSistemasCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasCloudResponse
import com.helisur.helisurapp.data.cloud.gestionotos.model.parameter.ObtieneInsumosOTOSCloudParameter
import com.helisur.helisurapp.data.cloud.gestionotos.model.response.ObtieneInsumosByOTOSCloudResponse
import com.helisur.helisurapp.data.cloud.gestionotos.model.response.ObtieneOTOSCloudResponse
import com.helisur.helisurapp.domain.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GestionOTOSService @Inject constructor(private val api: GestionOTOSApiClient) {

    suspend fun obtieneOTOS(
    ): ObtieneOTOSCloudResponse {
        return withContext(Dispatchers.IO) {
            val response = api.obtieneOTOS()
            when (response.code()) {
                Constants.RESPONSE_CODE._200 -> response.body()!!

                Constants.RESPONSE_CODE._400 -> ObtieneOTOSCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_400
                )

                Constants.RESPONSE_CODE._401 -> ObtieneOTOSCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_401
                )

                Constants.RESPONSE_CODE._403 -> ObtieneOTOSCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_403
                )

                Constants.RESPONSE_CODE._404 -> ObtieneOTOSCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_404
                )

                Constants.RESPONSE_CODE._500 -> ObtieneOTOSCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_500
                )

                Constants.RESPONSE_CODE._503 -> ObtieneOTOSCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_503
                )

                else -> { // Note the block
                    ObtieneOTOSCloudResponse(
                        Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_OTHER
                    )
                }
            }
        }
    }


    suspend fun obtieneInsumosByOT(
        codigoOT: String
    ): ObtieneInsumosByOTOSCloudResponse {
        val parameterBody = ObtieneInsumosOTOSCloudParameter(codigoOT)
        return withContext(Dispatchers.IO) {
            val response = api.obtieneInsumosByOT(parameterBody)
            when (response.code()) {
                Constants.RESPONSE_CODE._200 -> response.body()!!

                Constants.RESPONSE_CODE._400 -> ObtieneInsumosByOTOSCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_400
                )

                Constants.RESPONSE_CODE._401 -> ObtieneInsumosByOTOSCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_401
                )

                Constants.RESPONSE_CODE._403 -> ObtieneInsumosByOTOSCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_403
                )

                Constants.RESPONSE_CODE._404 -> ObtieneInsumosByOTOSCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_404
                )

                Constants.RESPONSE_CODE._500 -> ObtieneInsumosByOTOSCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_500
                )

                Constants.RESPONSE_CODE._503 -> ObtieneInsumosByOTOSCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_503
                )

                else -> { // Note the block
                    ObtieneInsumosByOTOSCloudResponse(
                        Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_OTHER
                    )
                }
            }
        }
    }

    /*
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
    */





}