package com.helisur.helisurapp.ui.mantenimiento.formatos


import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.helisur.helisurapp.R
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ActualizaReportajeFormatoCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ActualizaReportajeFormatoDetalleCloudParameter
import com.helisur.helisurapp.databinding.ActivityListaFormatosDiscrBinding
import com.helisur.helisurapp.databinding.ActivityListaPrevuelosRealizadosBinding
import com.helisur.helisurapp.databinding.FragmentEscogeAeronaveBinding
import com.helisur.helisurapp.domain.model.DetalleFormatoRegistro
import com.helisur.helisurapp.domain.model.Estacion
import com.helisur.helisurapp.domain.model.FormatoRegistro
import com.helisur.helisurapp.domain.model.Reportaje
import com.helisur.helisurapp.domain.util.BaseActivity
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.SessionUserManager
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.mantenimiento.AeronavesViewModel
import com.helisur.helisurapp.ui.mantenimiento.formatos.postvuelo.PostVueloActivity
import com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo.PreVueloActivity
import com.helisur.helisurapp.ui.mantenimiento.formatos.spinners.SpinenrItemUbicacion
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FormatosDiscrepanciasFragment : Fragment() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityListaFormatosDiscrBinding

    var listaFormatosRealizados = ArrayList<FormatoRegistro>()
 //   var listaFormatosRealizados = ArrayList<ObtieneFormatosRealizadosDataTableCloudResponse>()
  //  var listaUbicaciones = ArrayList<ObtieneEstacionesDataTableCloudResponse>()
    var listaUbicaciones = ArrayList<Estacion>()
 //   var listaReportajesFormato = ArrayList<ObtieneReportajesFormatoDataTableCloudResponse>()
    var listaReportajesFormato = ArrayList<DetalleFormatoRegistro>()
    var listaReportajes = ArrayList<Reportaje>()
    var idUbicacion = ""
    var loading: TransparentProgressDialog? = null
    var className = "FormatosDiscrepanciasFragment"

    var parameter: ActualizaReportajeFormatoCloudParameter? = null

    var ID_FORMATO_REGISTRO_A_EDITAR = ""

    var idUsuario = ""

    private val formatosViewModel: FormatosViewModel by viewModels()
    private val aeronavesViewModel: AeronavesViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ActivityListaFormatosDiscrBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        clicListener()
        observers()
        return root
    }



    fun initUI() {
        loading = TransparentProgressDialog(requireContext())
      //  binding.tvTituloFormato.setText(getNombreFormato(requireContext()))
      //  binding.tvTituloFormatoeditar!!.setText(getNombreFormato(requireContext()))
     //   binding.nombreAeronave.text = getNombreModeloAeronave(requireContext())

        idUsuario = SessionUserManager(requireContext()).getId().toString()

        var codFormato = getFormato(requireContext())
        //  aeronavesViewModel.getEstacionesListCloud()
        formatosViewModel.getReportajesListDB()
        aeronavesViewModel.getEstacionesListDB()
        formatosViewModel.getFormatosRegistroIncompletedListDB()
      //  formatosViewModel.obtieneFormatosRealizados(codFormato!!, "S")

    //    formatosViewModel.getFormatosRegistroListDB()
      //  formatosViewModel.getDetalleFormatosRegistroListDB()
    }


    private fun observers() {

        formatosViewModel.responseGetFormatosRegistroListDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                listaFormatosRealizados = ArrayList(it)
                setRecyclerViewFormatosRealizados(listaFormatosRealizados!!)
                formatosViewModel.getDetalleFormatosRegistroListDB()

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        formatosViewModel.responseGetDetalleFormatosRegistroListDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                var listaFormatosRealizadosssss : ArrayList<FormatoRegistro> = arrayListOf()


                for(itemFormato in listaFormatosRealizados)
                {
                    var noTieneDefault = false

                    for(itemDetalle in ArrayList(it))
                    {
                        if(itemFormato.id_db.equals(itemDetalle.idRegistroFormatoDB))
                        {

                            for(itemReportaje in listaReportajes)
                            {
                                if(itemDetalle.codigoReportaje.equals(itemReportaje.id_cloud))
                                {
                                    if(itemReportaje.defaultt.equals("0"))
                                    {
                                        listaFormatosRealizadosssss.add(itemFormato)
                                    }
                                }

                            }


                        }
                    }



                }

                setRecyclerViewFormatosRealizados(listaFormatosRealizadosssss!!)


            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        formatosViewModel.isLoading.observe(viewLifecycleOwner, Observer {
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


        aeronavesViewModel.isLoading.observe(viewLifecycleOwner, Observer {
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


        formatosViewModel.formatosState.observe(viewLifecycleOwner, Observer {
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


        formatosViewModel.responseObtieneFormatosRealizados.observe(viewLifecycleOwner, Observer {
            if (it != null) {

               // listaFormatosRealizados = ArrayList(it!!.data!!.table)
                setRecyclerViewFormatosRealizados(listaFormatosRealizados!!)
            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })




        formatosViewModel.responseGetFormatosRegistroIncompletedListDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                listaFormatosRealizados = arrayListOf()
             //   var codFormato = getFormato(requireContext())

                var listaFormatosRealizadosWithoutFilter : ArrayList<FormatoRegistro> = ArrayList(it)

                for(item in listaFormatosRealizadosWithoutFilter)
                {
                 //   if(item.codigoFormato.equals(codFormato))
                //    {
                        listaFormatosRealizados.add(item)
                //    }
                }

                //  listaFormatosRealizados = ArrayList(it)
                setRecyclerViewFormatosRealizados(listaFormatosRealizados!!)
            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        formatosViewModel.responseObtieneReportajesFormato.observe(viewLifecycleOwner, Observer {
            if (it != null) {

            //    listaReportajesFormato = ArrayList(it!!.data!!.table)

                for(reportajeItem in listaReportajesFormato)
                {
                    newCheckBox(reportajeItem.nombreReportaje,reportajeItem.codigoReportaje,binding.llContenedorReportajes!!,reportajeItem.indicadorSN!!,reportajeItem.indicadorBloqueo!!,reportajeItem.nombreTarea!!,reportajeItem.motivoReportaje!!)
                }

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        formatosViewModel.responseGetDetalleFormatosRegistroByFormatoRegistroListDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                listaReportajesFormato = ArrayList(it)

                for(itemReportaje in listaReportajes)
                {
                    for(itemRepoFormato in listaReportajesFormato)
                    {
                        if(itemReportaje.id_cloud.equals(itemRepoFormato.codigoReportaje))
                        {
                            itemRepoFormato.nombreReportaje = itemReportaje.nombreReportaje
                        }
                    }
                }

                for(reportajeItem in listaReportajesFormato)
                {
                  //  if(reportajeItem.indicadorSN.equals("1"))
                 //   {
                        newCheckBox(reportajeItem.nombreReportaje,reportajeItem.codigoReportaje,binding.llContenedorReportajes!!,reportajeItem.indicadorSN!!,reportajeItem.indicadorBloqueo!!,reportajeItem.nombreTarea!!,reportajeItem.motivoReportaje!!)
                 //   }
                }

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        aeronavesViewModel.responseGetEstacionesListCloud.observe(viewLifecycleOwner, Observer {
            if (it != null) {

             //   listaUbicaciones = ArrayList(it!!.data!!.table)

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        aeronavesViewModel.responseGetEstacionListDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                listaUbicaciones = ArrayList(it)

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        formatosViewModel.responseGetReportajeListDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                listaReportajes = ArrayList(it)
               // formatosViewModel.getFormatosRegistroListDB()

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        formatosViewModel.responseUpdateCompleteFormatoRegistroDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {

           //     if(isOnline())
           //     {
            //        saveFormatoRegistroCloud()
            //    }
            //    else
            //    {
                    limpiarYEsconderEdicionFormatoRegistro()
                    listaFormatosRealizados = arrayListOf()
                    formatosViewModel.getFormatosRegistroIncompletedListDB()
            //    }

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        formatosViewModel.responseActualizaFormato.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                limpiarYEsconderEdicionFormatoRegistro()
                listaFormatosRealizados = arrayListOf()
                formatosViewModel.getFormatosRegistroIncompletedListDB()

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        formatosViewModel.responseUpdateFormatoRegistroDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                saveDetalleFormatoRegistroDB()
            } else {
                Log.e(className, Constants.ERROR.ERROR)
            }
        })

        formatosViewModel.responseUpdateDetalleFormatoRegistroDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {


            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


    }


    fun clicListener() {

        binding.addNewPreVuelo.setOnClickListener {
            var format = getNombreFormato(requireContext())
            if(format.equals("POST-VUELO"))
            {
                val intent = Intent(requireContext(), PostVueloActivity::class.java)
                startActivity(intent)
            }
            else
            {
                try {
                    val intent = Intent(requireContext(), PreVueloActivity::class.java)
                    startActivity(intent)
                }
                catch (e:Exception)
                {
                    showErrorDialog(e.toString())
                }

            }

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

        binding.btnGuardarTodo!!.setOnClickListener {

         //   saveFormatoRegistroDB()

            formatosViewModel.updateCompleteFormatoRegistro(ID_FORMATO_REGISTRO_A_EDITAR)

        }
    }


    fun saveFormatoRegistroDB()
    {
        var rtv = binding.etRTV.text.toString()
        var ubicacion = idUbicacion
        formatosViewModel.updateFormatoRegistro(ID_FORMATO_REGISTRO_A_EDITAR,rtv,ubicacion)
    }

    fun saveDetalleFormatoRegistroDB() {
        var contador = 0

        for (item in listaReportajesFormato) {
            if (item.indicadorSN == "0") {
                contador++
                formatosViewModel.updateDetalleFormatoRegistro(item.id_db!!, item.indicadorSN)
            }

        }

        if (contador == listaReportajesFormato.size) {
            formatosViewModel.updateCompleteFormatoRegistro(ID_FORMATO_REGISTRO_A_EDITAR)
        }
        else
        {
            if(isOnline())
            {
                saveFormatoRegistroCloud()
            }
            else
            {
                limpiarYEsconderEdicionFormatoRegistro()
                listaFormatosRealizados = arrayListOf()
                formatosViewModel.getFormatosRegistroIncompletedListDB()
            }


        }
    }


    fun saveFormatoRegistroCloud()
    {
        var rtv = binding.etRTV.text.toString()
        var ubicacion = idUbicacion

        var listaParameter: ArrayList<ActualizaReportajeFormatoDetalleCloudParameter> = arrayListOf()
        for(item in listaReportajesFormato)
        {
            var parameter = ActualizaReportajeFormatoDetalleCloudParameter(item.codigoTarea,item.codigoReportaje,item.indicadorSN,"",idUsuario)
            listaParameter.add(parameter)
        }

        parameter = ActualizaReportajeFormatoCloudParameter(ID_FORMATO_REGISTRO_A_EDITAR,
            rtv,ubicacion,listaParameter)
    }



    fun setRecyclerViewFormatosRealizados(lista: ArrayList<FormatoRegistro>) {
        val recyclerview = binding.rvFormatosPrevuelo
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ListaFormatosDiscrepanciasAdapter(lista,requireContext())
        recyclerview.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                recyclerview.context,
                R.drawable.divider
            )!!
        )

        recyclerview.addItemDecoration(dividerItemDecoration)

        adapter.onItemClick = { formatoRegistro ->

            binding.etRTV.setText(formatoRegistro.numeroRTV)
            setSpinnerUbicacion(formatoRegistro.codigoEstacion)
            idUbicacion = formatoRegistro.codigoEstacion

            binding.tvTituloAeronave.text = getNombreModeloAeronave(requireContext()) + "  /  "+formatoRegistro.nombreAeronave


            var codForma = formatoRegistro.codigoFormato
            var nombreFormato = ""
            if(codForma.equals("00001"))
            {
                nombreFormato = "PRE-VUELO"
            }
            else
            {
                nombreFormato = "POST-VUELO"
            }

            binding.tvTituloFormatoeditar.text = nombreFormato

            //getDetalleFormateRegistro By idFormatoRegistro
            formatosViewModel.getDetalleFormatoRegistroByFormatoRegistroDB(formatoRegistro.id_db!!)
        //    formatosViewModel.obtieneReportajesFormato(formatoRegistro.id_cloud!!)

            ID_FORMATO_REGISTRO_A_EDITAR = formatoRegistro.id_db!!


            binding.llEditarFormato.visibility = View.VISIBLE
            binding.listaFormatosPendientes.visibility = View.GONE
            binding.addNewPreVuelo.visibility = View.GONE
        }
    }



    private fun showDialogCerrarEditarFormato() {
        val dialog = Dialog(requireContext())
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

            limpiarYEsconderEdicionFormatoRegistro()

        }

        val noBtn = dialog!!.findViewById<RelativeLayout>(R.id.btnNo)
        noBtn.setOnClickListener { dialog!!.dismiss() }

        dialog!!.show()
    }

    fun limpiarYEsconderEdicionFormatoRegistro()
    {
        binding.llEditarFormato.visibility = View.GONE
        binding.listaFormatosPendientes.visibility = View.VISIBLE
     //   binding.addNewPreVuelo.visibility = View.VISIBLE

        binding.llContenedorReportajes.removeAllViews()
        listaReportajesFormato = arrayListOf()
        binding.etRTV.setText("")

        ID_FORMATO_REGISTRO_A_EDITAR = ""
        //  binding.spiUbicacion.adapter = null
    }


    fun getNombreModeloAeronave(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(Constants.SHARED_PREFERENCES.AERONAVE, MODE_PRIVATE)
        val text = sharedPreferences.getString(Constants.SHARED_PREFERENCES.NOMBRE_MODELO_AERONAVE, "")
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
            spinnerArray.add(item.nombre!!)
            spinnerArrayImages.add(R.drawable.ic_location)
        }

        val adapter =
            SpinenrItemUbicacion(
                requireContext(), 0,
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



        var conteo = 0
        for(item in listaUbicaciones)
        {
            if(item.id_cloud.equals(codEstacion))
            {
                binding.spiUbicacion.setSelection(conteo+1)
            }

            conteo++
        }



    }


    fun newCheckBox(nombre:String,id:String,contenedor:LinearLayout,indicadorSN:String,indicadorBloqueo:String,nombreTarea:String,motivoReportaje:String)
    {

        val tituloTarea = TextView(requireContext())
        tituloTarea.setText("\n"+"   -   "+nombreTarea)

        tituloTarea.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

        val tabletSize = resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {
            tituloTarea.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.nombretarea_formatos_realizados))
       //     tituloTarea.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23f)
        } else {
            tituloTarea.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.nombretarea_formatos_realizados_cel))
        //    tituloTarea.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        }

        val tituloMotivo = TextView(requireContext())
        tituloMotivo.setText("  Motivo : "+motivoReportaje)
        tituloMotivo.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        tituloMotivo.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.motivo_formatos_realizados))

        val cb = CheckBox(requireContext())
        cb.setTextColor( resources.getColor(R.color.texto_simple_pantalla_general))


        val tabletSizee = resources.getBoolean(R.bool.isTablet)
        if (tabletSizee) {
            cb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
        } else {
            cb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        }

        CompoundButtonCompat.setButtonTintList(cb, ColorStateList.valueOf(getResources().getColor(R.color.titulo_pantalla_general)))
     //   CompoundButtonCompat.setButtonTintList(tituloTarea, ColorStateList.valueOf(getResources().getColor(R.color.titulo_pantalla_general)))

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

     //   tituloTarea.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23f);

      //  val param = cb.layoutParams as ViewGroup.MarginLayoutParams
      //  param.setMargins(0,10,0,0)
      //  cb.layoutParams = param

        contenedor.addView(tituloTarea)
        contenedor.addView(cb)
        contenedor.addView(tituloMotivo)



        cb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {

                val nombre = cb.text
                val idReportaje = cb.tag

                for(item in listaReportajesFormato)
                    {
                    if(item.codigoReportaje.equals(idReportaje))
                    {
                        item.indicadorSN = "1"
                    }
                }

            } else {
                val nombre = cb.text
                val idReportaje = cb.tag

                for(item in listaReportajesFormato)
                {
                    if(item.codigoReportaje.equals(idReportaje))
                    {
                        item.indicadorSN = "0"
                    }
                }
            }

            }
    }

    fun isOnline(): Boolean {
        try {
            val p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com")
            val returnVal = p1.waitFor()
            val reachable = (returnVal == 0)
            return reachable
        } catch (e: Exception) {
            //  e.printStackTrace();
        }
        return false
    }


    fun showErrorDialog(message: String?) {
        val bundle = Bundle()
        bundle.putString("errorMessage", message)
        val df: ErrorMessageDialog = ErrorMessageDialog()
        df.setArguments(bundle)
        df.show(requireFragmentManager(), "")
    }


}