package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.helisur.helisurapp.databinding.ActivityLoginBinding
import com.helisur.helisurapp.databinding.FragmentDatosAeronaveBinding
import com.helisur.helisurapp.databinding.FragmentEscogeAeronaveBinding
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.SessionUserManager
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DatosAeronaveFragment : Fragment() {

    var className = "ConcursosAvancesFragment"

  //  private var _binding: FragmentDatosAeronaveBinding? = null
  //  private val binding get() = _binding!!

    private lateinit var binding: FragmentDatosAeronaveBinding

    var loading: TransparentProgressDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDatosAeronaveBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        setSpinnerAeronave()
        setSpinnerUbicacion()
        //   observers()
        return root
    }

    fun initUI() {
        loading = TransparentProgressDialog(requireContext())
        ///   val sessionManager = SessionUserManager(requireContext())
        //  val tokennn = sessionManager.getToken()!!
        //  cocursosViewModel.listaPeriodos(sessionManager.getToken()!!)
        //   llenaLista()
        //   setRecyclerView(concursosList)
        //   setSpinnerPeriodo()
    }




    fun setSpinnerAeronave(
    ) {
        var spinnerTipo = binding.spiAeronave
        val spinnerArray: MutableList<String> = ArrayList()
        spinnerArray.add("Seleccione aeronave")
        spinnerArray.add("Aeronave 1")
        spinnerArray.add("Aeronave 2")

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



    fun setSpinnerUbicacion(
    ) {
        var spinnerTipo = binding.spiUbicacion
        val spinnerArray: MutableList<String> = ArrayList()
        spinnerArray.add("Seleccione ubicación")
        spinnerArray.add("Base principal")
        spinnerArray.add("UFA")

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