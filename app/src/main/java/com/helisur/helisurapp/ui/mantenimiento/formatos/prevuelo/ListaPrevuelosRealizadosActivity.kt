package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.lifecycle.Observer
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.helisur.helisurapp.R
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneEstacionesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosRealizadosDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneReportajesFormatoDataTableCloudResponse
import com.helisur.helisurapp.databinding.ActivityListaPrevuelosRealizadosBinding
import com.helisur.helisurapp.domain.util.BaseActivity
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.SessionUserManager
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.mantenimiento.AeronavesViewModel
import com.helisur.helisurapp.ui.mantenimiento.formatos.FormatosViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


@AndroidEntryPoint
class ListaPrevuelosRealizadosActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityListaPrevuelosRealizadosBinding

    var listaFormatosRealizados = ArrayList<ObtieneFormatosRealizadosDataTableCloudResponse>()
    var listaUbicaciones = ArrayList<ObtieneEstacionesDataTableCloudResponse>()
    var listaReportajesFormato = ArrayList<ObtieneReportajesFormatoDataTableCloudResponse>()
    var idUbicacion = ""
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
     //   listaReportajes(baseContext,"0000000002")
    }


    fun listaReportajes(ctx:Context,idTarea:String) {

        var sessionUserManager = SessionUserManager(context = ctx)
        //  val idDevice = Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID)
        //  var usuario = sessionUserManager.getUser()!!
        //   var pass = sessionUserManager.getPass()!!

        var urlApi = "http://38.199.4.100:81/api/FormatoRegistro/GetFormatoAnotaciones"
        val payload = "{'cadena': '"+idTarea+"'}"

        val okHttpClient = OkHttpClient()
        val requestBody = payload.toRequestBody()

        val request = Request.Builder()
            .post(requestBody)
            .url(urlApi)
            .header("Content-Type", "application/json")
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ERROR LOGIN TOKEN",e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                var responseData = response.body!!.string()
                try {
                    var json = JSONObject(responseData)
                    Log.i("JSON RESPONSE: ", json.toString())
                    val responseObject = json.getJSONObject("data")
                    val contArray = responseObject.getJSONObject("obj")
                    val arrayReportajes = contArray.getJSONArray("table")

                    for(i in 0 until arrayReportajes.length())
                    {
                        val itemReportaje = arrayReportajes.getJSONObject(i)

                        val codigoRegistroFormato = itemReportaje.getJSONObject("codigoRegistroFormato")
                        val codigoTarea = itemReportaje.getJSONObject("codigoTarea")
                        val nombreTarea = itemReportaje.getJSONObject("nombreTarea")

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })


    }



    fun initUI() {
        loading = TransparentProgressDialog(this)
        binding.tvTituloFormato.setText(getNombreFormato(baseContext))
        binding.tvTituloFormatoeditar!!.setText(getNombreFormato(baseContext))
        binding.nombreAeronave.text = getNombreAeronave(baseContext)

        var codFormato = getFormato(baseContext)
        aeronavesViewModel.getEstacionesListCloud()
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

                listaFormatosRealizados = ArrayList(it!!.data!!.table)
                setRecyclerViewFormatosRealizados(listaFormatosRealizados!!)
            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        formatosViewModel.responseObtieneReportajesFormato.observe(this, Observer {
            if (it != null) {

                listaReportajesFormato = ArrayList(it!!.data!!.table)

                for(reportajeItem in listaReportajesFormato)
                {
                    newCheckBox(reportajeItem.nombreReportaje,reportajeItem.codigoReportaje,binding.llContenedorReportajes!!,reportajeItem.indicadorSN!!,reportajeItem.indicadorBloqueo!!,reportajeItem.nombreTarea!!)
                }

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        aeronavesViewModel.responseGetEstacionesListCloud.observe(this, Observer {
            if (it != null) {

                listaUbicaciones = ArrayList(it!!.data!!.table)

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


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
            showDialogCerrarEditarFormato()
        }

        binding.btnDocumentacionAeronaves!!.setOnClickListener {

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://airbusworld.helicopters.airbus.com/c/portal/login?redirect=%2Fgroup%2Fguest&refererPlid=13195661&p_l_id=13195493"))
            startActivity(browserIntent)
        }

        binding.btnDocumentacionMotores!!.setOnClickListener {

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.intermountainturbine.com/"))
            startActivity(browserIntent)
        }
    }


    fun setRecyclerViewFormatosRealizados(lista: ArrayList<ObtieneFormatosRealizadosDataTableCloudResponse>) {
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

        adapter.onItemClick = { formato ->

            binding.etRTV.setText(formato.numeroRTV)
            setSpinnerUbicacion(formato.codigoEstacion)

            binding.tvTituloAeronave.text = getNombreAeronave(baseContext) + "  /  "+formato.aeronave

            formatosViewModel.obtieneReportajesFormato(formato.id)

            binding.llEditarFormato.visibility = View.VISIBLE
            binding.listaFormatosPendientes.visibility = View.GONE
            binding.addNewPreVuelo.visibility = View.GONE
        }
    }



    private fun showDialogCerrarEditarFormato() {
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

            binding.llContenedorReportajes.removeAllViews()
            listaReportajesFormato = arrayListOf()
            binding.etRTV.setText("")
          //  binding.spiUbicacion.adapter = null
        }

        val noBtn = dialog!!.findViewById<RelativeLayout>(R.id.btnNo)
        noBtn.setOnClickListener { dialog!!.dismiss() }

        dialog!!.show()
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


    fun setSpinnerUbicacion(codEstacion:String
    ) {
        var spinnerTipo = binding.spiUbicacion
        val spinnerArray: MutableList<String> = ArrayList()
        val spinnerArrayImages: MutableList<Int> = ArrayList()
        spinnerArray.add("Seleccione ubicación")
        spinnerArrayImages.add(R.drawable.empty)
        for(item in listaUbicaciones!!)
        {
            spinnerArray.add(item.nombre)
            spinnerArrayImages.add(R.drawable.ic_location)
        }

        val adapter = SpinenrItemUbicacion(baseContext,0,
            spinnerArray.toTypedArray(), spinnerArrayImages.toTypedArray())

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
                    idUbicacion = listaUbicaciones!![position-1].id

                }
            }
        }



        var conteo = 0
        for(item in listaUbicaciones)
        {
            if(item.id.equals(codEstacion))
            {
                binding.spiUbicacion.setSelection(conteo+1)
            }

            conteo++
        }



    }


    fun newCheckBox(nombre:String,id:String,contenedor:LinearLayout,indicadorSN:String,indicadorBloqueo:String,nombreTarea:String)
    {

        val tituloTarea = TextView(applicationContext)
        tituloTarea.setText(nombreTarea)

        val cb = CheckBox(applicationContext)
        cb.setTextColor( resources.getColor(R.color.texto_simple_pantalla_general))


        val tabletSize = resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {
            cb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);
        } else {
            cb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
        }

        CompoundButtonCompat.setButtonTintList(cb, ColorStateList.valueOf(getResources().getColor(R.color.titulo_pantalla_general)))

        if(indicadorBloqueo.equals("0"))
        {
            cb.isEnabled = true
        }
        else
        {
            if(indicadorBloqueo.equals("1"))
            {
                cb.isEnabled = false
            }
            else
            {
                cb.isEnabled = true
            }
        }


        if(indicadorSN.equals("0"))
        {
            cb.isChecked = false
        }
        else
        {
            if(indicadorSN.equals("1"))
            {
                cb.isChecked = true
            }
            else
            {
                cb.isChecked = false
            }
        }


        cb.text = nombre
        cb.tag = id!!
       // cb.setTag(1,id)
      //  cb.setTag(2,)

        tituloTarea.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23f);

      //  val param = cb.layoutParams as ViewGroup.MarginLayoutParams
      //  param.setMargins(0,10,0,0)
      //  cb.layoutParams = param

        contenedor.addView(tituloTarea)
        contenedor.addView(cb)



        cb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {

                val nombre = cb.text
                val idReportaje = cb.tag

            } else {
                val nombre = cb.text
                val idReportaje = cb.tag
            }

            }
    }

}