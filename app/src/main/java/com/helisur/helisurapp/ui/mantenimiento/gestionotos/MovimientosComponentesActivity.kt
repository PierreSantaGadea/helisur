package com.helisur.helisurapp.ui.mantenimiento.gestionotos

import android.os.Bundle
import com.helisur.helisurapp.databinding.ActivityOrdenMovimientosComponentesBinding

import com.helisur.helisurapp.databinding.ActivityPrevueloBinding
import com.helisur.helisurapp.domain.util.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovimientosComponentesActivity  : BaseActivity() {

    private lateinit var binding: ActivityOrdenMovimientosComponentesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdenMovimientosComponentesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickListener()
    }


    fun clickListener()
    {
        binding.llBack.setOnClickListener {
            finish()
        }
    }


}