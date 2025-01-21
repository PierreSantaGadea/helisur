package com.helisur.helisurapp.ui.mantenimiento.formatos

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.databinding.FragmentEscogeAeronaveBinding
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.SessionUserManager
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.login.LoginViewModel
import com.helisur.helisurapp.ui.mantenimiento.AeronavesViewModel
import com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo.PreVueloActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EscogeAeronaveFragment  : Fragment() {

    var className = "ConcursosAvancesFragment"

    private var _binding: FragmentEscogeAeronaveBinding? = null
    private val binding get() = _binding!!

    private val aeronavesViewModel: AeronavesViewModel by viewModels()

    private var aeronavesList: ArrayList<ObtieneAeronavesDataTableCloudResponse>? = null

    var loading: TransparentProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEscogeAeronaveBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
     //   setSpinnerAeronave()
        setSpinnerUbicacion()
        clickListener()
        observers()
        return root
    }

    fun initUI() {
        loading = TransparentProgressDialog(requireContext())
        aeronavesViewModel.obtieneAeronaves()
        ///   val sessionManager = SessionUserManager(requireContext())
        //  val tokennn = sessionManager.getToken()!!
        //  cocursosViewModel.listaPeriodos(sessionManager.getToken()!!)
        //   llenaLista()
        //   setRecyclerView(concursosList)
        //   setSpinnerPeriodo()
    }


    fun clickListener()
    {
        binding.btnCotinuar.setOnClickListener {

            val intent = Intent (getActivity(), PreVueloActivity::class.java)
            getActivity()?.startActivity(intent)
        }
    }


    fun setSpinnerAeronave(
    ) {
        var spinnerTipo = binding.spiAeronave
        val spinnerArray: MutableList<String> = ArrayList()
        spinnerArray.add("Seleccione aeronave")

           for(item in aeronavesList!!)
           {
               var itemName = item.descripcion + " / " + item.codigoModeloPuesto
               spinnerArray.add(itemName)
           }

        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, spinnerArray)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
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
        var spinnerTipo = binding.spiFormato
        val spinnerArray: MutableList<String> = ArrayList()
        spinnerArray.add("Seleccione formato")
        spinnerArray.add("Pre-Vuelo")
        spinnerArray.add("Otro")

        //   for(item in periodosList)
        //   { spinnerArray.add(item.detalle) }

        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, spinnerArray)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
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

        aeronavesViewModel.responseObtieneAeronaves.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    aeronavesList = ArrayList(it.data!!.table)
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