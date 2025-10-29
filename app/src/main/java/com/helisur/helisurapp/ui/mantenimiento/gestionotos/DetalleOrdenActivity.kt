package com.helisur.helisurapp.ui.mantenimiento.gestionotos

import android.annotation.SuppressLint
import android.os.Bundle
import com.helisur.helisurapp.databinding.ActivityPrevueloBinding
import com.helisur.helisurapp.domain.util.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetalleOrdenActivity  : BaseActivity() {

    private lateinit var binding: ActivityPrevueloBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrevueloBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadTabHomeFragment()
    }

    @SuppressLint("SuspiciousIndentation")
    fun loadTabHomeFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val homeFragment: DetalleOrdenTabsFragment =
            DetalleOrdenTabsFragment()
        fragmentTransaction.replace(binding.containerView.id, homeFragment)
        fragmentTransaction.commit()
    }

}