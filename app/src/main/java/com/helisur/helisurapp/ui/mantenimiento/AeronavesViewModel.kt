package com.helisur.helisurapp.ui.mantenimiento

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneEstacionesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveCloudResponse
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.ui.mantenimiento.formatos.FormatosViewModel.FormatosState
import com.helisur.helisurapp.usercases.AeronavesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AeronavesViewModel @Inject constructor(
    private val aeronavesUseCase: AeronavesUseCase
) : ViewModel() {


    val responseObtieneAeronaves = MutableLiveData<ObtieneAeronavesCloudResponse>()
    val responseModelosAeronave = MutableLiveData<ObtieneModelosAeronaveCloudResponse>()
    val responseObtieneEstaciones = MutableLiveData<ObtieneEstacionesCloudResponse>()
    val isLoading = MutableLiveData<Boolean>()
    val aeronavesState = MutableLiveData<AeronavesState>(AeronavesState.START)


    fun obtieneAeronaves() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = aeronavesUseCase.obtieneAeronaves()
            if (result != null) {
                if(result.success == Constants.ERROR.ERROR_ENTERO)
                {
                    isLoading.postValue(false)
                    aeronavesState.postValue(AeronavesState.FAILURE(result.message))
                }
                else
                {
                    isLoading.postValue(false)
                    aeronavesState.postValue(AeronavesState.SUCCESS)
                    responseObtieneAeronaves.postValue(result)
                }

            } else {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }


    fun obtieneModelosAeronave(aeronave: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = aeronavesUseCase.obtieneModelosAeronave(aeronave)
            if (result != null) {

                if(result.success == Constants.ERROR.ERROR_ENTERO)
                {
                    isLoading.postValue(false)
                    aeronavesState.postValue(AeronavesState.FAILURE(result.message))
                }
                else
                {
                    isLoading.postValue(false)
                    aeronavesState.postValue(AeronavesState.SUCCESS)
                    responseModelosAeronave.postValue(result)
                }

            } else {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }


    fun obtieneEstaciones() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = aeronavesUseCase.obtieneEstaciones()
            if (result != null) {
                if(result.success == Constants.ERROR.ERROR_ENTERO)
                {
                    isLoading.postValue(false)
                    aeronavesState.postValue(AeronavesState.FAILURE(result.message))
                }
                else
                {
                    isLoading.postValue(false)
                    aeronavesState.postValue(AeronavesState.SUCCESS)
                    responseObtieneEstaciones.postValue(result)
                }

            } else {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }


    sealed class AeronavesState {
        object START : AeronavesState()
        object LOADING : AeronavesState()
        object SUCCESS : AeronavesState()
        data class FAILURE(val message: String?) : AeronavesState()
    }


}