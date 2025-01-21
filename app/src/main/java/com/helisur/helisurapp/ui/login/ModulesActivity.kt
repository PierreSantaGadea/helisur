package com.helisur.helisurapp.ui.login

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.helisur.helisurapp.databinding.ActivityModulosBinding
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import com.helisur.helisurapp.domain.util.BaseActivity
import com.helisur.helisurapp.ui.mantenimiento.MainActivityMantenimiento

@AndroidEntryPoint
class ModulesActivity : BaseActivity()  {

    private lateinit var binding: ActivityModulosBinding
    private val loginViewModel: LoginViewModel by viewModels()
    var passView: Boolean = false
    var loading: TransparentProgressDialog? = null
    var className = "ModulesActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModulosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        clickListener()
        disableBackButton()
        //   doWorkAsync("yeee")
    }


    fun initUI() {

        loading = TransparentProgressDialog(this)
    }


    fun clickListener() {

        binding.llMantenimeinto.setOnClickListener { view ->
            // if(validations())
            next(MainActivityMantenimiento::class.java, null)
        }



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