package com.helisur.helisurapp.ui.mantenimiento.formatos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaFormatoCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.response.GrabaFormatoCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosRealizadosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneReportajesFormatoCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasCloudResponse
import com.helisur.helisurapp.domain.model.Sistema
import com.helisur.helisurapp.domain.model.Tarea
import com.helisur.helisurapp.domain.util.ConnectivityRepository
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
    val responseGrabaFormato= MutableLiveData<GrabaFormatoCloudResponse>()
    val responseObtieneSistemas= MutableLiveData<ArrayList<Sistema>>()
    val responseObtieneTareas= MutableLiveData<ArrayList<Tarea>>()
    val responseObtieneFormatosRealizados= MutableLiveData<ObtieneFormatosRealizadosCloudResponse>()
    val responseObtieneReportajesFormato= MutableLiveData<ObtieneReportajesFormatoCloudResponse>()
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

/*
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

 */

    fun obtieneSistemas(codigoFormato:String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = formatosUseCase.obtieneSistemas(codigoFormato)
            if (result != null) {
                if(result.size>0)
                {
                    if(result.size == 1)
                    {
                        if(result.get(0).messageFailed.equals(""))
                        {
                            // sin errores 1 solo item
                            isLoading.postValue(false)
                            formatosState.postValue(FormatosState.SUCCESS)
                            responseObtieneSistemas.postValue(result)
                        }
                        else
                        {
                            //error
                            isLoading.postValue(false)
                            formatosState.postValue(FormatosState.FAILURE(result.get(0).messageFailed))
                        }
                    }
                    else
                    {
                        // sin errores varios items
                        isLoading.postValue(false)
                        formatosState.postValue(FormatosState.SUCCESS)
                        responseObtieneSistemas.postValue(result)
                    }
                }
                else
                {
                    //error sin resultados
                    isLoading.postValue(false)
                    formatosState.postValue(FormatosState.FAILURE("No se encontraron resultados"))
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
                if(result.size>0)
                {
                    if(result.size == 1)
                    {
                        if(result.get(0).messageFailed.equals(""))
                        {
                            // sin errores 1 solo item
                            isLoading.postValue(false)
                            formatosState.postValue(FormatosState.SUCCESS)
                            responseObtieneTareas.postValue(result)
                        }
                        else
                        {
                            //error
                            isLoading.postValue(false)
                            formatosState.postValue(FormatosState.FAILURE(result.get(0).messageFailed))
                        }
                    }
                    else
                    {
                        // sin errores varios items
                        isLoading.postValue(false)
                        formatosState.postValue(FormatosState.SUCCESS)
                        responseObtieneTareas.postValue(result)
                    }
                }
                else
                {
                    //error sin resultados
                    isLoading.postValue(false)
                    formatosState.postValue(FormatosState.FAILURE("No se encontraron resultados"))
                }

            } else {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }


    fun grabaFormato(parameter: GuardaFormatoCloudParameter) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = formatosUseCase.grabaFormato(parameter)
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
                    responseGrabaFormato.postValue(result)
                }
            } else {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }



    fun obtieneFormatosRealizados(codigoFormato:String,formatosHoy:String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = formatosUseCase.obtieneFormatosRealizados(codigoFormato,formatosHoy)
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
                    responseObtieneFormatosRealizados.postValue(result)
                }
            } else {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }


    fun obtieneReportajesFormato(codigoFormato:String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = formatosUseCase.obtieneReportajesFormato(codigoFormato)
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
                    responseObtieneReportajesFormato.postValue(result)
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
        data class FAILURE(val Error: String?) : FormatosState()
    }


}