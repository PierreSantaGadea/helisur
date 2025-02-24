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

    val responseGetFormatoListDB = MutableLiveData<List<Formato>?>()
    val responseGetSistemaListDB = MutableLiveData<List<Sistema>?>()
    val responseGetSistemaByFormatoDB = MutableLiveData<List<Sistema>?>()
    val responseGetTareaListDB = MutableLiveData<List<Tarea>?>()
    val responseGetReportajeListDB = MutableLiveData<List<Reportaje>?>()

    val responseGetTareaBySistemaListDB = MutableLiveData<List<Tarea>?>()

    val responseGetFormatosRegistroListDB = MutableLiveData<List<FormatoRegistro>?>()
    val responseGetFormatosRegistroIncompletedListDB = MutableLiveData<List<FormatoRegistro>?>()
    val responseGetDetalleFormatosRegistroListDB = MutableLiveData<List<DetalleFormatoRegistro>?>()
    val responsInsertFormatoRegistroDB = MutableLiveData<Boolean?>()
    val responsInsertDetalleFormatoRegistroDB = MutableLiveData<Boolean?>()

    val responseGetDetalleFormatosRegistroByFormatoRegistroListDB = MutableLiveData<List<DetalleFormatoRegistro>?>()

    val responseUpdateCompleteFormatoRegistroDB = MutableLiveData<Boolean?>()


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


    fun getFormatosListDB() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = formatosUseCase.getFormatosListDB()
            if (result!=null) {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.SUCCESS)
                responseGetFormatoListDB.postValue(result)
            } else {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }

    fun getSistemasListDB() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = formatosUseCase.getSistemasListDB()
            if (result!=null) {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.SUCCESS)
                responseGetSistemaListDB.postValue(result)
            } else {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }


    fun getSistemnasByFormatoDB(idFormato: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = formatosUseCase.getSistemnasByFormato(idFormato)
            if (result!=null) {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.SUCCESS)
                responseGetSistemaByFormatoDB.postValue(result)
            } else {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }



    fun getTareasListDB() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = formatosUseCase.getTareasListDB()
            if (result!=null) {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.SUCCESS)
                responseGetTareaListDB.postValue(result)
            } else {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }

    fun getReportajesListDB() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = formatosUseCase.getReportajesListDB()
            if (result!=null) {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.SUCCESS)
                responseGetReportajeListDB.postValue(result)
            } else {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }

    fun getTareasBySistema(idSistema: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = formatosUseCase.getTareasBySistema(idSistema)
            if (result!=null) {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.SUCCESS)
                responseGetTareaBySistemaListDB.postValue(result)
            } else {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }


    fun getFormatosRegistroListDB() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = formatosUseCase.getFormatosRegistroListDB()
            if (result!=null) {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.SUCCESS)
                responseGetFormatosRegistroListDB.postValue(result)
            } else {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }

    fun getFormatosRegistroIncompletedListDB() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = formatosUseCase.getFormatosRegistroIncompletedListDB()
            if (result!=null) {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.SUCCESS)
                responseGetFormatosRegistroIncompletedListDB.postValue(result)
            } else {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }

    fun getDetalleFormatosRegistroListDB() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = formatosUseCase.getDetalleFormatosRegistroListDB()
            if (result!=null) {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.SUCCESS)
                responseGetDetalleFormatosRegistroListDB.postValue(result)
            } else {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }

    fun getDetalleFormatoRegistroByFormatoRegistroDB(idFormatoRegistroDb: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = formatosUseCase.getDetalleFormatoRegistroByFormatoRegistroDB(idFormatoRegistroDb)
            if (result!=null) {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.SUCCESS)
                responseGetDetalleFormatosRegistroByFormatoRegistroListDB.postValue(result)
            } else {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }


    fun updateCompleteFormatoRegistro(idFormatoRegistroDb: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = formatosUseCase.updateCompleteFormatoRegistro(idFormatoRegistroDb)
            if (result!=null) {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.SUCCESS)
                responseUpdateCompleteFormatoRegistroDB.postValue(result)
            } else {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }

    fun insertFormatoRegistroDB(formatosRegistro: FormatoRegistro) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = formatosUseCase.insertFormatoRegistroDB(formatosRegistro)
            if (result!=null) {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.SUCCESS)
                responsInsertFormatoRegistroDB.postValue(result)
            } else {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }

    fun insertDetalleFormatoRegistroDB(detalles:List<DetalleFormatoRegistro>) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = formatosUseCase.insertDetalleFormatoRegistroDB(detalles)
            if (result!=null) {
                isLoading.postValue(false)
                formatosState.postValue(FormatosState.SUCCESS)
                responsInsertDetalleFormatoRegistroDB.postValue(result)
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