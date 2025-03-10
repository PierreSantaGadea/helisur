package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo


import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.gcacace.signaturepad.views.SignaturePad
import com.helisur.helisurapp.R
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaFormatoCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaTareaCloudParameter
import com.helisur.helisurapp.databinding.FragmentFirmasBinding
import com.helisur.helisurapp.domain.model.DetalleFormatoRegistro
import com.helisur.helisurapp.domain.model.Empleado
import com.helisur.helisurapp.domain.model.FormatoRegistro
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.SessionUserManager
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.login.LoginViewModel
import com.helisur.helisurapp.ui.mantenimiento.MainActivityMantenimiento
import com.helisur.helisurapp.ui.mantenimiento.formatos.FormatosViewModel
import com.helisur.helisurapp.ui.mantenimiento.formatos.spinners.SpinenrItemEmpleado
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.GregorianCalendar
import java.util.UUID


@AndroidEntryPoint
class PreVueloFirmasFragment : Fragment() {

    var className = "PreVueloFirmasFragment"
    private lateinit var binding: FragmentFirmasBinding
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
        binding = FragmentFirmasBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
     //   setCheckBox()
        clickListener()
         observers()
        signatureEvents()
        return root
    }

    fun initUI() {
        loading = TransparentProgressDialog(requireContext())
       // loginViewModel.obtieneEmpleados("00020")
        loginViewModel.getEmpleadosListDB()

  //      bmp = BitmapFactory.decodeResource(resources, R.drawable.ic_form)
  //      scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false)

     //   firmapiloto = binding.signaturePadPiloto

       // var bitt = loadBitmapFromViewww(binding.btnBorrarFirmaPiloto!!)
      //  binding.signaturePadPiloto!!.signatureBitmap = bitt
      //  binding.signaturePadCopiloto!!.signatureBitmap = bitt


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

    var responsableFirmoPiloto = false
    var responsableFirmoCoPiloto = false
    fun signatureEvents()
    {

        binding.signaturePadPiloto!!.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                //Event triggered when the pad is touched
                responsableFirmoPiloto = true
            }

            override fun onSigned() {
                //Event triggered when the pad is signed
            }

            override fun onClear() {
                //Event triggered when the pad is cleared
            }
        })

        binding.signaturePadCopiloto!!.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                //Event triggered when the pad is touched
                responsableFirmoCoPiloto = true
            }

            override fun onSigned() {
                //Event triggered when the pad is signed
            }

            override fun onClear() {
                //Event triggered when the pad is cleared
            }
        })
    }



    fun clickListener() {

        binding.tvAtras.setOnClickListener {

            genraa()
     //       pruebaPDF()
       //     TabsPreVuelo.viewPager.setCurrentItem(Constants.TABS_PRE_VUELO.ENTREGA_OPERACIONES)
        }


        binding.btnGuardarFirmaCopiloto!!.setOnClickListener{



            if(idCopiloto.equals(""))
            {
                showErrorDialog("Seleccione un copiloto")
            }
            else
            {
                if(responsableFirmoCoPiloto)
                {
                    piloto_copiloto = "COPILOTO"
                    showDialogLogin()
                }
                else
                {
                    showErrorDialog("Firme por favor")
                }
            }



        }


        binding.btnBorrarFirmaCopiloto!!.setOnClickListener{
            showDialogBorrarFirmaCoPiloto()
        }


        binding.btnGuardarFirmaPiloto!!.setOnClickListener{


            if(idPiloto.equals(""))
            {
                showErrorDialog("Seleccione un piloto")
            }
            else
            {
                if(responsableFirmoPiloto)
                {
                    piloto_copiloto = "PILOTO"
                    showDialogLogin()
                }
                else
                {
                    showErrorDialog("Firme por favor")
                }
            }


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


                var completado:Boolean = false

                if(parameter.listaTareas!=null)
                {
                    completado = false
                }
                else
                {
                    completado = true
                }

                var fechaHoy: String = ""
                var fechaHoyCloud:String = ""
                val gc: GregorianCalendar = GregorianCalendar()
                val pattern = "yyyy-MM-dd HH:mm:ss"
                val pattern2 = "yyyyMMdd HH:mm:ss"
                val simpleDateFormat = SimpleDateFormat(pattern)
                val simpleDateFormat2 = SimpleDateFormat(pattern2)
                simpleDateFormat.calendar = gc
                simpleDateFormat2.calendar = gc
                fechaHoy = simpleDateFormat.format(gc.time)
                fechaHoyCloud = simpleDateFormat2.format(gc.time)

                TabsPreVuelo.formatoParameter.fechaHoraFinRegistro = fechaHoyCloud
                TabsPreVuelo.formatoParameter.usuarioRegistro = SessionUserManager(requireContext()).getId()!!

                var formatoRegistro: FormatoRegistro = FormatoRegistro(uniqueID,"",parameter.codigoFormato,nombreAeronave,parameter.codigoPuestoTecnico,parameter.numeroRTV,
                    parameter.codigoEstacion,parameter.existenDiscrepancias,parameter.numeroRTVDiscrepancias,parameter.accionesMantenimiento,
                    parameter.solicitaEncMotores,parameter.idEmpleadoResponsable,parameter.urlFirmaResponsable,parameter.idEmpleadoPiloto,
                    parameter.urlFirmaPiloto,parameter.idEmpleadoCoPiloto,parameter.urlFirmaCoPiloto,parameter.fechaHoraInicioRegistro,
                    parameter.fechaHoraFinRegistro,parameter.usuarioRegistro,fechaHoy,"",completado)

                formatosViewModel.insertFormatoRegistroDB(formatoRegistro)

                if(parameter.listaTareas!=null)
                {
                    var listaDetalle:ArrayList<GuardaTareaCloudParameter> = ArrayList(TabsPreVuelo.formatoParameter.listaTareas)
                    var listaDetalleDB:ArrayList<DetalleFormatoRegistro> = ArrayList()
                    for(item in listaDetalle)
                    {
                        val uniqueIDDetalle: String = UUID.randomUUID().toString()
                        var detalle:DetalleFormatoRegistro = DetalleFormatoRegistro(uniqueIDDetalle,"",uniqueID,item.codigoRegistroFormato,item.codigoTarea,item.nombreTarea,item.codigoReportaje,
                            "",item.indicadorSN,"",fechaHoy,"")

                        listaDetalleDB.add(detalle)
                    }

                    formatosViewModel.insertDetalleFormatoRegistroDB(listaDetalleDB)
                }




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
                //    copilotosList = ArrayList(it)
                //    pilotosList = ArrayList(it)
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


        loginViewModel.responseGetEmpleadoListDB.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {

                    copilotosList = arrayListOf()
                    pilotosList = arrayListOf()
                    var empleadosListaTotal: ArrayList<Empleado>? = ArrayList(it)
                    empleadosListAll=ArrayList(it)

                    for(item in empleadosListaTotal!!)
                    {
                        if(item.codigoArea.equals("00020"))
                        {
                            copilotosList!!.add(item)
                            pilotosList!!.add(item)
                        }

                    }
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


        formatosViewModel.responsInsertFormatoRegistroDB.observe(viewLifecycleOwner, Observer {
            try {

                if(isOnline())
                {
                    formatosViewModel.grabaFormato(TabsPreVuelo.formatoParameter)
                }
                else
                {
                    //grabacion correcta
                    requireActivity().finish()

                    val intent = Intent (getActivity(), MainActivityMantenimiento::class.java)
                    requireActivity().startActivity(intent)
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





    }

    fun isOnline(): Boolean {
        try {
            val p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com")
            val returnVal = p1.waitFor()
            val reachable = (returnVal == 0)
            return reachable
        } catch (e: Exception) {
            //  e.printStackTrace();
        }
        return false
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
            spinnerArray.add(item.nombreCompleto!!)
            spinnerArrayImages.add(R.drawable.ic_user)
        }

        val adapter =
            SpinenrItemEmpleado(
                requireContext(), 0,
                spinnerArray.toTypedArray(), spinnerArrayImages.toTypedArray()
            )

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
                    idCopiloto  =  copilotosList!![position-1].id_cloud!!
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
            spinnerArray.add(item.nombreCompleto!!)
            spinnerArrayImages.add(R.drawable.ic_user)
        }

     //   val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, spinnerArray)
      //  adapter.setDropDownViewResource(R.layout.spinner_item)
        val adapter =
            SpinenrItemEmpleado(
                requireContext(), 0,
                spinnerArray.toTypedArray(), spinnerArrayImages.toTypedArray()
            )
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
                    idPiloto  =  pilotosList!![position-1].id_cloud!!
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
/*
    override fun onDestroyView() {
        super.onDestroyView()
/*
        try {
            var bise= ""
        }
        catch (e:Exception)
        {
            var bise= ""
        }

 */

        /*
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

         */



    }


 */
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

            var user = etUsuario.text.toString().trim()
            var pass = etPass.text.toString().trim()

            var userExist = false

            for(empleado in empleadosListAll!!)
            {
                var empleadoWithoutDomain = empleado.email!!.replace("@helisur.com.pe","")
                if(empleado.email!!.contains(user))
                {
                    if(pass.equals(empleado.numeroDocumento))
                    {
                        if(piloto_copiloto.equals("PILOTO"))
                        {
                            if(empleado.id_cloud.equals(idPiloto))
                            {
                                userExist = true
                                binding.signaturePadPiloto!!.isEnabled = false
                                binding.llFirmaValidadaPiloto!!.visibility = View.VISIBLE
                            }
                        }
                        else
                        {
                            if(empleado.id_cloud.equals(idCopiloto))
                            {
                                userExist = true
                                binding.signaturePadCopiloto!!.isEnabled = false
                                binding.llFirmaValidadaCopiloto!!.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
            if(userExist)
            {
                dialog.dismiss()
            }
            else
            {
                showErrorDialog("Usuario inválido")

                if(piloto_copiloto.equals("PILOTO"))
                {
                    binding.llFirmaValidadaPiloto!!.visibility = View.GONE
                }
                else
                {
                    binding.llFirmaValidadaCopiloto!!.visibility = View.GONE
                }

            }

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
            binding.llFirmaValidadaPiloto!!.visibility = View.GONE
            responsableFirmoPiloto = false
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
            binding.llFirmaValidadaCopiloto!!.visibility = View.GONE
            responsableFirmoCoPiloto = false
            dialog.dismiss()

        }

        val noBtn = dialog.findViewById(R.id.btnNo) as RelativeLayout
        noBtn.setOnClickListener {

            dialog.dismiss()

        }
        dialog.show()
    }




    var pageHeight = 1120
    var pageWidth = 792

    // creating a bitmap variable
    // for storing our images
 //   lateinit var bmp: Bitmap
  //  lateinit var scaledbmp: Bitmap

    // on below line we are creating a
    // constant code for runtime permissions.
    var PERMISSION_CODE = 101


    fun checkPermissions(): Boolean {
        // on below line we are creating a variable for both of our permissions.

        // on below line we are creating a variable for
        // writing to external storage permission
        var writeStoragePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            WRITE_EXTERNAL_STORAGE
        )

        // on below line we are creating a variable
        // for reading external storage permission
        var readStoragePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            READ_EXTERNAL_STORAGE
        )

        // on below line we are returning true if both the
        // permissions are granted and returning false
        // if permissions are not granted.
        return writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission() {

        // on below line we are requesting read and write to
        // storage permission for our application.
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), PERMISSION_CODE
        )
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // on below line we are checking if the
        // request code is equal to permission code.
        if (requestCode == PERMISSION_CODE) {

            // on below line we are checking if result size is > 0
            if (grantResults.size > 0) {

                // on below line we are checking
                // if both the permissions are granted.
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]
                    == PackageManager.PERMISSION_GRANTED) {

                    var noses = ""
                    // if permissions are granted we are displaying a toast message.
                  //  Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()

                } else {
                    var noses = ""
                    // if permissions are not granted we are
                    // displaying a toast message as permission denied.
                  //  Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show()
                    //finish()
                }
            }
        }
    }




    fun genraa()
    {

        var pdfDocument: PdfDocument = PdfDocument()

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        var paint: Paint = Paint()
        var title: Paint = Paint()

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        var myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

        // below line is used for setting
        // start page for our PDF file.
        var myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        // creating a variable for canvas
        // from our page of PDF.
        var canvas: Canvas = myPage.canvas


        val bitmap: Bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.logo_helisur_color)
        val scaledBitmap: Bitmap = Bitmap.createScaledBitmap(bitmap, 120, 50, false)
        canvas.drawBitmap(scaledBitmap, 20f, 20f, paint)

        // Dibujar líneas para la tabla
        paint.strokeWidth = 2f
        paint.style = Paint.Style.STROKE
        canvas.drawRect(150f, 20f, 570f, 80f, paint) // Cuadro principal

        // Líneas internas
        canvas.drawLine(300f, 20f, 300f, 80f, paint) // Separación primera columna
        canvas.drawLine(450f, 20f, 450f, 80f, paint) // Separación segunda columna

        // Texto dentro de la tabla
        paint.style = Paint.Style.FILL
        paint.textSize = 12f
        paint.typeface = Typeface.DEFAULT_BOLD

        canvas.drawText("Código: FPRGAC-7A", 160f, 40f, paint)
        canvas.drawText("Página 1", 460f, 40f, paint)

        paint.textSize = 10f
        paint.typeface = Typeface.DEFAULT
        canvas.drawText("Edición:", 160f, 60f, paint)
        canvas.drawText("Noviembre 2024", 210f, 60f, paint)
        canvas.drawText("Revisión:", 310f, 60f, paint)
        canvas.drawText("01 (Reedición)", 370f, 60f, paint)
        canvas.drawText("Fecha de Revisión:", 460f, 60f, paint)
        canvas.drawText("Noviembre 2024", 560f, 60f, paint)

        // Dibujar la barra negra con el título
        paint.color = android.graphics.Color.BLACK
        canvas.drawRect(20f, 90f, 570f, 110f, paint)

        paint.color = android.graphics.Color.WHITE
        paint.textSize = 14f
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText("FORMATO DE PRE-VUELO PARA AERONAVE MBB-BK117 B-2", 30f, 105f, paint)




        pdfDocument.finishPage(myPage)

        // below line is used to set the name of
        // our PDF file and its path.
        val file: File = File(Environment.getExternalStorageDirectory(), "GFG.pdf")

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(FileOutputStream(file))

            // on below line we are displaying a toast message as PDF file generated..
            Toast.makeText(requireContext(), "PDF file generated..", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // below line is used
            // to handle error
            e.printStackTrace()

            // on below line we are displaying a toast message as fail to generate PDF
            Toast.makeText(requireContext(), "Fail to generate PDF file..", Toast.LENGTH_SHORT)
                .show()
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close()

        //    viewPdf("GFG.pdf",Environment.getExternalStorageDirectory().path)

    }


    fun generatePDF() {
        // creating an object variable
        // for our PDF document.
        var pdfDocument: PdfDocument = PdfDocument()

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        var paint: Paint = Paint()
        var title: Paint = Paint()

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        var myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

        // below line is used for setting
        // start page for our PDF file.
        var myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        // creating a variable for canvas
        // from our page of PDF.
        var canvas: Canvas = myPage.canvas

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
       // canvas.drawBitmap(scaledbmp, 56F, 40F, paint)

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.textSize = 15F

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(requireContext(), R.color.purple_200))

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText("A portal for IT professionals.", 209F, 100F, title)
        canvas.drawText("Geeks for Geeks", 209F, 80F, title)
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
        title.setColor(ContextCompat.getColor(requireContext(), R.color.purple_200))
        title.textSize = 15F

        // below line is used for setting
        // our text to center of PDF.
        title.textAlign = Paint.Align.CENTER
        canvas.drawText("This is sample document which we have created.", 396F, 560F, title)

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage)

        // below line is used to set the name of
        // our PDF file and its path.
        val file: File = File(Environment.getExternalStorageDirectory(), "GFG.pdf")

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(FileOutputStream(file))

            // on below line we are displaying a toast message as PDF file generated..
            Toast.makeText(requireContext(), "PDF file generated..", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // below line is used
            // to handle error
            e.printStackTrace()

            // on below line we are displaying a toast message as fail to generate PDF
            Toast.makeText(requireContext(), "Fail to generate PDF file..", Toast.LENGTH_SHORT)
                .show()
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close()

    //    viewPdf("GFG.pdf",Environment.getExternalStorageDirectory().path)
    }


    private fun viewPdf(file: String, directory: String) {
        val pdfFile = File(
            Environment.getExternalStorageDirectory().toString() + "/" + directory + "/" + file
        )
        val path = Uri.fromFile(pdfFile)

        // Setting the intent for pdf reader
        val pdfIntent = Intent(Intent.ACTION_VIEW)
        pdfIntent.setDataAndType(path, "application/pdf")
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        try {
            startActivity(pdfIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireActivity(),
                "No application has been found to open PDF files.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}