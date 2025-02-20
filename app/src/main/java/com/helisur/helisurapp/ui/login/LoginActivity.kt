package com.helisur.helisurapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.helisur.helisurapp.databinding.ActivityLoginBinding
import com.helisur.helisurapp.domain.model.Empleado
import com.helisur.helisurapp.domain.util.BaseActivity
import com.helisur.helisurapp.domain.util.ConnectivityRepository
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.InternetViewModel
import com.helisur.helisurapp.domain.util.ServiceSyncData
import com.helisur.helisurapp.domain.util.ServiceSyncDataFirstTime
import com.helisur.helisurapp.domain.util.SessionUserManager
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.mantenimiento.AeronavesViewModel
import com.helisur.helisurapp.ui.sync.SyncActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    var passView: Boolean = false
    var loading: TransparentProgressDialog? = null
    var className = "LoginActivity"
    var online:Boolean?=null

    var empeladosList:ArrayList<Empleado>?=null

    private var internetViewModel: InternetViewModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        clickListener()
        observers()
        disableBackButton()

    }

    fun initUI() {
        binding.etEmail.setText("analista_app")
        binding.etPass.setText("helisur2024.")
        loading = TransparentProgressDialog(this)
        internetViewModel = InternetViewModel(ConnectivityRepository(baseContext))
        loginViewModel.getEmpleadosListDB()

    }

    fun clickListener() {

        binding.btnlogin.setOnClickListener { view ->
            //     if(validations())
            var usuario = binding.etEmail.text.toString().trim()
            var pass = binding.etPass.text.toString().trim()

            if(isOnline())
            {

                if(usuario.equals("analista_app") && pass.equals("helisur2024."))
                {
                    loginViewModel.login(
                        usuario, pass
                    )
                }
                else
                {
                    var userExist = false

                    for(empleado in empeladosList!!)
                    {
                        var empleadoWithoutDomain = empleado.email!!.replace("@helisur.com.pe","")
                        if(usuario.equals(empleadoWithoutDomain)
                            && pass.equals(empleado.numeroDocumento))
                        {
                            userExist = true
                            saveDatosUser(
                                empleado.codigoUsuario!!,
                                empleado.nombreCompleto!!,
                                empleado.apellidoPaterno!!,
                                empleado.apellidoMaterno!!,
                                empleado.cargo!!
                            )
                            next(ModulesActivity::class.java,null)

                        }
                    }

                    if(!userExist)
                    {
                        showErrorDialog("Usuario o contraseña incorrectos")
                    }
                }
            }
            else
            {

                if(empeladosList!!.size==0)
                {
                    //error
                }
                else
                {
                    var userExist = false

                    for(empleado in empeladosList!!)
                    {
                        var empleadoWithoutDomain = empleado.email!!.replace("@helisur.com.pe","")
                        if(usuario.equals(empleadoWithoutDomain)
                            && pass.equals(empleado.numeroDocumento))
                        {
                            userExist = true
                            saveDatosUser(
                                empleado.codigoUsuario!!,
                                empleado.nombreCompleto!!,
                                empleado.apellidoPaterno!!,
                                empleado.apellidoMaterno!!,
                                empleado.cargo!!
                            )
                            next(ModulesActivity::class.java,null)

                        }
                    }
                    if(!userExist)
                    {
                        showErrorDialog("Usuario o contraseña incorrectos")
                    }
                }

            }


        }

        binding.ivPass.setOnClickListener { view ->
            clickViewPass()
        }

    }

    private fun observers() {





        internetViewModel!!.isOnline.observe(this) { isOnline ->
            if (isOnline) {
                // Handle online state
                online= true
                //intent to syncActivity
             //   next(SyncActivity::class.java,null)
            } else {
                // Handle offline state
                online= false
            }
        }


        loginViewModel.isLoading.observe(this, Observer {
            if (it) {
                if (!loading!!.isShowing) {
                    loading!!.show()
                }
            } else {
                if (loading!!.isShowing) {
                    loading!!.dismiss()
                }
            }
        })

        loginViewModel.loginState.observe(this, Observer {
            if (it.toString().contains(Constants.ERROR.SUCCESS)) {
            } else {
                if (it.toString().contains(Constants.ERROR.FAILURE)) {
                    Log.e(className, Constants.ERROR.ERROR)
                }
            }
        })

        loginViewModel.responseObtieneTokenCloud.observe(this, Observer {
            if (it != null) {
                saveTokenUser(it.token)
                beginService()
               // loginViewModel.obtieneDatosUsuarioCloud(binding.etEmail.text.toString().trim())
                loginViewModel.obtieneDatosUsuarioCloud("chroman")
            } else {
                Log.e(className, Constants.ERROR.ERROR)
            }
        })


        loginViewModel.responseObtieneDatosUsuario.observe(this, Observer {
            try {
                if (it != null) {
                    if (it.success == Constants.ERROR.ERROR_ENTERO) {
                        showErrorDialog(it.message)
                    } else {
                        saveDatosUser(
                            it.data!!.table.get(0).codigoUsuario,
                            it.data.table.get(0).nombreUsuario,
                            it.data.table.get(0).apellidoPaterno,
                            it.data.table.get(0).apellidoMaterno,
                            it.data.table.get(0).cargo
                        )
                        next(ModulesActivity::class.java,null)

                    }
                }
            } catch (e: Exception) {
                Log.e(className, e.message!!)
                showErrorDialog(e.toString())
            }

        })


        loginViewModel.responseGetEmpleadoListDB.observe(this, Observer {
            if (it != null) {
                empeladosList = ArrayList(it)
            } else {
                Log.e(className, Constants.ERROR.ERROR)
            }
        })


    }


    fun clickViewPass() {
        try {
            if (!passView) {
                binding.etPass.setTransformationMethod(null)
                passView = true
            } else {
                binding.etPass.setTransformationMethod(PasswordTransformationMethod.getInstance())
                passView = false
            }
        } catch (e: Exception) {
            Log.e(className, e.message!!)
            showErrorDialog(e.toString())
        }

    }

    fun validations(): Boolean {
        var isValid = true
        if (binding.etEmail.text.equals("")) {
            isValid = false
        } else {
            if (!binding.etEmail.text.contains("@")) {
                isValid = false
            } else {
                if (binding.etEmail.length() < 8) {
                    isValid = false
                } else {

                }
            }
        }

        return isValid
    }

    fun beginService() {
        Intent(applicationContext, ServiceSyncDataFirstTime::class.java).also {
            it.action = ServiceSyncData.Actions.START.toString()
            startService(it)
        }
    }


    fun saveDatosUser(
        idUser: String,
        nombres: String,
        apellidoPaterno: String,
        apellidoMaterno: String,
        rol: String
    ) {
        try {
            val sessionManager = SessionUserManager(baseContext)
           // sessionManager.saveUser(binding.etEmail.text.toString().trim())
            sessionManager.saveUser("chroman")
            sessionManager.savePass(binding.etPass.text.toString().trim())
            sessionManager.saveUserId(idUser)
            sessionManager.saveUserNombres(nombres)
            sessionManager.saveUserApellidoPaterno(apellidoPaterno)
            sessionManager.saveUserApellidoMaterno(apellidoMaterno)
            sessionManager.saveUserRol(rol)
            sessionManager.saveUserLogged(true)
        } catch (e: Exception) {
        }
    }

    fun saveTokenUser(token: String) {
        try {
            val sessionManager = SessionUserManager(baseContext)
            sessionManager.saveAuthToken(token)
        } catch (e: Exception) {
        }
    }

    fun disableBackButton() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // No hagas nada aquí para deshabilitar el botón "Atrás"
            }
        })
    }

}