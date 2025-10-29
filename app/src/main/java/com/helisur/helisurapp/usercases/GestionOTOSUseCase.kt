package com.helisur.helisurapp.usercases

import android.util.Log
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneEstacionesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveCloudResponse
import com.helisur.helisurapp.data.cloud.gestionotos.model.response.ObtieneOTOSCloudResponse
import com.helisur.helisurapp.data.database.entities.response.SimpleResponse
import com.helisur.helisurapp.data.database.entities.response.ListModeloAeronaveResponse
import com.helisur.helisurapp.data.repository.AeronavesRepository
import com.helisur.helisurapp.data.repository.GestionOTOSRepository
import com.helisur.helisurapp.domain.model.Aeronave
import com.helisur.helisurapp.domain.model.Estacion
import com.helisur.helisurapp.domain.model.ModeloAeronave
import com.helisur.helisurapp.domain.util.Constants
import javax.inject.Inject

class GestionOTOSUseCase @Inject constructor(private val repository: GestionOTOSRepository) {

    val TAG = "GestionOTOSUseCase"

    suspend fun getOTOSList(): ObtieneOTOSCloudResponse {
        try {
            val respuesta = repository.obtieneOTOS()
            return respuesta
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            val responseFailed = ObtieneOTOSCloudResponse()
            responseFailed.success = Constants.ERROR.ERROR_ENTERO
            responseFailed.message = e.toString()
            return responseFailed
        }
    }









}