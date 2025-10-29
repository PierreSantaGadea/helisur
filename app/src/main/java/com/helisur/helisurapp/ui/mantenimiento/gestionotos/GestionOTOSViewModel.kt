package com.helisur.helisurapp.ui.mantenimiento.gestionotos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneEstacionesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveCloudResponse
import com.helisur.helisurapp.data.cloud.gestionotos.model.response.ObtieneOTOSCloudResponse
import com.helisur.helisurapp.data.database.entities.response.SimpleResponse
import com.helisur.helisurapp.data.database.entities.response.ListModeloAeronaveResponse
import com.helisur.helisurapp.domain.model.Aeronave
import com.helisur.helisurapp.domain.model.Estacion
import com.helisur.helisurapp.domain.model.ModeloAeronave
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.usercases.AeronavesUseCase
import com.helisur.helisurapp.usercases.GestionOTOSUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GestionOTOSViewModel @Inject constructor(
    private val aeronavesUseCase: GestionOTOSUseCase
) : ViewModel() {

    val responseGetOTOSListCloud = MutableLiveData<ObtieneOTOSCloudResponse>()


    val isLoading = MutableLiveData<Boolean>()
    val gestionOTOSState = MutableLiveData<GestionOTOSState>(GestionOTOSState.START)


    fun getOTOSList() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = aeronavesUseCase.getOTOSList()
            if (result.success == 1) {
                isLoading.postValue(false)
                gestionOTOSState.postValue(GestionOTOSState.SUCCESS)
                responseGetOTOSListCloud.postValue(result)
            } else {
                isLoading.postValue(false)
                gestionOTOSState.postValue(GestionOTOSState.FAILURE(result.message))
            }
        }
    }








    sealed class GestionOTOSState {
        object START : GestionOTOSState()
        object LOADING : GestionOTOSState()
        object SUCCESS : GestionOTOSState()
        data class FAILURE(val message: String?) : GestionOTOSState()
    }


}