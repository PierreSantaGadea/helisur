package com.helisur.helisurapp.ui.mantenimiento.formatos.postvuelo


import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.helisur.helisurapp.R
import com.helisur.helisurapp.databinding.FragmentDatosAeronaveBinding
import com.helisur.helisurapp.domain.model.Aeronave
import com.helisur.helisurapp.domain.model.DetalleFormatoRegistro
import com.helisur.helisurapp.domain.model.Estacion
import com.helisur.helisurapp.domain.model.FormatoRegistro
import com.helisur.helisurapp.domain.model.Reportaje
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.mantenimiento.AeronavesViewModel
import com.helisur.helisurapp.ui.mantenimiento.formatos.FormatosViewModel
import com.helisur.helisurapp.ui.mantenimiento.formatos.spinners.SpinenrItemAeronave
import com.helisur.helisurapp.ui.mantenimiento.formatos.spinners.SpinenrItemUbicacion
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PostVueloDatosAeronaveFragment : Fragment() {

    var className = "ConcursosAvancesFragment"
    private val aeronavesViewModel: AeronavesViewModel by viewModels()
    private lateinit var binding: FragmentDatosAeronaveBinding
    var loading: TransparentProgressDialog? = null
 //   private var modelosAeronavesList: ArrayList<ObtieneModelosAeronaveDataTableCloudResponse>? = null
    private var modelosAeronavesList: ArrayList<Aeronave>? = null
  //  private var estacionesList: ArrayList<ObtieneEstacionesDataTableCloudResponse>? = null
    private var estacionesList: ArrayList<Estacion>? = null


    private var formatoRegistroList: ArrayList<FormatoRegistro>? = null
    private var detalleFormatoRegistroList: ArrayList<DetalleFormatoRegistro>? = null


    private var listaAnotacionesFormato: ArrayList<DetalleFormatoRegistro>? = null

    var adapter : PostVueloListaAnotacionesAdapter? = null


    private val formatoViewModel: FormatosViewModel by viewModels()

    var idAeronave:String = ""
    var idUbicacion:String = ""

    var showDetalleCamapana = false
    private var listaReportajes: ArrayList<Reportaje>? = null

    var nombreAeronave = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDatosAeronaveBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        observers()
        clickListener()
        return root
    }

    fun initUI() {
        loading = TransparentProgressDialog(requireContext())
        binding.rlDiscrepancias!!.setBackgroundResource(R.drawable.shape_control_disabled)
        binding.etDiscrepancias!!.isEnabled = false
        var idAeronave = getModeloAeronave(requireContext())
      //  aeronavesViewModel.getAeronaveListCloud(idAeronave!!)
        aeronavesViewModel.getAeronavesByModeloDB(idAeronave!!)
      //  aeronavesViewModel.getEstacionesListCloud()
        aeronavesViewModel.getEstacionesListDB()
        setCheckBox()
        editTextEvent()

        PostVueloTabsFragment.formatoParameter.existenDiscrepancias = "0"
        PostVueloTabsFragment.formatoParameter.accionesMantenimiento = "0"
        PostVueloTabsFragment.formatoParameter.solicitaEncMotores = "0"

        formatoViewModel.getReportajesListDB()
        formatoViewModel.getFormatosRegistroListDB()
        formatoViewModel.getDetalleFormatosRegistroListDB()
    }


    fun editTextEvent()
    {
        binding.etRTV!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int) {}
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length >= 1) {
                    PostVueloTabsFragment.formatoParameter.numeroRTV = s.toString()
                }
            }
        })


        binding.etDiscrepancias!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int) {}
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length >= 1) {
                    PostVueloTabsFragment.formatoParameter.numeroRTVDiscrepancias = s.toString()
                }
            }
        })

    }

    fun clickListener()
    {
        binding.tvSiguiente.setOnClickListener {


            if(listaAnotacionesFormato!=null)
            {
                if(listaAnotacionesFormato!!.size==0)
                {
                    if(validationsNextScreen())
                    {
                        PostVueloTabsFragment.viewPager.setCurrentItem(Constants.TABS_PRE_VUELO.SISTEMAS)
                    }
                }
                else
                {
                    var tieneRTV = false
                    for(itemAnotacion in listaAnotacionesFormato!!)
                    {
                        if(itemAnotacion.nombreReportaje.contains("RTV"))
                        {
                            tieneRTV = true
                        }
                    }
                    if(tieneRTV)
                    {
                        showDialogCierreDiscrepancias("La aeronave "+nombreAeronave+ " tiene discrepancias con RTV")
                    }
                    else
                    {
                        if(validationsNextScreen())
                        {
                            PostVueloTabsFragment.viewPager.setCurrentItem(Constants.TABS_PRE_VUELO.SISTEMAS)
                        }
                    }

                }
            }
            else
            {
                if(validationsNextScreen())
                {
                    PostVueloTabsFragment.viewPager.setCurrentItem(Constants.TABS_PRE_VUELO.SISTEMAS)
                }
            }


        }

        binding.tvDocumentacion!!.setOnClickListener {

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://airbusworld.helicopters.airbus.com/c/portal/login?redirect=%2Fgroup%2Fguest&refererPlid=13195661&p_l_id=13195493"))
            startActivity(browserIntent)
        }


        binding.llContenedorConteoDiscrepancias!!.setOnClickListener {

            setRecyclerViewAnot(listaAnotacionesFormato!!)

            if(showDetalleCamapana)
            {
                binding.lldetalleCampana!!.visibility = View.GONE
                showDetalleCamapana = false

            }
            else
            {

                binding.lldetalleCampana!!.visibility = View.VISIBLE
                showDetalleCamapana = true
            }

        }


        binding.ivCerrarDetalle!!.setOnClickListener {

            binding.lldetalleCampana!!.visibility = View.GONE
            showDetalleCamapana = false

        }


    }

    fun setRecyclerViewAnot(lista: ArrayList<DetalleFormatoRegistro>) {
        val recyclerview = binding.rvAnotacionessPostVuelo
        recyclerview!!.layoutManager = LinearLayoutManager(requireContext())
        val adapter = PostVueloListaAnotacionesAdapter(lista)
        recyclerview.adapter = adapter

        adapter.onItemClick = { formatoRegistro ->

        }
    }



    private fun showDialogCierreDiscrepancias(message:String) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_cierre_discrepancias)
        dialog.getWindow()!!.getAttributes().windowAnimations = R.style.DialogAnimation

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.attributes.alpha = 1f
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        val tvMensaje = dialog.findViewById(R.id.tvMensaje) as TextView
        tvMensaje.setText(message)

        val yesBtn = dialog.findViewById(R.id.btnCerrar) as RelativeLayout
        yesBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    fun validationsNextScreen():Boolean
    {
        var isOk = true

        if(idAeronave.equals(""))
        {
            showErrorDialog("Escoja aeronave")
            isOk = false
        }
        else
        {
            if(binding.etRTV!!.text.toString().trim().equals(""))
            {
                showErrorDialog("Ingrese RTV")
                isOk = false
            }
            else
            {
                if(idUbicacion.equals(""))
                {
                    showErrorDialog("Escoja ubicación")
                    isOk = false
                }
                else
                {
                    isOk = true
                }
            }
        }


        return isOk
    }

    fun setCheckBox() {

        binding.chxDiscrepancias!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.rlDiscrepancias!!.setBackgroundResource(R.drawable.shape_text_box)
                binding.etDiscrepancias!!.isEnabled = true
                PostVueloTabsFragment.formatoParameter.existenDiscrepancias = "1"
            } else {
                binding.rlDiscrepancias!!.setBackgroundResource(R.drawable.shape_control_disabled)
                binding.etDiscrepancias!!.isEnabled = false
                PostVueloTabsFragment.formatoParameter.existenDiscrepancias = "0"
            }
        }

        binding.chbxAccionesMantenimiento!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                PostVueloTabsFragment.formatoParameter.accionesMantenimiento = "1"
            } else {
                PostVueloTabsFragment.formatoParameter.accionesMantenimiento = "0"
            }
        }


        binding.chbxSolicitaEncendidoPrevio!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                PostVueloTabsFragment.formatoParameter.solicitaEncMotores = "1"
            } else {
                PostVueloTabsFragment.formatoParameter.solicitaEncMotores = "0"
            }
        }

    }


    fun getModeloAeronave(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES.AERONAVE, MODE_PRIVATE)
        val text = sharedPreferences.getString(Constants.SHARED_PREFERENCES.ID_MODELO_AERONAVE, "")
        return text
    }

    fun getNombreModeloAeronave(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES.AERONAVE, MODE_PRIVATE)
        val text = sharedPreferences.getString(Constants.SHARED_PREFERENCES.NOMBRE_MODELO_AERONAVE, "")
        return text
    }


    fun getFormato(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES.FORMATO, MODE_PRIVATE)
        val text = sharedPreferences.getString(Constants.SHARED_PREFERENCES.ID_FORMATO, "")
        return text
    }

    fun getNombreFormato(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES.FORMATO, MODE_PRIVATE)
        val text = sharedPreferences.getString(Constants.SHARED_PREFERENCES.NOMBRE_FORMATO, "")
        return text
    }



    fun setSpinnerAeronave(
    ) {
        var spinnerTipo = binding.spiAeronave
        val spinnerArrayImages: MutableList<Int> = ArrayList()
        val spinnerArray: MutableList<String> = ArrayList()
        spinnerArray.add("Seleccione aeronave")
        spinnerArrayImages.add(R.drawable.empty)
           for(item in modelosAeronavesList!!) {

               var itemName = getNombreModeloAeronave(requireContext())
               spinnerArray.add(item.nombre)

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
                requireContext(), 0,
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
                    nombreAeronave = ""
                    listaAnotacionesFormato = arrayListOf()
                    PostVueloTabsFragment.formatoParameter.codigoPuestoTecnico =  ""
                } else {
                    idAeronave = modelosAeronavesList!![position-1].codigoModeloPuesto
                    nombreAeronave  = modelosAeronavesList!![position-1].nombre
                    listaAnotacionesFormato = arrayListOf()
                    PostVueloTabsFragment.formatoParameter.codigoPuestoTecnico =  modelosAeronavesList!![position-1].codigoModeloPuesto
                    saveAeronave(requireContext(),idAeronave,modelosAeronavesList!![position-1].nombre)
               //     aeronavesViewModel.getCountDetallessByAeronave(idAeronave)
                 //   if(getFormato(requireContext()).equals("00001"))
                  //  {
                        var conteoDiscre:Int = conteoDiscrepancias2(idAeronave,getFormato(requireContext())!!)
                      //  var conteoDiscre:Int = conteoDiscrepancias(idAeronave)
                       if(conteoDiscre==0)
                       {
                           binding.llContenedorConteoDiscrepancias!!.visibility = View.GONE
                       }
                        else
                       {
                           binding.llContenedorConteoDiscrepancias!!.visibility = View.VISIBLE
                           binding.tvConteoDiscrepancias!!.setText(conteoDiscre.toString())
                       }
                  //  }


                }
            }
        }



    }

    fun saveAeronave(context: Context, idAeronave:String, nombreAeronave:String) {
        val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES.AERONAVE, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.SHARED_PREFERENCES.ID_AERONAVE, idAeronave)
        editor.putString(Constants.SHARED_PREFERENCES.NOMBRE_AERONAVE, nombreAeronave)
        editor.apply()
    }


    fun setSpinnerUbicacion(
    ) {
        var spinnerTipo = binding.spiUbicacion
        val spinnerArray: MutableList<String> = ArrayList()
        val spinnerArrayImages: MutableList<Int> = ArrayList()
        spinnerArray.add("Seleccione ubicación")
        spinnerArrayImages.add(R.drawable.empty)
           for(item in estacionesList!!)
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
                    PostVueloTabsFragment.formatoParameter.codigoEstacion = ""
                } else {
                    idUbicacion = estacionesList!![position-1].id_cloud!!
                    PostVueloTabsFragment.formatoParameter.codigoEstacion =  estacionesList!![position-1].id_cloud!!
                }
            }
        }
    }


    private fun observers()
    {

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

        aeronavesViewModel.aeronavesState.observe(viewLifecycleOwner, Observer {
            try {
                if (it.toString().contains(Constants.ERROR.SUCCESS)) {
                } else {
                    if (it.toString().contains(Constants.ERROR.FAILURE)) {
                        var messageError = it.toString().replace("FAILURE","ERROR")
                        Log.e(className,messageError )
                        showErrorDialog(messageError)
                    }
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })

        aeronavesViewModel.responseGetAeronaveListCloud.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                //    modelosAeronavesList = ArrayList(it.data!!.table)
               //     binding.rlAeronave!!.setBackgroundResource(R.drawable.shape_text_box)
                    setSpinnerAeronave()
                } else {
                    Log.e(className, Constants.ERROR.ERROR)
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })


        aeronavesViewModel.responseGetAeronaveByModeloListDB.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    modelosAeronavesList = ArrayList(it)
                    //     binding.rlAeronave!!.setBackgroundResource(R.drawable.shape_text_box)
                    setSpinnerAeronave()
                } else {
                    Log.e(className, Constants.ERROR.ERROR)
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })

        formatoViewModel.responseGetReportajeListDB.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    listaReportajes = ArrayList(it)
                } else {
                    Log.e(className, Constants.ERROR.ERROR)
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })


        aeronavesViewModel.responseGetEstacionesListCloud.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                 //   estacionesList = ArrayList(it.data!!.table)
                    //     binding.rlAeronave!!.setBackgroundResource(R.drawable.shape_text_box)
                    setSpinnerUbicacion()
                } else {
                    Log.e(className, Constants.ERROR.ERROR)
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })


        aeronavesViewModel.responseGetEstacionListDB.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    estacionesList = ArrayList(it)
                    //     binding.rlAeronave!!.setBackgroundResource(R.drawable.shape_text_box)
                    setSpinnerUbicacion()
                } else {
                    Log.e(className, Constants.ERROR.ERROR)
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })



        aeronavesViewModel.responseCountDiscrepancias.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {

                    var cuenta = it
                } else {
                    Log.e(className, Constants.ERROR.ERROR)
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })


        formatoViewModel.responseGetFormatosRegistroListDB.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    formatoRegistroList = ArrayList(it)
                } else {
                    Log.e(className, Constants.ERROR.ERROR)
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })


        formatoViewModel.responseGetDetalleFormatosRegistroListDB.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    detalleFormatoRegistroList = ArrayList(it)
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


    fun conteoDiscrepancias(aeronaveCodPuestoTecnico:String):Int
    {

        var ultimoFormatoRegistroPostVuelo:FormatoRegistro? = null

        for(item in formatoRegistroList!!)
        {
            if(item.codigoPuestoTecnico.equals(aeronaveCodPuestoTecnico))
            {
                if(item.codigoFormato.equals("00002"))
                {
                    ultimoFormatoRegistroPostVuelo = item
                }

            }
            //obtengo el ultimo que tenga  el formato postvuelo y que tenga la aeronave
        }

        //una vez q tengo el ultimo de registroformato me voy al detalle

        if(ultimoFormatoRegistroPostVuelo!=null)
        {
            var listaAVer:ArrayList<DetalleFormatoRegistro>? = arrayListOf()

            for(item in detalleFormatoRegistroList!!)
            {
                if(item.idRegistroFormatoDB.equals(ultimoFormatoRegistroPostVuelo!!.id_db))
                {
                    listaAVer!!.add(item)
                }
                //obtengo la cantidad que tienen eel id que obtuve en el buicle anterior
            }

            return listaAVer!!.count()

        }
        else{
            return 0
        }






    }

    fun conteoDiscrepancias2(aeronaveCodPuestoTecnico:String,codFormato:String):Int
    {

        var formatosConDiscrepancias:ArrayList<FormatoRegistro> = arrayListOf()

        for(item in formatoRegistroList!!)
        {
            if(item.codigoPuestoTecnico.equals(aeronaveCodPuestoTecnico))
            {
                if(item.codigoFormato.equals(codFormato))
                {
                    if(!item.completadado!!)
                    {
                        formatosConDiscrepancias.add(item)
                    }
                }

            }

            // obtengo todos los formatos con discrepancias de la aeronave seleccionada (que no estan completados)
        }

        // una vez q tengo todos los formatos con discrepancias me voy al detalle

        if(formatosConDiscrepancias.size>0)
        {
            var listaAVer:ArrayList<DetalleFormatoRegistro>? = arrayListOf()

            for(itemDetalleFormato in detalleFormatoRegistroList!!)
            {

                for(itemReportaje in listaReportajes!!)
                {
                    if(itemReportaje.id_cloud.equals(itemDetalleFormato.codigoReportaje))
                    {
                        itemDetalleFormato.nombreReportaje = itemReportaje.nombreReportaje
                    }
                }


                for(itemFormatosConDiscrepancias in formatosConDiscrepancias)
                {
                    if(itemFormatosConDiscrepancias.id_db.equals(itemDetalleFormato.idRegistroFormatoDB))
                    {
                        listaAVer!!.add(itemDetalleFormato)
                    }
                }


            }
            listaAnotacionesFormato = listaAVer

            return listaAVer!!.count()

        }
        else
        {
            return 0
        }



    }

    fun showErrorDialog(message: String?) {
        val bundle = Bundle()
        bundle.putString("errorMessage", message)
        val df: ErrorMessageDialog = ErrorMessageDialog()
        df.setArguments(bundle)
        df.show(requireFragmentManager(), "")
    }

    override fun onDestroyView() {
        super.onDestroyView()
     //   _binding = null
    }


    override fun onResume() {
        super.onResume()
        var prueba = ""
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            //  val sessionManager = SessionUserManager(requireContext())
            //  cocursosViewModel.listaPeriodos(sessionManager!!.getToken()!!)
        } else {
        }
    }
}