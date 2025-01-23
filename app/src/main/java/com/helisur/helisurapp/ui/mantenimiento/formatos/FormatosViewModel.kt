package com.helisur.helisurapp.ui.mantenimiento.formatos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasCloudResponse
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.usercases.FormatosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormatosViewModel @Inject constructor(
    private val formatosUseCase: FormatosUseCase
) : ViewModel() {


    val responseObtieneFormatos= MutableLiveData<ObtieneFormatosCloudResponse>()
    val responseObtieneSistemas= MutableLiveData<ObtieneSistemasCloudResponse>()
    val responseObtieneTareas= MutableLiveData<ObtieneTareasCloudResponse>()
    val isLoading = MutableLiveData<Boolean>()
    val formatosState = MutableLiveData<FormatosState>(FormatosState.START)


    fun obtieneFormatos() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = formatosUseCase.obtieneFormatos()
            if (result != null) {
                if(result.success == Constants.ERROR.ERROR_ENTERO)
                {
                    isLoading.postValue(false)
                    formatosState.postValue(FormatosState.FAILURE(result.message))
                }
                else
                {
                    isLoading.postValue(false)
                    formatosState.postValue(FormatosState.SUCCESS)
                    responseObtieneFormatos.postValue(result)
                }
            } else {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }

    fun obtieneSistemas(codigoFormato:String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = formatosUseCase.obtieneSistemas(codigoFormato)
            if (result != null) {
                if(result.success == Constants.ERROR.ERROR_ENTERO)
                {
                    isLoading.postValue(false)
                    formatosState.postValue(FormatosState.FAILURE(result.message))
                }
                else
                {
                    isLoading.postValue(false)
                    formatosState.postValue(FormatosState.SUCCESS)
                    responseObtieneSistemas.postValue(result)
                }
            } else {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }

    fun obtieneTareas(codigoSistema:String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = formatosUseCase.obtieneTareas(codigoSistema)
            if (result != null) {
                if(result.success == Constants.ERROR.ERROR_ENTERO)
                {
                    isLoading.postValue(false)
                    formatosState.postValue(FormatosState.FAILURE(result.message))
                }
                else
                {
                    isLoading.postValue(false)
                    formatosState.postValue(FormatosState.SUCCESS)
                    responseObtieneTareas.postValue(result)
                }
            } else {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }



    sealed class FormatosState {
        object START : FormatosState()
        object LOADING : FormatosState()
        object SUCCESS : FormatosState()
        data class FAILURE(val message: String?) : FormatosState()
    }


}