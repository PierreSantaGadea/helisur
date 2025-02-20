package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo


import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.helisur.helisurapp.R
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneEstacionesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveDataTableCloudResponse
import com.helisur.helisurapp.databinding.FragmentDatosAeronaveBinding
import com.helisur.helisurapp.domain.model.Aeronave
import com.helisur.helisurapp.domain.model.Estacion
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.mantenimiento.AeronavesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DatosAeronaveFragment : Fragment() {

    var className = "ConcursosAvancesFragment"
    private val aeronavesViewModel: AeronavesViewModel by viewModels()
    private lateinit var binding: FragmentDatosAeronaveBinding
    var loading: TransparentProgressDialog? = null
 //   private var modelosAeronavesList: ArrayList<ObtieneModelosAeronaveDataTableCloudResponse>? = null
    private var modelosAeronavesList: ArrayList<Aeronave>? = null
  //  private var estacionesList: ArrayList<ObtieneEstacionesDataTableCloudResponse>? = null
    private var estacionesList: ArrayList<Estacion>? = null

    var idAeronave:String = ""
    var idUbicacion:String = ""


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
        var idAeronave = getAeronave(requireContext())
      //  aeronavesViewModel.getAeronaveListCloud(idAeronave!!)
        aeronavesViewModel.getAeronavesByModeloDB(idAeronave!!)
      //  aeronavesViewModel.getEstacionesListCloud()
        aeronavesViewModel.getEstacionesListDB()
        setCheckBox()
        editTextEvent()

        TabsPreVuelo.formatoParameter.existenDiscrepancias = "0"
        TabsPreVuelo.formatoParameter.accionesMantenimiento = "0"
        TabsPreVuelo.formatoParameter.solicitaEncMotores = "0"
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
                    TabsPreVuelo.formatoParameter.numeroRTV = s.toString()
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
                    TabsPreVuelo.formatoParameter.numeroRTVDiscrepancias = s.toString()
                }
            }
        })

    }

    fun clickListener()
    {
        binding.tvSiguiente.setOnClickListener {
            TabsPreVuelo.viewPager.setCurrentItem(Constants.TABS_PRE_VUELO.SISTEMAS)
        }

        binding.tvDocumentacion!!.setOnClickListener {

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://airbusworld.helicopters.airbus.com/c/portal/login?redirect=%2Fgroup%2Fguest&refererPlid=13195661&p_l_id=13195493"))
            startActivity(browserIntent)
        }

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
            if(binding.etRTV!!.text.equals(""))
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
                TabsPreVuelo.formatoParameter.existenDiscrepancias = "1"
            } else {
                binding.rlDiscrepancias!!.setBackgroundResource(R.drawable.shape_control_disabled)
                binding.etDiscrepancias!!.isEnabled = false
                TabsPreVuelo.formatoParameter.existenDiscrepancias = "0"
            }
        }

        binding.chbxAccionesMantenimiento!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                TabsPreVuelo.formatoParameter.accionesMantenimiento = "1"
            } else {
                TabsPreVuelo.formatoParameter.accionesMantenimiento = "0"
            }
        }


        binding.chbxSolicitaEncendidoPrevio!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                TabsPreVuelo.formatoParameter.solicitaEncMotores = "1"
            } else {
                TabsPreVuelo.formatoParameter.solicitaEncMotores = "0"
            }
        }

    }


    fun getAeronave(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES.AERONAVE, MODE_PRIVATE)
        val text = sharedPreferences.getString(Constants.SHARED_PREFERENCES.ID_AERONAVE, "")
        return text
    }

    fun getNombreAeronave(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES.AERONAVE, MODE_PRIVATE)
        val text = sharedPreferences.getString(Constants.SHARED_PREFERENCES.NOMBRE_AERONAVE, "")
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

               var itemName = getNombreAeronave(requireContext())
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
        val adapter = SpinenrItemAeronave(requireContext(),0,
            spinnerArray.toTypedArray(), spinnerArrayImages.toTypedArray())
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
                    TabsPreVuelo.formatoParameter.codigoPuestoTecnico =  ""
                } else {
                    idAeronave = modelosAeronavesList!![position-1].codigoPuestoTecnico
                    TabsPreVuelo.formatoParameter.codigoPuestoTecnico =  modelosAeronavesList!![position-1].codigoPuestoTecnico
                }
            }
        }



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

        val adapter = SpinenrItemUbicacion(requireContext(),0,
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
                    TabsPreVuelo.formatoParameter.codigoEstacion = ""
                } else {
                    idUbicacion = estacionesList!![position-1].id_cloud!!
                    TabsPreVuelo.formatoParameter.codigoEstacion =  estacionesList!![position-1].id_cloud!!
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