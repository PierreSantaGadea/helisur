package com.helisur.helisurapp.ui.mantenimiento.formatos

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
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
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosDataTableCloudResponse
import com.helisur.helisurapp.databinding.FragmentEscogeAeronaveBinding
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.mantenimiento.AeronavesViewModel
import com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo.ListaPrevuelosRealizadosActivity
import com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo.SpinenrItemAeronave
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EscogeAeronaveFragment  : Fragment() {

    var className = "ConcursosAvancesFragment"

    private var _binding: FragmentEscogeAeronaveBinding? = null
    private val binding get() = _binding!!

    private val aeronavesViewModel: AeronavesViewModel by viewModels()
    private val formatosViewModel: FormatosViewModel by viewModels()

    private var aeronavesList: ArrayList<ObtieneAeronavesDataTableCloudResponse>? = null
    private var formatosList: ArrayList<ObtieneFormatosDataTableCloudResponse>? = null

    var loading: TransparentProgressDialog? = null
    private var idAeronave:String = ""
    private var nombreAeronave:String = ""
    private var idFormato:String = ""
    private var nombreFormato:String = ""

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
        formatosViewModel.obtieneFormatos()
    }


    fun clickListener()
    {
        binding.btnCotinuar.setOnClickListener {

            if(idAeronave.equals(""))
            {
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
                    saveFormato(requireContext(),idFormato,nombreFormato)
                    val intent = Intent (getActivity(), ListaPrevuelosRealizadosActivity::class.java)
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


    fun saveFormato(context: Context,idFormato:String,nombreFormato:String) {
        val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES.FORMATO, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.SHARED_PREFERENCES.ID_FORMATO, idFormato)
        editor.putString(Constants.SHARED_PREFERENCES.NOMBRE_FORMATO, nombreFormato)
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
                } else {
                }

                idAeronave = aeronavesList!![position].codigoModeloPuesto
                nombreAeronave = aeronavesList!![position].descripcion
                setSpinnerFormato(idAeronave)
            }
        }
    }



    fun setSpinnerFormato(idModeloAeronave:String
    ) {
        var spinnerTipo = binding.spiFormato
        val spinnerArray: MutableList<String> = ArrayList()
        spinnerArray.add("Seleccione formato")

        if(formatosList!=null)
        {
            if(formatosList!!.size>0)
            {
                for(item in formatosList!!)
                {
                    if(item.codigoModeloAeronave.equals(idModeloAeronave))
                    {
                        spinnerArray.add(item.nombreFormato)
                    }
                }
            }
        }


        if(spinnerArray.size>1)
        {
            binding.rlFormato!!.setBackgroundResource(R.drawable.shape_text_box)

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
                        idFormato = formatosList!![position-1].codigoFormato
                        nombreFormato = formatosList!![position-1].nombreFormato
                    }
                }
            }
        }
        else
        {
            binding.rlFormato!!.setBackgroundResource(R.drawable.shape_control_disabled)
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



        formatosViewModel.isLoading.observe(viewLifecycleOwner, Observer {
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



        formatosViewModel.responseObtieneFormatos.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    formatosList = ArrayList(it.data!!.table)
                    binding.rlAeronave!!.setBackgroundResource(R.drawable.shape_text_box)
                   // setSpinnerAeronave()
                } else {
                    Log.e(className, Constants.ERROR.ERROR)
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })


        formatosViewModel.formatosState.observe(viewLifecycleOwner, Observer {
            try {
                if (it.toString().contains(Constants.ERROR.SUCCESS)) {
                } else {
                    if (it.toString().contains(Constants.ERROR.FAILURE)) {
                        var messageError = it.toString().replace("FAILURE","ERROR")
                        //    var messageWithoutFormat = it.toString().replace("FAILURE(message=","")
                        //    var messageFormat = messageWithoutFormat.replace(")","")
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