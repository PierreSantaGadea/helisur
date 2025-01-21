package com.helisur.helisurapp.ui.mantenimiento.formatos

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.provider.Contacts.SettingsColumns.KEY
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.helisur.helisurapp.R
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.databinding.FragmentEscogeAeronaveBinding
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.SessionUserManager
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.mantenimiento.AeronavesViewModel
import com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo.ListaPrevuelosRealizadosActivity
import com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo.PreVueloActivity
import com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo.SpinenrItemAeronave
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EscogeAeronaveFragment  : Fragment() {

    var className = "ConcursosAvancesFragment"

    private var _binding: FragmentEscogeAeronaveBinding? = null
    private val binding get() = _binding!!

    private val aeronavesViewModel: AeronavesViewModel by viewModels()

    private var aeronavesList: ArrayList<ObtieneAeronavesDataTableCloudResponse>? = null

    var loading: TransparentProgressDialog? = null
    private var idAeronave:String = ""
    private var nombreAeronave:String = ""
    private var idFormato:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEscogeAeronaveBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        clickListener()
        observers()
        return root
    }

    fun initUI() {
        loading = TransparentProgressDialog(requireContext())
        binding.rlAeronave!!.setBackgroundResource(R.drawable.shape_control_disabled)
        binding.rlFormato!!.setBackgroundResource(R.drawable.shape_control_disabled)
        aeronavesViewModel.obtieneAeronaves()
    }


    fun clickListener()
    {
        binding.btnCotinuar.setOnClickListener {

            if(idAeronave.equals(""))
            {
              //  val intent = Intent (getActivity(), PreVueloActivity::class.java)
              //  getActivity()?.startActivity(intent)
                showErrorDialog("Escoja aeronave")
            }
            else
            {
                if(idFormato.equals(""))
                {
                    showErrorDialog("Escoja formato")
                }
                else
                {

                    saveAeronave(requireContext(),idAeronave,nombreAeronave)
                    val intent = Intent (getActivity(), ListaPrevuelosRealizadosActivity::class.java)
              //      val intent = Intent (getActivity(), PreVueloActivity::class.java)
                    getActivity()?.startActivity(intent)
                }
            }

        }
    }

    fun saveAeronave(context: Context,idAeronave:String,nombreAeronave:String) {
        val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES.AERONAVE, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.SHARED_PREFERENCES.ID_AERONAVE, idAeronave)
        editor.putString(Constants.SHARED_PREFERENCES.NOMBRE_AERONAVE, nombreAeronave)
        editor.apply()
    }


    fun setSpinnerAeronave(
    ) {
        var spinnerTipo = binding.spiAeronave
        val spinnerArray: MutableList<String> = ArrayList()
        val spinnerArrayImages: MutableList<Int> = ArrayList()
    //    spinnerArray.add("Seleccione modelo")
    //    spinnerArrayImages.add(R.drawable.ic_down)

           for(item in aeronavesList!!)
           {
               var itemName = item.descripcion
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

        val adapter = SpinenrItemAeronave(requireContext(),0,
            spinnerArray.toTypedArray(), spinnerArrayImages.toTypedArray())

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
                      idAeronave = ""
                    nombreAeronave = ""
                    binding.spiFormato.adapter = null
                    binding.rlFormato!!.setBackgroundResource(R.drawable.shape_control_disabled)
                } else {
                    idAeronave = aeronavesList!![position].codigoModeloPuesto
                    nombreAeronave = aeronavesList!![position].descripcion
                    setSpinnerFormato()
                    binding.rlFormato!!.setBackgroundResource(R.drawable.shape_text_box)
                    val sessionManager = SessionUserManager(requireContext())
                    //  concursosList = ArrayList()
                    //  cocursosViewModel.listaConcursos(sessionManager.getToken()!!,periodoSelected)
                }
            }
        }
    }



    fun setSpinnerFormato(
    ) {
        var spinnerTipo = binding.spiFormato
        val spinnerArray: MutableList<String> = ArrayList()
        spinnerArray.add("Seleccione formato")
        spinnerArray.add("Pre-Vuelo")

        //   for(item in periodosList)
        //   { spinnerArray.add(item.detalle) }

        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, spinnerArray)
        adapter.setDropDownViewResource(R.layout.spinner_item)
        spinnerTipo.adapter = adapter

        spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                if (position == 0) {
                      idFormato = ""
                } else {
                    idFormato = "op"
                    //  periodoSelected = periodosList[position-1].id
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

        aeronavesViewModel.loginState.observe(viewLifecycleOwner, Observer {
            try {
                if (it.toString().contains(Constants.ERROR.SUCCESS)) {
                } else {
                    if (it.toString().contains(Constants.ERROR.FAILURE)) {
                        Log.e(className, Constants.ERROR.ERROR)
                    }
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })

        aeronavesViewModel.responseObtieneAeronaves.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    aeronavesList = ArrayList(it.data!!.table)
                    binding.rlAeronave!!.setBackgroundResource(R.drawable.shape_text_box)
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
        _binding = null
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