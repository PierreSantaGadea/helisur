package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo


import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.helisur.helisurapp.R
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneListaFormatosCloudResponse
import com.helisur.helisurapp.databinding.ActivityListaFormatosBinding
import com.helisur.helisurapp.domain.util.BaseActivity
import com.helisur.helisurapp.domain.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListaPrevuelosRealizadosActivity  : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityListaFormatosBinding

    var itemList = ArrayList<ObtieneListaFormatosCloudResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaFormatosBinding.inflate(layoutInflater)
        setContentView(binding.root)


        itemList = arrayListOf()

        itemList.add(ObtieneListaFormatosCloudResponse(0,"priueba"))
        itemList.add(ObtieneListaFormatosCloudResponse(0,"priueba2"))

        setRecyclerView(itemList)

        clicListener()

        binding.nombreAeronave.text = getNombreAeronave(baseContext)

    }


    fun getNombreAeronave(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES.AERONAVE, MODE_PRIVATE)
        val text = sharedPreferences.getString(Constants.SHARED_PREFERENCES.NOMBRE_AERONAVE, "")
        return text
    }



    fun clicListener()
    {

        binding.addNewPreVuelo.setOnClickListener {

            val intent = Intent (baseContext, PreVueloActivity::class.java)
            startActivity(intent)

        }

        binding.llBack.setOnClickListener {

         finish()

        }
    }


    fun setRecyclerView(lista: ArrayList<ObtieneListaFormatosCloudResponse>) {
        val recyclerview = binding.rvFormatosPrevuelo
        recyclerview.layoutManager = LinearLayoutManager(baseContext)
        val adapter = ListaPreVuelosRealizadosAdapter(lista)
        recyclerview.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(baseContext, LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(recyclerview.context, R.drawable.divider)!!)

        recyclerview.addItemDecoration(dividerItemDecoration)

        adapter.onItemClick = { contact ->
        }
    }





}