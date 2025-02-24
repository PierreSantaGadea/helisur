package com.helisur.helisurapp.ui.login

import android.os.Bundle
import android.os.Handler

import androidx.activity.OnBackPressedCallback
import com.helisur.helisurapp.databinding.ActivitySplashBinding
import com.helisur.helisurapp.domain.util.BaseActivity
import com.helisur.helisurapp.domain.util.SessionUserManager
import com.helisur.helisurapp.ui.sync.SyncActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity  : BaseActivity() {

    private val className = "SplashActivity"
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        timerSplash()
        disableBackButton()
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
            next(ModulesActivity::class.java, null)
         //   next(MainActivityVendedor::class.java, null)
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


}