package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo

import android.content.Context
import android.content.Context.MODE_PRIVATE
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
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveDataTableCloudResponse
import com.helisur.helisurapp.databinding.FragmentDatosAeronaveBinding
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.SessionUserManager
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.mantenimiento.AeronavesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DatosAeronaveFragment : Fragment() {

    var className = "ConcursosAvancesFragment"

    private val aeronavesViewModel: AeronavesViewModel by viewModels()

    private lateinit var binding: FragmentDatosAeronaveBinding

    var loading: TransparentProgressDialog? = null
    private var modelosAeronavesList: ArrayList<ObtieneModelosAeronaveDataTableCloudResponse>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDatosAeronaveBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
      //  setSpinnerAeronave()
        setSpinnerUbicacion()
        observers()
        return root
    }

    fun initUI() {
        loading = TransparentProgressDialog(requireContext())
        var idAeronave = getAeronave(requireContext())
        aeronavesViewModel.obtieneModelosAeronave(idAeronave!!)
        ///   val sessionManager = SessionUserManager(requireContext())
        //  val tokennn = sessionManager.getToken()!!
        //  cocursosViewModel.listaPeriodos(sessionManager.getToken()!!)
        //   llenaLista()
        //   setRecyclerView(concursosList)
        //   setSpinnerPeriodo()
    }


    fun getAeronave(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES.AERONAVE, MODE_PRIVATE)
        val text = sharedPreferences.getString(Constants.SHARED_PREFERENCES.ID_AERONAVE, "")
        return text
    }



    fun setSpinnerAeronave(
    ) {
        var spinnerTipo = binding.spiAeronave
        val spinnerArray: MutableList<String> = ArrayList()
        spinnerArray.add("Seleccione modelo")
     //   spinnerArray.add("Aeronave 1")
     //   spinnerArray.add("Aeronave 2")

           for(item in modelosAeronavesList!!)
           { spinnerArray.add(item.nombre) }

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
                    ///  periodoSelected = ""
                } else {
                    //  periodoSelected = periodosList[position-1].id
                    val sessionManager = SessionUserManager(requireContext())
                    //  concursosList = ArrayList()
                    //  cocursosViewModel.listaConcursos(sessionManager.getToken()!!,periodoSelected)
                }
            }
        }
    }



    fun setSpinnerUbicacion(
    ) {
        var spinnerTipo = binding.spiUbicacion
        val spinnerArray: MutableList<String> = ArrayList()
        spinnerArray.add("Seleccione ubicación")
        spinnerArray.add("Base principal")
        spinnerArray.add("UFA")

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
                    ///  periodoSelected = ""
                } else {
                    //  periodoSelected = periodosList[position-1].id
                    val sessionManager = SessionUserManager(requireContext())
                    //  concursosList = ArrayList()
                    //  cocursosViewModel.listaConcursos(sessionManager.getToken()!!,periodoSelected)
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

        aeronavesViewModel.responseModelosAeronave.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    modelosAeronavesList = ArrayList(it.data!!.table)
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