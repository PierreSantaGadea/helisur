package com.helisur.helisurapp.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneDatosUsuarioCloudResponse
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneEmpleadosDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneTokenCloudResponse
import com.helisur.helisurapp.domain.model.Aeronave
import com.helisur.helisurapp.domain.model.Empleado
import com.helisur.helisurapp.domain.model.Usuario
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.ui.mantenimiento.AeronavesViewModel.AeronavesState
import com.helisur.helisurapp.usercases.UsuarioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor
    (
    private val usuarioUseCase: UsuarioUseCase
) : ViewModel() {


    val responseObtieneTokenCloud = MutableLiveData<ObtieneTokenCloudResponse>()
    val responseObtieneDatosUsuario = MutableLiveData<ObtieneDatosUsuarioCloudResponse>()
    val responseObtieneEmpleados = MutableLiveData<List<ObtieneEmpleadosDataTableCloudResponse>>()
    val isLoading = MutableLiveData<Boolean>()
    val loginState = MutableLiveData<LoginState>(LoginState.START)

    val responseGetEmpleadoListDB = MutableLiveData<List<Empleado>?>()


    fun login(usuario: String, contrasenia: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = usuarioUseCase.obtieneTokenCloud(usuario, contrasenia)
            if (result != null) {
                if(result.success==Constants.ERROR.ERROR_ENTERO)
                {
                    isLoading.postValue(false)
                    loginState.postValue(LoginState.FAILURE(result.message))
                }
                else
                {
                    isLoading.postValue(false)
                    loginState.postValue(LoginState.SUCCESS)
                    responseObtieneTokenCloud.postValue(result)
                }

            } else {
                isLoading.postValue(false)
                loginState.postValue(LoginState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }


    fun obtieneDatosUsuarioCloud(usuario: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = usuarioUseCase.obtieneDatosUsuarioCloud(usuario)
            if (result != null) {
                isLoading.postValue(false)
                loginState.postValue(LoginState.SUCCESS)
                responseObtieneDatosUsuario.postValue(result)
            } else {
                isLoading.postValue(false)
                loginState.postValue(LoginState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }


    fun obtieneEmpleados(area: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = usuarioUseCase.obtieneEmpleados(area)
            if (result != null) {

                if(result.success==Constants.ERROR.ERROR_ENTERO)
                {
                    isLoading.postValue(false)
                    loginState.postValue(LoginState.FAILURE(result.message))
                }
                else
                {
                    isLoading.postValue(false)
                    loginState.postValue(LoginState.SUCCESS)
                    responseObtieneEmpleados.postValue(result.data!!.table)
                }

            } else {
                isLoading.postValue(false)
                loginState.postValue(LoginState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }

    fun getEmpleadosListDB() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = usuarioUseCase.getEmpleadosListDB()
            if (result!=null) {
                isLoading.postValue(false)
                loginState.postValue(LoginState.SUCCESS)
                responseGetEmpleadoListDB.postValue(result)
            } else {
                isLoading.postValue(false)
                loginState.postValue(LoginState.FAILURE(Constants.ERROR.ERROR))
            }
        }
    }


    sealed class LoginState {
        object START : LoginState()
        object LOADING : LoginState()
        object SUCCESS : LoginState()
        data class FAILURE(val Error: String?) : LoginState()
    }



}