package com.helisur.helisurapp.ui.hojaruta

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView

import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.helisur.helisurapp.R
import com.helisur.helisurapp.data.cloud.hojaruta.model.response.ObtieneHojasRutaDataTableCloudResponse
import com.helisur.helisurapp.databinding.ActivityListaHojaRutaBinding
import com.helisur.helisurapp.domain.model.Aeronave
import com.helisur.helisurapp.domain.model.Estacion
import com.helisur.helisurapp.domain.model.ModeloAeronave

import com.helisur.helisurapp.domain.util.BaseActivity
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.SessionUserManager.Companion.USER
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.mantenimiento.AeronavesViewModel
import com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo.PreVueloTabsFragment
import com.helisur.helisurapp.ui.mantenimiento.formatos.spinners.SpinenrItemAeronave
import com.helisur.helisurapp.ui.mantenimiento.formatos.spinners.SpinenrItemUbicacion

import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class ListaHojaRutaActivity  : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityListaHojaRutaBinding
    var loading: TransparentProgressDialog? = null
    var className = "ListaHojaRutaActivity"
    var listaHojasRuta = ArrayList<ObtieneHojasRutaDataTableCloudResponse>()
    var listaUbicaciones = ArrayList<Estacion>()
    private var modelosAeronavesList: ArrayList<ModeloAeronave>? = null
    private var aeronavesList: ArrayList<Aeronave>? = null

    private val hojasRutaViewModel: HojaRutaViewModel by viewModels()
    private val aeronavesViewModel: AeronavesViewModel by viewModels()

    private var idModeloAeronave:String = ""
    private var nombreModeloAeronave:String = ""

    private var idAeronave:String = ""
    private var idUbicacion:String = ""
    private var fechaInicio:String = ""
    private var fechaFin:String = ""

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
        aeronavesViewModel.getModelosAeronavesListDB()
        aeronavesViewModel.getEstacionesListDB()
    }


    fun clicListener()
    {

        binding.ivFilter.setOnClickListener {
            showDialogFiltros()
        }
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


        aeronavesViewModel.responseGetEstacionListDB.observe(this, Observer {
            if (it != null) {

                listaUbicaciones = ArrayList(it)

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        aeronavesViewModel.responseGetModeloAeronaveListDB.observe(this, Observer {
            try {
                if (it != null) {
                    modelosAeronavesList = ArrayList(it.data!!)

                } else {
                    Log.e(className, Constants.ERROR.ERROR)
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })

        aeronavesViewModel.responseGetAeronaveListDB.observe(this, Observer {
            try {
                if (it != null) {
                    aeronavesList = ArrayList(it)

                } else {
                    Log.e(className, Constants.ERROR.ERROR)
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
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


    private fun showDialogFiltros() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_filtros_hoja_ruta)
        dialog.getWindow()!!.getAttributes().windowAnimations = R.style.DialogAnimation

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.attributes.alpha = 1f
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val btnAplicarFiltro = dialog.findViewById(R.id.btnAplicarFiltro) as RelativeLayout
        val btnCerrar = dialog.findViewById(R.id.btnCerrar) as RelativeLayout
        val spiModeloAeronave = dialog.findViewById(R.id.spiModeloAeronave) as Spinner
        val spiAeronave = dialog.findViewById(R.id.spiAeronave) as Spinner
        val spiEstacion = dialog.findViewById(R.id.spiEstacion) as Spinner

        val tvFechaInicio = dialog.findViewById(R.id.tvFechaInicio) as TextView
        val tvFechaFin = dialog.findViewById(R.id.tvFechaFin) as TextView

        setSpinnerModeloAeronave(spiModeloAeronave,spiAeronave)
        setSpinnerUbicacion(spiEstacion)

        tvFechaInicio.setOnClickListener {
            loadDatePickerInicio(tvFechaInicio)
        }

        tvFechaFin.setOnClickListener {
            loadDatePickerFin(tvFechaFin)
        }

        btnAplicarFiltro.setOnClickListener {
            dialog.dismiss()
        }

        btnCerrar.setOnClickListener {
            dialog.dismiss()
        }


        dialog.show()
    }

    val myCalendar: Calendar = Calendar.getInstance()

    private fun loadDatePickerInicio(tvFechaInicio: TextView) {
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabelFechaInicio(tvFechaInicio)
        }

        DatePickerDialog(
            this,
            date,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateLabelFechaInicio(tvFechaInicio: TextView) {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        tvFechaInicio.setText(sdf.format(myCalendar.time))
        fechaInicio = sdf.format(myCalendar.time)
    }



    private fun loadDatePickerFin(tvFechaFin: TextView) {
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabelFechaFin(tvFechaFin)
        }

        DatePickerDialog(
            this,
            date,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateLabelFechaFin(tvFechaFin: TextView) {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        tvFechaFin.setText(sdf.format(myCalendar.time))
        fechaFin = sdf.format(myCalendar.time)
    }


    fun setSpinnerModeloAeronave(spinnerModeloAeronave:Spinner,spinnerAeronave:Spinner
    ) {
        var spinnerTipo = spinnerModeloAeronave
        val spinnerArray: MutableList<String> = ArrayList()
        val spinnerArrayImages: MutableList<Int> = ArrayList()
        spinnerArray.add("Seleccione modelo aeronave")
        spinnerArrayImages.add(R.drawable.empty)
        //    spinnerArray.add("Seleccione modelo")
        //    spinnerArrayImages.add(R.drawable.ic_down)

        for(item in modelosAeronavesList!!)
        {
            var itemName = item.nombre!!
            spinnerArray.add(itemName)

            if(itemName.contains("MI"))
            {
                spinnerArrayImages.add(R.drawable.img_mi8)
            }
            else
            {
                if(itemName.contains("BK"))
                {
                    spinnerArrayImages.add(R.drawable.img_bk117)
                }
                else
                {
                    if(itemName.contains("BELL"))
                    {
                        spinnerArrayImages.add(R.drawable.img_bell412)
                    }
                    else
                    {
                        //    spinnerArrayImages.add(R.drawable.img_bell412)
                    }
                }
            }
        }

        val adapter =
            SpinenrItemAeronave(
                baseContext, 0,
                spinnerArray.toTypedArray(), spinnerArrayImages.toTypedArray()
            )
        //    val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, spinnerArray)
        //   adapter.setDropDownViewResource(R.layout.spinner_item)
        spinnerTipo.adapter = adapter

        spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {

                if (position == 0) {
                    idModeloAeronave = ""
                    nombreModeloAeronave = ""
                } else {

                    idModeloAeronave = modelosAeronavesList!![position-1].id_cloud!!
                    nombreModeloAeronave = modelosAeronavesList!![position-1].nombre!!
                    setSpinnerAeronave(spinnerAeronave)
                }


            }
        }
    }




    fun setSpinnerAeronave(spinner:Spinner
    ) {
        var spinnerTipo = spinner
        val spinnerArrayImages: MutableList<Int> = ArrayList()
        val spinnerArray: MutableList<String> = ArrayList()
        spinnerArray.add("Seleccione aeronave")
        spinnerArrayImages.add(R.drawable.empty)
        for(item in modelosAeronavesList!!) {

            var itemName = nombreModeloAeronave
            spinnerArray.add(item.nombre!!)

            if(itemName!!.contains("MI"))
            {
                spinnerArrayImages.add(R.drawable.img_mi8)
            }
            else
            {
                if(itemName.contains("BK"))
                {
                    spinnerArrayImages.add(R.drawable.img_bk117)
                }
                else
                {
                    if(itemName.contains("BELL"))
                    {
                        spinnerArrayImages.add(R.drawable.img_bell412)
                    }
                    else
                    {
                        //    spinnerArrayImages.add(R.drawable.img_bell412)
                    }
                }
            }

        }

        //   val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, spinnerArray)
        val adapter =
            SpinenrItemAeronave(
                baseContext, 0,
                spinnerArray.toTypedArray(), spinnerArrayImages.toTypedArray()
            )
        //   adapter.setDropDownViewResource(R.layout.spinner_item_aeronave)

        spinnerTipo.adapter = adapter

        spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                if (position == 0) {
                    idAeronave = ""

                } else {
                    idAeronave = aeronavesList!![position-1].codigoModeloPuesto

                }
            }
        }



    }



    fun setSpinnerUbicacion(spinner:Spinner
    ) {
        var spinnerTipo = spinner
        val spinnerArray: MutableList<String> = ArrayList()
        val spinnerArrayImages: MutableList<Int> = ArrayList()
        spinnerArray.add("Seleccione ubicación")
        spinnerArrayImages.add(R.drawable.empty)
        for(item in listaUbicaciones!!)
        {
            spinnerArray.add(item.nombre!!)
            spinnerArrayImages.add(R.drawable.ic_location)
        }

        val adapter =
            SpinenrItemUbicacion(
                baseContext, 0,
                spinnerArray.toTypedArray(), spinnerArrayImages.toTypedArray()
            )

        //   val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, spinnerArray)
        //   adapter.setDropDownViewResource(R.layout.spinner_item)
        spinnerTipo.adapter = adapter

        spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                if (position == 0) {
                    idUbicacion = ""
                } else {
                    idUbicacion = listaUbicaciones!![position-1].id_cloud!!

                }
            }
        }

    }




}