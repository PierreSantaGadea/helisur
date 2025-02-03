package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.helisur.helisurapp.R
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosRealizadosDataTableCloudResponse
import com.helisur.helisurapp.databinding.ActivityListaPrevuelosRealizadosBinding
import com.helisur.helisurapp.domain.util.BaseActivity
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.mantenimiento.AeronavesViewModel
import com.helisur.helisurapp.ui.mantenimiento.formatos.FormatosViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListaPrevuelosRealizadosActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityListaPrevuelosRealizadosBinding

    var itemList = ArrayList<ObtieneFormatosRealizadosDataTableCloudResponse>()
    var loading: TransparentProgressDialog? = null
    var className = "ListaPrevuelosRealizadosActivity"

    private val formatosViewModel: FormatosViewModel by viewModels()
    private val aeronavesViewModel: AeronavesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaPrevuelosRealizadosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        clicListener()
        observers()
    }

    fun initUI() {
        loading = TransparentProgressDialog(this)
        binding.tvTituloFormato.setText(getNombreFormato(baseContext))
        binding.nombreAeronave.text = getNombreAeronave(baseContext)
        var codFormato = getFormato(baseContext)
        aeronavesViewModel.obtieneEstaciones()
        formatosViewModel.obtieneFormatosRealizados(codFormato!!, "S")
    }


    private fun observers() {

        formatosViewModel.isLoading.observe(this, Observer {
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


        aeronavesViewModel.isLoading.observe(this, Observer {
            try {
                if (it) {
                    if (!loading!!.isShowing) {
                        loading!!.show()
                    }
                } else {
                    if (loading!!.isShowing) {
                        loading!!.dismiss()
                    }
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })

        formatosViewModel.formatosState.observe(this, Observer {
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

        formatosViewModel.responseObtieneFormatosRealizados.observe(this, Observer {
            if (it != null) {

                itemList = ArrayList(it!!.data!!.table)
                setRecyclerView(itemList!!)
            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


    }


    fun getNombreAeronave(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(Constants.SHARED_PREFERENCES.AERONAVE, MODE_PRIVATE)
        val text = sharedPreferences.getString(Constants.SHARED_PREFERENCES.NOMBRE_AERONAVE, "")
        return text
    }

    fun getNombreFormato(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(Constants.SHARED_PREFERENCES.FORMATO, MODE_PRIVATE)
        val text = sharedPreferences.getString(Constants.SHARED_PREFERENCES.NOMBRE_FORMATO, "")
        return text
    }

    fun getFormato(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(Constants.SHARED_PREFERENCES.FORMATO, MODE_PRIVATE)
        val text = sharedPreferences.getString(Constants.SHARED_PREFERENCES.ID_FORMATO, "")
        return text
    }

    fun clicListener() {

        binding.addNewPreVuelo.setOnClickListener {

            val intent = Intent(baseContext, PreVueloActivity::class.java)
            startActivity(intent)

        }

        binding.llBack.setOnClickListener {

            finish()

        }


        binding.llBackEditarFormato.setOnClickListener {

           showDialog()

        }
    }


    fun setRecyclerView(lista: ArrayList<ObtieneFormatosRealizadosDataTableCloudResponse>) {
        val recyclerview = binding.rvFormatosPrevuelo
        recyclerview.layoutManager = LinearLayoutManager(baseContext)
        val adapter = ListaPreVuelosRealizadosAdapter(lista)
        recyclerview.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(baseContext, LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                recyclerview.context,
                R.drawable.divider
            )!!
        )

        recyclerview.addItemDecoration(dividerItemDecoration)

        adapter.onItemClick = { contact ->

            binding.llEditarFormato.visibility = View.VISIBLE
            binding.listaFormatosPendientes.visibility = View.GONE
            binding.addNewPreVuelo.visibility = View.GONE
        }
    }


    private fun showDialog() {
        val dialog = Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.dialog_cerrar_editar_formato)
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogAnimation

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.attributes.alpha = 1f
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        val yesBtn = dialog!!.findViewById<RelativeLayout>(R.id.btnSi)
        yesBtn.setOnClickListener {
             dialog.dismiss();

            binding.llEditarFormato.visibility = View.GONE
            binding.listaFormatosPendientes.visibility = View.VISIBLE
            binding.addNewPreVuelo.visibility = View.VISIBLE
            //dialog.dismiss();

            //         Intent intent = new Intent(getActivity(), ListaPrevuelosRealizadosActivity.class);
            //         startActivity(intent);
        }

        val noBtn = dialog!!.findViewById<RelativeLayout>(R.id.btnNo)
        noBtn.setOnClickListener { dialog!!.dismiss() }

        dialog!!.show()
    }


}