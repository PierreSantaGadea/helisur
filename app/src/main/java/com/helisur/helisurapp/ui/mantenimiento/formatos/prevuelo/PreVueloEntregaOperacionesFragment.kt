package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo


import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.gcacace.signaturepad.views.SignaturePad
import com.helisur.helisurapp.R
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaFormatoCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaTareaCloudParameter
import com.helisur.helisurapp.databinding.FragmentEntregaoperacionesBinding
import com.helisur.helisurapp.databinding.FragmentFirmasBinding
import com.helisur.helisurapp.domain.model.DetalleFormatoRegistro
import com.helisur.helisurapp.domain.model.Empleado
import com.helisur.helisurapp.domain.model.FormatoRegistro
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.login.LoginViewModel
import com.helisur.helisurapp.ui.mantenimiento.MainActivityMantenimiento
import com.helisur.helisurapp.ui.mantenimiento.formatos.FormatosViewModel
import com.helisur.helisurapp.ui.mantenimiento.formatos.spinners.SpinenrItemEmpleado
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.GregorianCalendar
import java.util.UUID


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
            TabsPreVuelo.viewPager.setCurrentItem(Constants.TABS_PRE_VUELO.ANOTACIONES)
        }


        binding.tvSiguiente.setOnClickListener {
            TabsPreVuelo.viewPager.setCurrentItem(Constants.TABS_PRE_VUELO.FIRMA_RESPONSABLE)
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
            TabsPreVuelo.formatoParameter.fechaHoraFinRegistro = ""
            TabsPreVuelo.formatoParameter.fechaHoraInicioRegistro = ""
            TabsPreVuelo.formatoParameter.usuarioRegistro = TabsPreVuelo.idUsuario
        } else {
        }
    }




}