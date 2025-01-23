package com.helisur.helisurapp.data.cloud.aeronaves.apis

import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneEstacionesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveCloudResponse
import com.helisur.helisurapp.domain.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AeronavesService @Inject constructor(private val api: AeronavesApiClient) {


    suspend fun obtieneAeronaves(
    ): ObtieneAeronavesCloudResponse {
        return withContext(Dispatchers.IO) {
            val response = api.obtieneAeronaves()
            when (response.code()) {
                Constants.RESPONSE_CODE._200 -> response.body()!!

                Constants.RESPONSE_CODE._400 -> ObtieneAeronavesCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_400
                )

                Constants.RESPONSE_CODE._401 -> ObtieneAeronavesCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_401
                )

                Constants.RESPONSE_CODE._403 -> ObtieneAeronavesCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_403
                )

                Constants.RESPONSE_CODE._404 -> ObtieneAeronavesCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_404
                )

                Constants.RESPONSE_CODE._500 -> ObtieneAeronavesCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_500
                )

                else -> { // Note the block
                    ObtieneAeronavesCloudResponse(
                        Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_OTHER
                    )
                }
            }
        }
    }


    suspend fun obtieneModelosAeronave(
        aeronave: String
    ): ObtieneModelosAeronaveCloudResponse {
        var url: String = Constants.URLS.OBTIENE_MODELO_AERONAVES + aeronave
        return withContext(Dispatchers.IO) {
            val response = api.obtieneModelosAeronave(url)
            when (response.code()) {
                Constants.RESPONSE_CODE._200 -> response.body()!!

                Constants.RESPONSE_CODE._400 -> ObtieneModelosAeronaveCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_400
                )

                Constants.RESPONSE_CODE._401 -> ObtieneModelosAeronaveCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_401
                )

                Constants.RESPONSE_CODE._403 -> ObtieneModelosAeronaveCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_403
                )

                Constants.RESPONSE_CODE._404 -> ObtieneModelosAeronaveCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_404
                )

                Constants.RESPONSE_CODE._500 -> ObtieneModelosAeronaveCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_500
                )

                else -> { // Note the block
                    ObtieneModelosAeronaveCloudResponse(
                        Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_OTHER
                    )
                }
            }
        }
    }


    suspend fun obtieneEstaciones(
    ): ObtieneEstacionesCloudResponse {
        return withContext(Dispatchers.IO) {
            val response = api.obtieneEstaciones()
            when (response.code()) {
                Constants.RESPONSE_CODE._200 -> response.body()!!

                Constants.RESPONSE_CODE._400 -> ObtieneEstacionesCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_400
                )

                Constants.RESPONSE_CODE._401 -> ObtieneEstacionesCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_401
                )

                Constants.RESPONSE_CODE._403 -> ObtieneEstacionesCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_403
                )

                Constants.RESPONSE_CODE._404 -> ObtieneEstacionesCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_404
                )

                Constants.RESPONSE_CODE._500 -> ObtieneEstacionesCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_500
                )

                Constants.RESPONSE_CODE._503 -> ObtieneEstacionesCloudResponse(
                    Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_503
                )

                else -> { // Note the block
                    ObtieneEstacionesCloudResponse(
                        Constants.RESPONSE_CODE.FAILED, null, Constants.ERROR.MESSAGE_OTHER
                    )
                }
            }
        }
    }


}