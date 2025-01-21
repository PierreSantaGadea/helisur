package com.helisur.helisurapp.data.cloud.formatos.apis

import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesCloudResponse
import com.helisur.helisurapp.domain.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FormatosService @Inject constructor
    ( private val api: FormatosApiClient ) {


    suspend fun obtieneAeronaves(
    ): ObtieneAeronavesCloudResponse {
        return withContext(Dispatchers.IO) {
            val response = api.obtieneAeronaves()
            when (response.code()) {
                Constants.RESPONSE_CODE._200 ->
                    response.body()!!
                Constants.RESPONSE_CODE._400 ->
                    ObtieneAeronavesCloudResponse(Constants.RESPONSE_CODE.FAILED,null,Constants.ERROR.MESSAGE_400)
                Constants.RESPONSE_CODE._401 ->
                    ObtieneAeronavesCloudResponse(Constants.RESPONSE_CODE.FAILED,null,Constants.ERROR.MESSAGE_401)
                Constants.RESPONSE_CODE._403 ->
                    ObtieneAeronavesCloudResponse(Constants.RESPONSE_CODE.FAILED,null,Constants.ERROR.MESSAGE_403)
                Constants.RESPONSE_CODE._404 ->
                    ObtieneAeronavesCloudResponse(Constants.RESPONSE_CODE.FAILED,null,Constants.ERROR.MESSAGE_404)
                Constants.RESPONSE_CODE._500 ->
                    ObtieneAeronavesCloudResponse(Constants.RESPONSE_CODE.FAILED,null,Constants.ERROR.MESSAGE_500)
                else -> { // Note the block
                    ObtieneAeronavesCloudResponse(Constants.RESPONSE_CODE.FAILED,null,Constants.ERROR.MESSAGE_OTHER)
                }
            }
        }
    }







}