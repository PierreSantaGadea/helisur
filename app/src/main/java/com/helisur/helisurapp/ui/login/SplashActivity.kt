package com.helisur.helisurapp.ui.login

import android.net.Uri
import android.os.Bundle
import android.os.Handler

import androidx.activity.OnBackPressedCallback
import com.helisur.helisurapp.databinding.ActivitySplashBinding
import com.helisur.helisurapp.domain.util.BaseActivity
import com.helisur.helisurapp.domain.util.SessionUserManager
import com.helisur.helisurapp.ui.mantenimiento.VisorPDFActivity
import com.helisur.helisurapp.ui.sync.SyncActivity

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity  : BaseActivity() {

    private val className = "SplashActivity"
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        timerSplash()
        disableBackButton()


      /*  val uri = Uri.parse("file:///android_asset/formatopostuvuelo.pdf")
        val config = PdfActivityConfiguration.Builder(this).build()
        PdfActivity.showDocument(this, uri, config)*/
    }


    fun timerSplash() {
        val handler = Handler()
        handler.postDelayed({

            conditionToLogIn()
        }, 3000)
    }


    fun conditionToLogIn() {
        val sessionManager = SessionUserManager(baseContext)
        val userLogged = sessionManager.getLogged()
        if (userLogged!!) {

     //       next(VisorPDFActivity::class.java, null)
            next(ModulesActivity::class.java, null)
        } else {
            next(LoginActivity::class.java, null)
        }
    }

    fun disableBackButton() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
    }
/*
    override fun onBeforeTextSelectionChange(p0: TextSelection?, p1: TextSelection?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onAfterTextSelectionChange(p0: TextSelection?, p1: TextSelection?) {
        TODO("Not yet implemented")
    }

    override fun onEnterTextSelectionMode(p0: TextSelectionController) {
        TODO("Not yet implemented")
    }

    override fun onExitTextSelectionMode(p0: TextSelectionController) {
        TODO("Not yet implemented")
    }
*/

}