package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.helisur.helisurapp.databinding.FragmentEntregaoperacionesBinding
import com.helisur.helisurapp.domain.model.Empleado
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.login.LoginViewModel
import com.helisur.helisurapp.ui.mantenimiento.formatos.FormatosViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PreVueloEntregaOperacionesFragment : Fragment() {

    var className = "PreVueloFirmasFragment"
    private lateinit var binding: FragmentEntregaoperacionesBinding
    var loading: TransparentProgressDialog? = null

    private val loginViewModel: LoginViewModel by viewModels()
    private val formatosViewModel: FormatosViewModel by viewModels()

    var empleadosListAll: ArrayList<Empleado>? = null

    var piloto_copiloto = ""

    var dialogg:Dialog? = null

   // var copilotosList: ArrayList<ObtieneEmpleadosDataTableCloudResponse>? = null
   // var pilotosList: ArrayList<ObtieneEmpleadosDataTableCloudResponse>? = null


    var copilotosList: ArrayList<Empleado>? = null
    var pilotosList: ArrayList<Empleado>? = null

    var idCopiloto = ""
    var urlFirmaCopiloto = ""
    var idPiloto = ""
    var urlFirmaPiloto = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntregaoperacionesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setCheckBox()
        clickListener()
        return root
    }





    fun clickListener() {

        binding.tvAtras.setOnClickListener {
            PreVueloTabsFragment.viewPager.setCurrentItem(Constants.TABS_PRE_VUELO.ANOTACIONES)
        }


        binding.tvSiguiente.setOnClickListener {
            PreVueloTabsFragment.viewPager.setCurrentItem(Constants.TABS_PRE_VUELO.FIRMA_RESPONSABLE)
        }



    }




    fun setCheckBox()
    {
        binding.chxTripulacionEfectuoPrevuelo!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
            binding.tvSiguiente.visibility = View.VISIBLE
            } else {
                binding.tvSiguiente.visibility = View.GONE
            }
        }
    }




    override fun onResume() {
        super.onResume()
        var prueba = ""
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
          //  TabsPreVuelo.formatoParameter.fechaHoraFinRegistro = ""
         //   TabsPreVuelo.formatoParameter.fechaHoraInicioRegistro = ""
            PreVueloTabsFragment.formatoParameter.usuarioRegistro = PreVueloTabsFragment.idUsuario
        } else {
        }
    }




}