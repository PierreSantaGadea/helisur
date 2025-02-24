package com.helisur.helisurapp.ui.mantenimiento

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneEstacionesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveCloudResponse
import com.helisur.helisurapp.data.database.entities.response.SimpleResponse
import com.helisur.helisurapp.data.database.entities.response.ListModeloAeronaveResponse
import com.helisur.helisurapp.domain.model.Aeronave
import com.helisur.helisurapp.domain.model.Estacion
import com.helisur.helisurapp.domain.model.ModeloAeronave
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.usercases.AeronavesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AeronavesViewModel @Inject constructor(
    private val aeronavesUseCase: AeronavesUseCase
) : ViewModel() {

    val responseGetModeloAeronaveListCloud = MutableLiveData<ObtieneAeronavesCloudResponse>()
    val responseGetAeronaveListCloud = MutableLiveData<ObtieneModelosAeronaveCloudResponse>()
    val responseGetEstacionesListCloud = MutableLiveData<ObtieneEstacionesCloudResponse>()

    val responseGetModeloAeronaveListDB = MutableLiveData<ListModeloAeronaveResponse>()
    val responseInsertModeloAeronaveListDB = MutableLiveData<SimpleResponse>()
    val responseDeleteModeloAeronaveListDB = MutableLiveData<SimpleResponse>()
    val responseDeleteModeloAeronaveDB = MutableLiveData<SimpleResponse>()
    val responseUpdateModeloAeronaveDB = MutableLiveData<SimpleResponse>()
    val responseUpdateSyncModeloAeronaveDB = MutableLiveData<SimpleResponse>()

    val responseGetAeronaveListDB = MutableLiveData<List<Aeronave>?>()
    val responseGetEstacionListDB = MutableLiveData<List<Estacion>?>()

    val responseCountDiscrepancias = MutableLiveData<Int?>()

    val responseGetAeronaveByModeloListDB = MutableLiveData<List<Aeronave>?>()

    val isLoading = MutableLiveData<Boolean>()
    val aeronavesState = MutableLiveData<AeronavesState>(AeronavesState.START)


    fun getModelosAeronavesListDB() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = aeronavesUseCase.getModelosAeronavesListDB()
            if (result.success) {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.SUCCESS)
                responseGetModeloAeronaveListDB.postValue(result)
            } else {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.FAILURE(result.message))
            }
        }
    }


    fun insertModeloAeronaveListDB(listaModelosAeronave: List<ModeloAeronave>) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = aeronavesUseCase.insertModeloAeronaveListDB(listaModelosAeronave)
            if (result.success) {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.SUCCESS)
                responseInsertModeloAeronaveListDB.postValue(result)
            } else {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.FAILURE(result.message))
            }
        }
    }

    fun deleteTableModeloAeronaveDB() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = aeronavesUseCase.deleteTableModeloAeronaveDB()
            if (result.success) {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.SUCCESS)
                responseDeleteModeloAeronaveListDB.postValue(result)
            } else {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.FAILURE(result.message))
            }
        }
    }

    fun deleteModeloAeronaveDB(id: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = aeronavesUseCase.deleteModeloAeronaveDB(id)
            if (result.success) {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.SUCCESS)
                responseDeleteModeloAeronaveDB.postValue(result)
            } else {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.FAILURE(result.message))
            }
        }
    }

    fun updateModeloAeronaveDB(idCloud: String,nombre:String,fechaRegistro:String,fechaModificacion:String, sync: Boolean) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = aeronavesUseCase.updateModeloAeronaveDB(idCloud,nombre,fechaRegistro,fechaModificacion, sync)
            if (result.success) {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.SUCCESS)
                responseUpdateModeloAeronaveDB.postValue(result)
            } else {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.FAILURE(result.message))
            }
        }
    }

    fun updateSyncModeloAeronave(idCloud: String, sync: Boolean) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = aeronavesUseCase.updateSyncModeloAeronave(idCloud, sync)
            if (result.success) {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.SUCCESS)
                responseUpdateSyncModeloAeronaveDB.postValue(result)
            } else {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.FAILURE(result.message))
            }
        }
    }


    fun getModeloAeronaveListCloud() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = aeronavesUseCase.getModeloAeronaveListCloud()
            if (result != null) {
                if (result.success == Constants.ERROR.ERROR_ENTERO) {
                    isLoading.postValue(false)
                    aeronavesState.postValue(AeronavesState.FAILURE(result.message))
                } else {
                    isLoading.postValue(false)
                    aeronavesState.postValue(AeronavesState.SUCCESS)
                    responseGetModeloAeronaveListCloud.postValue(result)
                }

            } else {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }


    fun getAeronaveListCloud(aeronave: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = aeronavesUseCase.getAeromaveListCloud(aeronave)
            if (result != null) {
                if (result.success == Constants.ERROR.ERROR_ENTERO) {
                    isLoading.postValue(false)
                    aeronavesState.postValue(AeronavesState.FAILURE(result.message))
                } else {
                    isLoading.postValue(false)
                    aeronavesState.postValue(AeronavesState.SUCCESS)
                    responseGetAeronaveListCloud.postValue(result)
                }

            } else {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }


    fun getEstacionesListCloud() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = aeronavesUseCase.getEstacionesListCloud()
            if (result != null) {
                if (result.success == Constants.ERROR.ERROR_ENTERO) {
                    isLoading.postValue(false)
                    aeronavesState.postValue(AeronavesState.FAILURE(result.message))
                } else {
                    isLoading.postValue(false)
                    aeronavesState.postValue(AeronavesState.SUCCESS)
                    responseGetEstacionesListCloud.postValue(result)
                }

            } else {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }



    fun getAeronavesListDB() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = aeronavesUseCase.getAeronavesListDB()
            if (result!=null) {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.SUCCESS)
                responseGetAeronaveListDB.postValue(result)
            } else {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }

    fun getAeronavesByModeloDB(idModelo:String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = aeronavesUseCase.getAeronavesByModeloDB(idModelo)
            if (result!=null) {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.SUCCESS)
                responseGetAeronaveByModeloListDB.postValue(result)
            } else {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }

    fun getEstacionesListDB() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = aeronavesUseCase.getEstacionesListDB()
            if (result!=null) {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.SUCCESS)
                responseGetEstacionListDB.postValue(result)
            } else {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }


    fun getCountDetallessByAeronave(idAeronave: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = aeronavesUseCase.getCountDetallessByAeronave(idAeronave)
            if (result!=null) {
                isLoading.postValue(false)
                aeronavesState.postValue(AeronavesState.SUCCESS)
                responseCountDiscrepancias.postValue(result)
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