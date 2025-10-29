package com.helisur.helisurapp.ui.mantenimiento

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.helisur.helisurapp.R
import com.helisur.helisurapp.databinding.ActivityMantenimientoBinding
import com.helisur.helisurapp.databinding.PruebaBinding
import com.helisur.helisurapp.databinding.PruebaIaBinding
import com.helisur.helisurapp.domain.util.BaseActivity
import com.helisur.helisurapp.domain.util.ConnectivityRepository
import com.helisur.helisurapp.domain.util.FormatoBorradorManager
import com.helisur.helisurapp.domain.util.InternetViewModel
import com.helisur.helisurapp.domain.util.ServiceSyncData
import com.helisur.helisurapp.domain.util.SessionUserManager
import com.helisur.helisurapp.ui.login.LoginActivity
import com.helisur.helisurapp.ui.mantenimiento.formatos.EscogeAeronaveFragment
import com.helisur.helisurapp.ui.mantenimiento.formatos.FormatosDiscrepanciasFragment
import com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo.PreVueloTabsFragment
import com.helisur.helisurapp.ui.mantenimiento.formatos.spinners.SpinenrItemUbicacion
import com.helisur.helisurapp.ui.sync.SyncActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PruebaIAActivity : BaseActivity() {


    private lateinit var binding: PruebaBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PruebaBinding.inflate(layoutInflater)
        setContentView(binding.root)


       // binding.textviewTitulo.angle = 270

      //  binding.textviewTitulo.topDown = true
       // binding.textviewTitulo.setText("PROGRESO DE INSPECCIÓN")
       // binding.textviewTitulo.setTextColor(Color.BLUE)

      //  setSpinnerr()

    }

/*
    fun setSpinnerr(
    ) {
        var spinnerTipo = binding!!.spiUSer
        val spinnerArray: MutableList<String> = ArrayList()
         spinnerArray.add("Seleccione Usuario")
        spinnerArray.add("Marco De la Vega")
        spinnerArray.add("Leonel Messi")
        spinnerArray.add("Teofilo Cubillas")

        val adapter = ArrayAdapter(baseContext, android.R.layout.simple_spinner_item, spinnerArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipo.adapter = adapter

        spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {

                if(position>0)
                {
                    binding.tvLicencia.text = "51515151"
                    binding.tvLicencia.visibility = View.VISIBLE
                    binding.imagenFirma.visibility = View.VISIBLE
                }



            /**    periodoSelected = periodosList[position].id
                nombrePeriodoSelected = periodosList[position].detalle
                periodoPositionSelected = position
                val sessionManager = SessionUserManager(requireContext())
                concursosList = ArrayList()
                cocursosViewModel.listaConcursos(sessionManager.getToken()!!, periodoSelected)*/
            }
        }
    }

*/



}