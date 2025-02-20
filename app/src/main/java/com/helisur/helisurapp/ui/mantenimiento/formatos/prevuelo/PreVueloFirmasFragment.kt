package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo


import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.helisur.helisurapp.data.cloud.usuario.model.response.ObtieneEmpleadosDataTableCloudResponse
import com.helisur.helisurapp.databinding.FragmentFirmasBinding
import com.helisur.helisurapp.domain.model.DetalleFormatoRegistro
import com.helisur.helisurapp.domain.model.FormatoRegistro
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.login.LoginViewModel
import com.helisur.helisurapp.ui.mantenimiento.MainActivityMantenimiento
import com.helisur.helisurapp.ui.mantenimiento.formatos.FormatosViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID


@AndroidEntryPoint
class PreVueloFirmasFragment : Fragment() {

    var className = "PreVueloFirmasFragment"
    private lateinit var binding: FragmentFirmasBinding
    var loading: TransparentProgressDialog? = null

    private val loginViewModel: LoginViewModel by viewModels()
    private val formatosViewModel: FormatosViewModel by viewModels()

    var piloto_copiloto = ""

    var dialogg:Dialog? = null

    var copilotosList: ArrayList<ObtieneEmpleadosDataTableCloudResponse>? = null
    var pilotosList: ArrayList<ObtieneEmpleadosDataTableCloudResponse>? = null

    var idCopiloto = ""
    var urlFirmaCopiloto = ""
    var idPiloto = ""
    var urlFirmaPiloto = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirmasBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        setCheckBox()
        clickListener()
         observers()
        return root
    }

    fun initUI() {
        loading = TransparentProgressDialog(requireContext())
        loginViewModel.obtieneEmpleados("00020")


        firmapiloto = binding.signaturePadPiloto

    //    val bm = BitmapFactory.decodeResource(requireActivity().resources, R.drawable.shape_button_login)

    //    binding.signaturePadPiloto!!.signatureBitmap = bm
    //    binding.signaturePadCopiloto!!.signatureBitmap = bm
    }

    private fun getBitmapFromString(text: String, fontSizeSP: Float, context: Context): Bitmap {
        val fontSizePX: Int = convertDiptoPix(context, fontSizeSP)
        val pad = (fontSizePX / 9)
        val paint = Paint()

        paint.isAntiAlias = true
        paint.color = Color.WHITE
        paint.textSize = fontSizePX.toFloat()

        val textWidth = (paint.measureText(text) + pad * 2).toInt()
        val height = (fontSizePX / 0.75).toInt()
        val bitmap = Bitmap.createBitmap(textWidth, height, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(bitmap)
        val xOriginal = pad.toFloat()
        canvas.drawText(text, xOriginal, fontSizePX.toFloat(), paint)

        return bitmap
    }

    fun convertDiptoPix(context: Context, dip: Float): Int {
        val value = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            context.resources.displayMetrics
        ).toInt()
        return value
    }

    fun showErrorDialog(message: String?) {
        val bundle = Bundle()
        bundle.putString("errorMessage", message)
        val df: ErrorMessageDialog = ErrorMessageDialog()
        df.setArguments(bundle)
        df.show(requireFragmentManager(), "")
    }

    fun clickListener() {

        binding.tvAtras.setOnClickListener {
            TabsPreVuelo.viewPager.setCurrentItem(Constants.TABS_PRE_VUELO.ANOTACIONES)
        }


        binding.btnGuardarFirmaCopiloto!!.setOnClickListener{
            piloto_copiloto = "COPILOTO"
            showDialogLogin()
        }


        binding.btnBorrarFirmaCopiloto!!.setOnClickListener{
            showDialogBorrarFirmaCoPiloto()
        }


        binding.btnGuardarFirmaPiloto!!.setOnClickListener{
            piloto_copiloto = "PILOTO"
            showDialogLogin()

        }


        binding.btnBorrarFirmaPiloto!!.setOnClickListener{
            showDialogBorrarFirmaPiloto()
        }


        binding.btnGuardarTodo!!.setOnClickListener{
            if(validaciones())
            {
                var parameter: GuardaFormatoCloudParameter = TabsPreVuelo.formatoParameter
                var nombreAeronave:String = getNombreAeronave(requireContext())!!
                val uniqueID: String = UUID.randomUUID().toString()
                var formatoRegistro: FormatoRegistro = FormatoRegistro(uniqueID,"",parameter.codigoFormato,nombreAeronave,parameter.codigoPuestoTecnico,parameter.numeroRTV,
                    parameter.codigoEstacion,parameter.existenDiscrepancias,parameter.numeroRTVDiscrepancias,parameter.accionesMantenimiento,
                    parameter.solicitaEncMotores,parameter.idEmpleadoResponsable,parameter.urlFirmaResponsable,parameter.idEmpleadoPiloto,
                    parameter.urlFirmaPiloto,parameter.idEmpleadoCoPiloto,parameter.urlFirmaCoPiloto,parameter.fechaHoraInicioRegistro,
                    parameter.fechaHoraFinRegistro,parameter.usuarioRegistro,"","")

                formatosViewModel.insertFormatoRegistroDB(formatoRegistro)

                var listaDetalle:ArrayList<GuardaTareaCloudParameter> = ArrayList(TabsPreVuelo.formatoParameter.listaTareas)
                var listaDetalleDB:ArrayList<DetalleFormatoRegistro> = ArrayList()
                for(item in listaDetalle)
                {
                    var detalle:DetalleFormatoRegistro = DetalleFormatoRegistro("",uniqueID,item.codigoRegistroFormato,item.codigoTarea,item.nombreTarea,item.codigoReportaje,
                        "",item.indicadorSN,"","","")

                    listaDetalleDB.add(detalle)
                }

                formatosViewModel.insertDetalleFormatoRegistroDB(listaDetalleDB)

            }

            //formatosViewModel.grabaFormato(TabsPreVuelo.formatoParameter)
        }

    }

    fun getNombreAeronave(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES.AERONAVE, MODE_PRIVATE)
        val text = sharedPreferences.getString(Constants.SHARED_PREFERENCES.NOMBRE_AERONAVE, "")
        return text
    }

    fun validaciones():Boolean
    {
        var isOk = true


        return  isOk
    }

    fun observers()
    {

        loginViewModel.responseObtieneTokenCloud.observe(viewLifecycleOwner, Observer {
            try {

                if(piloto_copiloto.equals("PILOTO"))
                {
                    binding.signaturePadPiloto!!.isEnabled = false
                }
                else
                {
                    binding.signaturePadCopiloto!!.isEnabled = false
                }

                dialogg!!.dismiss()



            } catch (e: Exception) {
                //  Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })



        loginViewModel.responseObtieneEmpleados.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    copilotosList = ArrayList(it)
                    pilotosList = ArrayList(it)
                    setSpinnerCopilotos()
                    setSpinnerPilotos()
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
                        Log.e(className,it.toString())
                        var messageError = it.toString().replace("FAILURE(Error=","")
                        showErrorDialog(messageError.replace(")",""))
                    }
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })


        formatosViewModel.responseGrabaFormato.observe(viewLifecycleOwner, Observer {
            try {

                //grabacion correcta
                requireActivity().finish()

                val intent = Intent (getActivity(), MainActivityMantenimiento::class.java)
                requireActivity().startActivity(intent)


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





    }

    fun setCheckBox()
    {
        binding.chxTripulacionEfectuoPrevuelo!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.llFormularioPilotoCopiloto!!.visibility = View.VISIBLE
            } else {
                binding.llFormularioPilotoCopiloto!!.visibility = View.GONE
            }
        }
    }



    fun setSpinnerCopilotos(
    ) {
        var spinnerTipo = binding.spiCopilotos
        val spinnerArray: MutableList<String> = ArrayList()
        val spinnerArrayImages: MutableList<Int> = ArrayList()
        spinnerArray.add("Seleccione copiloto")
        spinnerArrayImages.add(R.drawable.empty)
        for(item in copilotosList!!)
        {
            spinnerArray.add(item.nombreCompleto)
            spinnerArrayImages.add(R.drawable.ic_user)
        }

        val adapter = SpinenrItemEmpleado(requireContext(),0,
            spinnerArray.toTypedArray(), spinnerArrayImages.toTypedArray())

        //   val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, spinnerArray)
     //   adapter.setDropDownViewResource(R.layout.spinner_item)
        spinnerTipo!!.adapter = adapter

        spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                if (position == 0) {
                    idCopiloto = ""
                    binding.etLicenciaCopiloto!!.setText("")
                } else {
                    idCopiloto  =  copilotosList!![position-1].codigoEmpleado
                    TabsPreVuelo.formatoParameter.idEmpleadoCoPiloto = idCopiloto
                    TabsPreVuelo.formatoParameter.urlFirmaCoPiloto = urlFirmaCopiloto
                    binding.etLicenciaCopiloto!!.setText(copilotosList!![position-1].licencia)
                }
            }
        }
    }


    fun setSpinnerPilotos(
    ) {
        var spinnerTipo = binding.spiPilotos
        val spinnerArray: MutableList<String> = ArrayList()
        val spinnerArrayImages: MutableList<Int> = ArrayList()
        spinnerArray.add("Seleccione piloto")
        spinnerArrayImages.add(R.drawable.empty)
        for(item in pilotosList!!)
        {
            spinnerArray.add(item.nombreCompleto)
            spinnerArrayImages.add(R.drawable.ic_user)
        }

     //   val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, spinnerArray)
      //  adapter.setDropDownViewResource(R.layout.spinner_item)
        val adapter = SpinenrItemEmpleado(requireContext(),0,
            spinnerArray.toTypedArray(), spinnerArrayImages.toTypedArray())
        spinnerTipo!!.adapter = adapter

        spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                if (position == 0) {
                    idPiloto = ""
                    binding.etLicenciaPiloto!!.setText("")
                } else {
                    idPiloto  =  pilotosList!![position-1].codigoEmpleado
                    TabsPreVuelo.formatoParameter.idEmpleadoPiloto = idPiloto
                    TabsPreVuelo.formatoParameter.urlFirmaPiloto = urlFirmaPiloto

                    binding.etLicenciaPiloto!!.setText(pilotosList!![position-1].licencia)
                }
            }
        }
    }

    var firmapiloto:SignaturePad? = null

    companion object {

        fun destruye(){

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Example: If you're creating a Bitmap from 'myView'
        binding.signaturePadPiloto!!.post {
            // This code will run after the View has been measured and laid out
            if (binding.signaturePadPiloto!!.width > 0 && binding.signaturePadPiloto!!.height > 0) {
                val bitmap: Bitmap? = loadBitmapFromView(binding.signaturePadPiloto!!)
                // ... do something with the bitmap ...
            }
        }

        binding.signaturePadCopiloto!!.post {
            // This code will run after the View has been measured and laid out
            if (binding.signaturePadCopiloto!!.width > 0 && binding.signaturePadCopiloto!!.height > 0) {
                val bitmap: Bitmap? = loadBitmapFromView(binding.signaturePadCopiloto!!)
                // ... do something with the bitmap ...
            }
        }

    }

    private fun loadBitmapFromView(view: View): Bitmap? {
        // Check if the view is valid and has dimensions
        if (view.width <= 0 || view.height <= 0) {
            return null
        }
        // ... your code to create a Bitmap from the View ...
        return null
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



    private fun showDialogLogin() {
        val dialog = Dialog(requireContext())
        dialogg = dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_credentials)
        dialog.getWindow()!!.getAttributes().windowAnimations = R.style.DialogAnimation

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.attributes.alpha = 1f
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val etUsuario = dialog.findViewById(R.id.etUsuario) as EditText
        val etPass = dialog.findViewById(R.id.etPass) as EditText

        val yesBtn = dialog.findViewById(R.id.btnSi) as RelativeLayout
        yesBtn.setOnClickListener {

         //   var user = etUsuario.text
         //   var pass = etPass.text
                 var user = "analista_app"
                 var pass = "helisur2024."

            loginViewModel.login(
                user.toString().trim(), pass.toString().trim()
            )
        }

        val noBtn = dialog.findViewById(R.id.btnNo) as RelativeLayout
        noBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


    private fun showDialogBorrarFirmaPiloto() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_borrarfirma)
        dialog.getWindow()!!.getAttributes().windowAnimations = R.style.DialogAnimation

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.attributes.alpha = 1f
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }


        val yesBtn = dialog.findViewById(R.id.btnSi) as RelativeLayout
        yesBtn.setOnClickListener {

            binding.signaturePadPiloto!!.isEnabled = true
            binding.signaturePadPiloto!!.clear()
            dialog.dismiss()

        }

        val noBtn = dialog.findViewById(R.id.btnNo) as RelativeLayout
        noBtn.setOnClickListener {

            dialog.dismiss()

        }
        dialog.show()
    }



    private fun showDialogBorrarFirmaCoPiloto() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_borrarfirma)
        dialog.getWindow()!!.getAttributes().windowAnimations = R.style.DialogAnimation

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.attributes.alpha = 1f
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }


        val yesBtn = dialog.findViewById(R.id.btnSi) as RelativeLayout
        yesBtn.setOnClickListener {

            binding.signaturePadCopiloto!!.isEnabled = true
            binding.signaturePadCopiloto!!.clear()
            dialog.dismiss()

        }

        val noBtn = dialog.findViewById(R.id.btnNo) as RelativeLayout
        noBtn.setOnClickListener {

            dialog.dismiss()

        }
        dialog.show()
    }




}