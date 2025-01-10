package com.helisur.helisurapp.ui.login

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.activity.OnBackPressedCallback
import com.helisur.helisurapp.databinding.ActivityLoginBinding
import com.helisur.helisurapp.domain.util.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
 //   private val loginViewModel: LoginViewModel by viewModels()
    var passView: Boolean = false
  //  var loading: TransparentProgressDialog? = null
    var className = "LoginActivity"

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
      //  loading = TransparentProgressDialog(this)
    }

    fun clickListener() {

        binding.btnlogin.setOnClickListener { view ->
            // if(validations())
          //  next(ModulesActivity::class.java, null)
            //    loginViewModel.login(
            //        binding.etEmail.text.toString().trim(), binding.etPass.text.toString().trim(), ""
            //    )
        }

        binding.ivPass.setOnClickListener { view ->
          //  clickViewPass()
        }

    }

    private fun observers() {

        /*
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

        loginViewModel.responseLoginUsuario.observe(this, Observer {
            if (it != null) {
                /*
                saveDatosUser(
                    it.id!!,
                    it.token!!,
                    it.nombres!!,
                    it.apellidoPaterno!!,
                    it.apellidoMaterno!!,
                    it.rol!!
                )

                 */
                //    loginViewModel.listaMenu(it.token)
                //  next(ModulesActivity::class.java, null)
                val intent = Intent(this, ModulesActivity::class.java)
                startActivity(intent)
            } else {
                //   Log.e(className, Constants.ERROR.ERROR)
                //   showErrorDialog(Constants.ERROR.ERROR)
                //Toast.makeText(this, Constants.ERROR.ERROR, Toast.LENGTH_SHORT).show()
            }
        })

        loginViewModel.responseListaMenu.observe(this, Observer {
            try {
                if (it != null) {
                    next(MainActivityVendedor::class.java, null)
                } else {
                    Log.e(className, Constants.ERROR.ERROR)
                    showErrorDialog(Constants.ERROR.ERROR)
                }
            }
            catch (e:Exception)
            {
                Log.e(className, e.message!!)
                showErrorDialog(e.message)
            }

        })
*/
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
        }
        catch (e:Exception)
        {
            //   Log.e(className, e.message!!)
            //   showErrorDialog(e.message)
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

    fun disableBackButton()
    {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // No hagas nada aquí para deshabilitar el botón "Atrás"
            }
        })
    }

}