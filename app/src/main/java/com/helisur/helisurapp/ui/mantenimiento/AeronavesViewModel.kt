package com.helisur.helisurapp.ui.mantenimiento

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveCloudResponse
import com.helisur.helisurapp.domain.util.Constants
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
    val isLoading = MutableLiveData<Boolean>()
    val loginState = MutableLiveData<AeronavesState>(AeronavesState.START)


    fun obtieneAeronaves() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = aeronavesUseCase.obtieneAeronaves()
            if (result != null) {
                isLoading.postValue(false)
                loginState.postValue(AeronavesState.SUCCESS)
                responseObtieneAeronaves.postValue(result)
            } else {
                isLoading.postValue(false)
                loginState.postValue(AeronavesState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }


    fun obtieneModelosAeronave(aeronave: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = aeronavesUseCase.obtieneModelosAeronave(aeronave)
            if (result != null) {
                isLoading.postValue(false)
                loginState.postValue(AeronavesState.SUCCESS)
                responseModelosAeronave.postValue(result)
            } else {
                isLoading.postValue(false)
                loginState.postValue(AeronavesState.FAILURE(Constants.ERROR.ERROR))
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