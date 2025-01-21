package com.helisur.helisurapp.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneDatosUsuarioCloudResponse
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneTokenCloudResponse
import com.helisur.helisurapp.domain.model.Usuario
import com.helisur.helisurapp.domain.util.Constants
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
    val isLoading = MutableLiveData<Boolean>()
    val loginState = MutableLiveData<LoginState>(LoginState.START)


    fun login(usuario: String, contrasenia: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = usuarioUseCase.obtieneTokenCloud(usuario, contrasenia)
            if (result != null) {
                isLoading.postValue(false)
                loginState.postValue(LoginState.SUCCESS)
                responseObtieneTokenCloud.postValue(result)
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


    sealed class LoginState {
        object START : LoginState()
        object LOADING : LoginState()
        object SUCCESS : LoginState()
        data class FAILURE(val message: String?) : LoginState()
    }



}