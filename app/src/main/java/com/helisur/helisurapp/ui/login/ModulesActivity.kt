package com.helisur.helisurapp.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.helisur.helisurapp.databinding.ActivityModulosBinding
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import com.helisur.helisurapp.domain.util.BaseActivity
import com.helisur.helisurapp.domain.util.ConnectivityRepository
import com.helisur.helisurapp.domain.util.InternetViewModel
import com.helisur.helisurapp.domain.util.ServiceSyncData
import com.helisur.helisurapp.ui.mantenimiento.MainActivityMantenimiento

@AndroidEntryPoint
class ModulesActivity : BaseActivity() {

    private lateinit var binding: ActivityModulosBinding
    private val loginViewModel: LoginViewModel by viewModels()
    var passView: Boolean = false
    var loading: TransparentProgressDialog? = null
    var className = "ModulesActivity"
    private var internetViewModel: InternetViewModel? = null
    var online: Boolean? = null
    var syncNow: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModulosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        clickListener()
        disableBackButton()
        observers()
    }

    fun beginService() {
        Intent(applicationContext, ServiceSyncData::class.java).also {
            it.action = ServiceSyncData.Actions.START.toString()
            startService(it)
        }
    }

    fun observers() {
        internetViewModel!!.isOnline.observe(this) { isOnline ->
            if (isOnline) {
                // Handle online state
                online = true
                //intent to syncActivity
                if (syncNow) {
                    // next(SyncActivity::class.java,null)
                    beginService()
                    syncNow = false
                }

            } else {
                // Handle offline state
                online = false
                syncNow = true
            }
        }
    }


    fun initUI() {

        loading = TransparentProgressDialog(this)
        internetViewModel = InternetViewModel(ConnectivityRepository(baseContext))
    }


    fun clickListener() {

        binding.llMantenimeinto.setOnClickListener { view ->
            // if(validations())
            next(MainActivityMantenimiento::class.java, null)
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