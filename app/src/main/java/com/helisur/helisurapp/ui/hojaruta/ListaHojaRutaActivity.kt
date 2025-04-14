package com.helisur.helisurapp.ui.hojaruta

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log

import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.helisur.helisurapp.R
import com.helisur.helisurapp.data.cloud.hojaruta.model.response.ObtieneHojasRutaDataTableCloudResponse
import com.helisur.helisurapp.databinding.ActivityListaHojaRutaBinding

import com.helisur.helisurapp.domain.util.BaseActivity
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.SessionUserManager.Companion.USER
import com.helisur.helisurapp.domain.util.TransparentProgressDialog

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListaHojaRutaActivity  : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityListaHojaRutaBinding
    var loading: TransparentProgressDialog? = null
    var className = "ListaHojaRutaActivity"
    var listaHojasRuta = ArrayList<ObtieneHojasRutaDataTableCloudResponse>()

    private val hojasRutaViewModel: HojaRutaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaHojaRutaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        clicListener()
        observers()
    }


    fun initUI()
    {
        loading = TransparentProgressDialog(this)
        hojasRutaViewModel.obtieneHojasRuta()
    }


    fun clicListener()
    {

    }

    private fun observers() {


        hojasRutaViewModel.isLoading.observe(this, Observer {
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


        hojasRutaViewModel.hojaRutaState.observe(this, Observer {
            if (it.toString().contains(Constants.ERROR.SUCCESS)) {
            } else {
                if (it.toString().contains(Constants.ERROR.FAILURE)) {
                    var message = it.toString().replace("FAILURE(Error=","")
                    var messagFinal = message.replace(")","")
                    showErrorDialog(messagFinal)
                    Log.e(className, it.toString())
                }
            }
        })


        hojasRutaViewModel.responseObtieneHojaRuta.observe(this, Observer {
            if (it != null) {

                listaHojasRuta = ArrayList(it.data!!.table)
                setRecyclerViewHojasRuta(listaHojasRuta!!)

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


    }


    fun saveDatosHojaRuta(nroHojaRuta:String,servMantenimiento:String, cliente:String, nroRequerimiento:String, modeloAeronave:String, matriculaAeronave:String,
                          ubicacion:String,plazo:String)
    {
         var prefs: SharedPreferences = baseContext.getSharedPreferences("HOJARUTA", Context.MODE_PRIVATE)

        val editor = prefs.edit()

        editor.putString("nroHojaRuta", nroHojaRuta)
        editor.putString("servMantenimiento", servMantenimiento)
        editor.putString("cliente", cliente)
        editor.putString("nroRequerimiento", nroRequerimiento)
        editor.putString("modeloAeronave", modeloAeronave)
        editor.putString("matriculaAeronave", matriculaAeronave)
        editor.putString("ubicacion", ubicacion)
        editor.putString("plazo", plazo)

        editor.apply()



    }


        fun setRecyclerViewHojasRuta(lista: ArrayList<ObtieneHojasRutaDataTableCloudResponse>) {
        val recyclerview = binding.rvHojasRuta
        recyclerview.layoutManager = LinearLayoutManager(baseContext)
        val adapter = ListaHojasRutaAdapter(lista,baseContext)
        recyclerview.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(baseContext, LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                recyclerview.context,
                R.drawable.divider
            )!!
        )

        recyclerview.addItemDecoration(dividerItemDecoration)

        adapter.onItemClick = { hojaRuta ->
            saveDatosHojaRuta(hojaRuta.id,hojaRuta.codigoTareaInspeccion,hojaRuta.codigoCliente,hojaRuta.numeroRequerimientoMnto,hojaRuta.codigoModeloPuesto,
                hojaRuta.codigoPuestoTecnico,hojaRuta.nombreEstacion,hojaRuta.tsnPlazo)
            next(NuevaHojaRutaActivity::class.java,null)

        }
    }




}