package com.helisur.helisurapp.ui.hojaruta

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ActualizaReportajeFormatoCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaFormatoCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.response.ActualizaReportajeFormatoCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.EnviaPdfCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.GrabaFormatoCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosRealizadosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneReportajesFormatoCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasCloudResponse
import com.helisur.helisurapp.data.cloud.hojaruta.model.response.ObtieneHojasRutaCloudResponse
import com.helisur.helisurapp.data.cloud.hojaruta.model.response.ObtieneListaActividadesPorHojaRutaCloudResponse
import com.helisur.helisurapp.data.cloud.hojaruta.model.response.ObtieneListaResponsableHojaRutaCloudResponse
import com.helisur.helisurapp.domain.model.DetalleFormatoRegistro
import com.helisur.helisurapp.domain.model.Estacion
import com.helisur.helisurapp.domain.model.Formato
import com.helisur.helisurapp.domain.model.FormatoRegistro
import com.helisur.helisurapp.domain.model.Reportaje
import com.helisur.helisurapp.domain.model.Sistema
import com.helisur.helisurapp.domain.model.Tarea
import com.helisur.helisurapp.domain.util.ConnectivityRepository
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.ui.mantenimiento.AeronavesViewModel.AeronavesState
import com.helisur.helisurapp.usercases.FormatosUseCase
import com.helisur.helisurapp.usercases.HojaRutaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HojaRutaViewModel @Inject constructor(
    private val hojaRutaUseCase: HojaRutaUseCase
) : ViewModel() {

    val responseObtieneHojaRuta= MutableLiveData<ObtieneHojasRutaCloudResponse>()
    val responseObtieneListaActividadesPorHojaRuta= MutableLiveData<ObtieneListaActividadesPorHojaRutaCloudResponse>()
    val responseObtieneListaResponsableHojaRuta= MutableLiveData<ObtieneListaResponsableHojaRutaCloudResponse>()

    val isLoading = MutableLiveData<Boolean>()
    val hojaRutaState = MutableLiveData<HojaRutaState>(HojaRutaState.START)


    fun obtieneHojasRuta() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = hojaRutaUseCase.obtieneHojasRuta()
            if (result != null) {
                if(result.success == Constants.ERROR.ERROR_ENTERO)
                {
                    isLoading.postValue(false)
                    hojaRutaState.postValue(HojaRutaState.FAILURE(result.message))
                }
                else
                {
                    isLoading.postValue(false)
                    hojaRutaState.postValue(HojaRutaState.SUCCESS)
                    responseObtieneHojaRuta.postValue(result)
                }
            } else {
                isLoading.postValue(false)
                hojaRutaState.postValue(HojaRutaState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }


    fun obtieneListaActividadesPorHojaRuta(idHojaRuta: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = hojaRutaUseCase.obtieneListaActividadesPorHojaRuta(idHojaRuta)
            if (result != null) {
                if(result.success == Constants.ERROR.ERROR_ENTERO)
                {
                    isLoading.postValue(false)
                    hojaRutaState.postValue(HojaRutaState.FAILURE(result.message))
                }
                else
                {
                    isLoading.postValue(false)
                    hojaRutaState.postValue(HojaRutaState.SUCCESS)
                    responseObtieneListaActividadesPorHojaRuta.postValue(result)
                }
            } else {
                isLoading.postValue(false)
                hojaRutaState.postValue(HojaRutaState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }



    fun obtieneListaResponsableHojaRuta(cadena1: String,cadena2: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = hojaRutaUseCase.obtieneListaResponsableHojaRuta(cadena1,cadena2)
            if (result != null) {
                if(result.success == Constants.ERROR.ERROR_ENTERO)
                {
                    isLoading.postValue(false)
                    hojaRutaState.postValue(HojaRutaState.FAILURE(result.message))
                }
                else
                {
                    isLoading.postValue(false)
                    hojaRutaState.postValue(HojaRutaState.SUCCESS)
                    responseObtieneListaResponsableHojaRuta.postValue(result)
                }
            } else {
                isLoading.postValue(false)
                hojaRutaState.postValue(HojaRutaState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }



    sealed class HojaRutaState {
        object START : HojaRutaState()
        object LOADING : HojaRutaState()
        object SUCCESS : HojaRutaState()
        data class FAILURE(val Error: String?) : HojaRutaState()
    }


}