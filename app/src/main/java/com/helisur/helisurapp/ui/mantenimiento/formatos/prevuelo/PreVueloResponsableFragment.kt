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
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.helisur.helisurapp.R
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaFormatoCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaTareaCloudParameter
import com.helisur.helisurapp.databinding.FragmentResponsableBinding
import com.helisur.helisurapp.domain.model.Aeronave
import com.helisur.helisurapp.domain.model.Anotacion
import com.helisur.helisurapp.domain.model.DetalleFormatoRegistro
import com.helisur.helisurapp.domain.model.Empleado
import com.helisur.helisurapp.domain.model.Estacion
import com.helisur.helisurapp.domain.model.Formato
import com.helisur.helisurapp.domain.model.FormatoRegistro
import com.helisur.helisurapp.domain.model.ModeloAeronave
import com.helisur.helisurapp.domain.model.Reportaje
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.SessionUserManager
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.login.LoginViewModel
import com.helisur.helisurapp.ui.mantenimiento.AeronavesViewModel
import com.helisur.helisurapp.ui.mantenimiento.MainActivityMantenimiento
import com.helisur.helisurapp.ui.mantenimiento.formatos.FormatosViewModel
import com.helisur.helisurapp.ui.mantenimiento.formatos.spinners.SpinenrItemEmpleado
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.UUID


@AndroidEntryPoint
class PreVueloResponsableFragment : Fragment() {

    var className = "PreVueloResponsableFragment"
    private lateinit var binding: FragmentResponsableBinding
    var loading: TransparentProgressDialog? = null
    private val loginViewModel: LoginViewModel by viewModels()
    private val formatosViewModel: FormatosViewModel by viewModels()
    private val aeronavesViewModel: AeronavesViewModel by viewModels()
    var dialogg: Dialog? = null
    var tareasObservados: ArrayList<GuardaTareaCloudParameter>? = null
    var recyclerview: RecyclerView? = null

    //  var empleadosList: ArrayList<ObtieneEmpleadosDataTableCloudResponse>? = null
    var empleadosList: ArrayList<Empleado>? = null
    var empleadosListAll: ArrayList<Empleado>? = null

    var idResponsable = ""
    var licenciaResponsable = ""
    var urlFirmaResponsable = ""

    var fimaValidada = false
    var firmaResponsable : Bitmap? = null

    var idCloudNuevoFormato :String = ""
    var idDB_nuevoFormato = ""
    var codFormato_nuevoFormato = ""

    var formatoAenviar:FormatoRegistro? = null
    var detalleFormatoAenviar:ArrayList<DetalleFormatoRegistro>? = null

    var listaEstacionesDb: ArrayList<Estacion>? = null
    var listaModelosAeronave:ArrayList<ModeloAeronave>? = null
    var listaAeronaves:ArrayList<Aeronave>? = null
    var listaEmpleados:ArrayList<Empleado>? = null
    var listaReportajes:ArrayList<Reportaje>? = null
    var listaFormatos:ArrayList<Formato>? = null
    private var filePdf : File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentResponsableBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        clicListener()
        observers()
        validatePOSTVUELO()
        signatureEvents()

        root!!.post {
            //   binding.signaturePad!!.signatureBitmap.height = 20
            //  binding.signaturePad!!.signatureBitmap.width = 20
        }
        return root
    }

    fun initUI() {
        loading = TransparentProgressDialog(requireContext())
//        binding.signaturePad!!.autofillId!!

        if(isOnline())
        {
            binding.chbxEnviarCorreo!!.isEnabled = true
        }
        else
        {
            binding.chbxEnviarCorreo!!.isEnabled = false
        }

        loginViewModel.getEmpleadosListDB()
        //    loginViewModel.obtieneEmpleados("00091")

        aeronavesViewModel.getEstacionesListDB()
        aeronavesViewModel.getModelosAeronavesListDB()
        aeronavesViewModel.getAeronavesListDB()
        formatosViewModel.getReportajesListDB()
        formatosViewModel.getFormatosListDB()

        recyclerview = binding.rvAnotaciones
        recyclerview!!.layoutManager = LinearLayoutManager(requireContext())

        ///   val sessionManager = SessionUserManager(requireContext())
        //  val tokennn = sessionManager.getToken()!!
        //  cocursosViewModel.listaPeriodos(sessionManager.getToken()!!)
        //   llenaLista()
        //   setRecyclerView(concursosList)
        //   setSpinnerPeriodo()

        //    var bitt = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888)


    }


    fun observers() {

        loginViewModel.responseObtieneTokenCloud.observe(viewLifecycleOwner, Observer {
            try {
                Toast.makeText(getActivity(), "Firma validada", Toast.LENGTH_SHORT).show()
                binding.signaturePad!!.isEnabled = false
                dialogg!!.dismiss()

            } catch (e: Exception) {
                //  Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })



        loginViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                if (!loading!!.isShowing) {
                    loading!!.show()
                }
            } else {
                if (loading!!.isShowing) {
                    loading!!.dismiss()
                }
            }
        })

        loginViewModel.loginState.observe(viewLifecycleOwner, Observer {
            if (it.toString().contains(Constants.ERROR.SUCCESS)) {
            } else {
                if (it.toString().contains(Constants.ERROR.FAILURE)) {
                    dialogg!!.dismiss()
                    var mensaje = it.toString().replace("FAILURE(Error=", "")
                    showErrorDialog(mensaje.replace(")", ""))
                    Log.e(className, Constants.ERROR.ERROR)
                }
            }
        })



        loginViewModel.responseObtieneEmpleados.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    //     empleadosList = ArrayList(it)
                    //     binding.rlAeronave!!.setBackgroundResource(R.drawable.shape_text_box)

                    setSpinnerEmpleados()
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

                    listaEmpleados = ArrayList(it)

                    empleadosList = arrayListOf()
                    var empleadosListaTotal: ArrayList<Empleado>? = ArrayList(it)
                    empleadosListAll = ArrayList(it)

                    for (item in empleadosListaTotal!!) {
                        if (item.codigoArea.equals("00091")) {
                            empleadosList!!.add(item)
                        }

                    }
                    setSpinnerEmpleados()
                } else {
                    Log.e(className, Constants.ERROR.ERROR)
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })



        formatosViewModel.responsInsertFormatoRegistroDB.observe(viewLifecycleOwner, Observer {
            try {

                if (isOnline()) {
                    formatosViewModel.grabaFormato(PreVueloTabsFragment.formatoParameter)
                } else {
                    //grabacion correcta
                    requireActivity().finish()

                    val intent = Intent(getActivity(), MainActivityMantenimiento::class.java)
                    requireActivity().startActivity(intent)
                }

            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })


        formatosViewModel.responseGrabaFormato.observe(viewLifecycleOwner, Observer {
            try {

                idCloudNuevoFormato = it.message

                formatosViewModel.updateIdCloudFormatoRefgistro(idDB_nuevoFormato,idCloudNuevoFormato)


                if(binding.chbxEnviarCorreo!!.isChecked)
                {
                    pintaDocumento(formatoAenviar!!,detalleFormatoAenviar!!)

                    requireActivity().finish()

                    val intent = Intent (getActivity(), MainActivityMantenimiento::class.java)
                    requireActivity().startActivity(intent)

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


        aeronavesViewModel.responseGetEstacionListDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                listaEstacionesDb = ArrayList(it)

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        aeronavesViewModel.responseGetModeloAeronaveListDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                listaModelosAeronave = ArrayList(it.data)

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        formatosViewModel.responseGetFormatoListDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                listaFormatos = ArrayList(it)

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        aeronavesViewModel.responseGetAeronaveListDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                listaAeronaves = ArrayList(it)

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        formatosViewModel.responseGetReportajeListDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                listaReportajes = ArrayList(it)

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


    }

    fun setSpinnerEmpleados(
    ) {
        var spinnerTipo = binding.spiEmpleados

        //  val spinnerArray: MutableList<String> = ArrayList()
        val spinnerArrayImages: MutableList<Int> = ArrayList()

        val spinnerArray: MutableList<String> = ArrayList()
        spinnerArray.add("Seleccione empleado")
        spinnerArrayImages.add(R.drawable.empty)
        for (item in empleadosList!!) {
            spinnerArray.add(item.nombreCompleto!!)
            spinnerArrayImages.add(R.drawable.ic_user)
        }
        val adapter =
            SpinenrItemEmpleado(
                requireContext(), 0,
                spinnerArray.toTypedArray(), spinnerArrayImages.toTypedArray()
            )

        //  val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, spinnerArray)
        //  adapter.setDropDownViewResource(R.layout.spinner_item)
        spinnerTipo!!.adapter = adapter

        spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long,
            ) {
                if (position == 0) {
                    idResponsable = ""
                    binding.etLicencia!!.setText("")
                } else {
                    idResponsable = empleadosList!![position - 1].id_cloud!!
                    licenciaResponsable = empleadosList!![position - 1].licencia!!
                    PreVueloTabsFragment.formatoParameter.idEmpleadoResponsable = idResponsable
                    PreVueloTabsFragment.formatoParameter.urlFirmaResponsable = urlFirmaResponsable
                    binding.etLicencia!!.setText(licenciaResponsable)
                }
            }
        }
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


    fun sendToStorage() {
        var signatureBitmap: Bitmap = binding.signaturePad!!.getSignatureBitmap()

        val storage = Firebase.storage
        val storageRef = storage.reference

        val mountainsRef = storageRef.child("mountains.jpg")

        val mountainImagesRef = storageRef.child("images/mountains.jpg")

// While the file names are the same, the references point to different files
        mountainsRef.name == mountainImagesRef.name // true
        mountainsRef.path == mountainImagesRef.path // false

        val baos = ByteArrayOutputStream()
        signatureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            mountainImagesRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
            } else {
                // Handle failures
                // ...
            }
        }


        /*
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }

         */


    }

    var responsableFirmo = false

    fun signatureEvents()
    {
        binding.signaturePad!!.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                //Event triggered when the pad is touched
                responsableFirmo = true
            }

            override fun onSigned() {
                //Event triggered when the pad is signed
            }

            override fun onClear() {
                //Event triggered when the pad is cleared
            }
        })
    }

    fun bloquearTodoHechoPorResponsable()
    {
        binding.spiEmpleados!!.isEnabled = false
        binding.etLicencia!!.isEnabled = false
        binding.btnGuardarFirma!!.isEnabled = false

        binding.btnBorrarFirma!!.isEnabled = false
        binding.chxSi!!.isEnabled = false
        binding.chxNo!!.isEnabled = false

    //    PreVueloTareasFragment.viewListadoTareas!!.isEnabled = false
   //     PreVueloDatosAeronaveFragment.viewSpinnerAeronave!!.isEnabled = false

        binding.tvAtras!!.visibility = View.GONE


    }

    fun clicListener()
    {

        binding.tvAtras.setOnClickListener {
            PreVueloTabsFragment.viewPager.setCurrentItem(Constants.TABS_PRE_VUELO.SISTEMAS)
        }

        binding.tvSiguiente.setOnClickListener {


            if(idResponsable.equals(""))
            {
                showErrorDialog("Debe escoger un responsable")
            }
            else
            {
                if(!responsableFirmo)
                {
                    showErrorDialog("El responsable debe firmar")
                }
                else
                {
                    if(!fimaValidada)
                    {
                        showErrorDialog("El responsable debe validar la firma")
                    }
                    else
                    {
                        bloquearTodoHechoPorResponsable()
                        PreVueloTabsFragment.viewPager.setCurrentItem(Constants.TABS_PRE_VUELO.ENTREGA_OPERACIONES)
                    }


                }

            }

        }


        binding.chxNo!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                showDialogFormatoSeCerrar()
                binding.chxSi.isChecked = false
                binding.tvSiguiente.visibility = View.GONE
                binding.chbxEnviarCorreo!!.visibility = View.VISIBLE
                binding.btnCerrarMomentaneamente!!.visibility = View.VISIBLE
            } else {

            }
        }


        binding.chxSi!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.chxNo.isChecked = false
                binding.tvSiguiente.visibility = View.VISIBLE
                binding.btnCerrarMomentaneamente!!.visibility = View.GONE
                binding.chbxEnviarCorreo!!.visibility = View.GONE
            } else {

            }
        }

        binding.btnCerrarMomentaneamente!!.setOnClickListener {


            if(!fimaValidada)
            {
                showErrorDialog("El responsable debe validar la firma")
            }
            else
            {
                var parameter: GuardaFormatoCloudParameter = PreVueloTabsFragment.formatoParameter
                var nombreAeronave:String = getNombreAeronave(requireContext())!!
                val uniqueID: String = UUID.randomUUID().toString()

                idDB_nuevoFormato = uniqueID
                codFormato_nuevoFormato = PreVueloTabsFragment.formatoParameter.codigoFormato

                var completado:Boolean = false
                if(parameter.listaTareas!=null)
                {
                    if(ArrayList(parameter.listaTareas).size == 0)
                    {
                        completado = true
                    }
                    else
                    {
                        completado = false
                    }

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

                PreVueloTabsFragment.formatoParameter.fechaHoraFinRegistro = fechaHoyCloud
                PreVueloTabsFragment.formatoParameter.usuarioRegistro = SessionUserManager(requireContext()).getId()!!

                saveBitmapOnLocalStorage(Constants.SAVE_FILE.PREFIJO_FIRMA+Constants.SAVE_FILE.PREFIJO_RESPONSABLE+uniqueID,firmaResponsable!!)


                var formatoRegistro: FormatoRegistro = FormatoRegistro(uniqueID,"",parameter.codigoFormato,nombreAeronave,parameter.codigoPuestoTecnico,parameter.numeroRTV,
                    parameter.codigoEstacion,parameter.existenDiscrepancias,parameter.numeroRTVDiscrepancias,parameter.accionesMantenimiento,
                    parameter.solicitaEncMotores,parameter.idEmpleadoResponsable,parameter.urlFirmaResponsable,parameter.idEmpleadoPiloto,
                    parameter.urlFirmaPiloto,parameter.idEmpleadoCoPiloto,parameter.urlFirmaCoPiloto,parameter.fechaHoraInicioRegistro,
                    parameter.fechaHoraFinRegistro,parameter.usuarioRegistro,fechaHoy,"",completado)

               // formatosViewModel.insertFormatoRegistroDB(formatoRegistro)

                var listaDetalleDB:ArrayList<DetalleFormatoRegistro> = ArrayList()
                if(parameter.listaTareas!=null)
                {
                    var listaDetalle:ArrayList<GuardaTareaCloudParameter> = ArrayList(
                        PreVueloTabsFragment.formatoParameter.listaTareas)
                   // var listaDetalleDB:ArrayList<DetalleFormatoRegistro> = ArrayList()
                    for(item in listaDetalle)
                    {
                        var nombreReportaje = ""
                        for(itemRepo in listaReportajes!!)
                        {
                            if(item.codigoReportaje.equals(itemRepo.id_cloud))
                            {
                                nombreReportaje = itemRepo.nombreReportaje
                            }
                        }

                        val uniqueIDDetalle: String = UUID.randomUUID().toString()
                        var detalle:DetalleFormatoRegistro = DetalleFormatoRegistro(uniqueIDDetalle,"",uniqueID,item.codigoRegistroFormato,item.codigoTarea,item.nombreTarea,item.nombreSistema,item.codigoReportaje,
                            nombreReportaje,item.motivoReportaje,item.indicadorSN,"",fechaHoy,"")

                        listaDetalleDB.add(detalle)
                    }

                   // formatosViewModel.insertDetalleFormatoRegistroDB(listaDetalleDB)
                }


                formatoAenviar = formatoRegistro
                detalleFormatoAenviar = listaDetalleDB

                formatosViewModel.insertFormatoRegistroDB(formatoRegistro)
                formatosViewModel.insertDetalleFormatoRegistroDB(listaDetalleDB)



            }





        }



        binding.btnGuardarFirma!!.setOnClickListener{


            if(idResponsable.equals(""))
            {
                showErrorDialog("Seleccione un responsable")
            }
            else
            {
                if(responsableFirmo)
                {
                    showDialogLogin()
                }
                else
                {
                    showErrorDialog("Firme por favor")
                }
            }


        }


        binding.btnBorrarFirma!!.setOnClickListener{

            showDialogBorrarFirma()

        }





    }

    fun savePdfToLocalStorage(nombreDocumento: String, pdfDocument: PdfDocument): File? {
        try {
            val file: File = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // For Android 10+ (API 29+), use scoped storage
                val mediaDir = File(context?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), Constants.SAVE_FILE.CARPETA_GENERAL + "/" + Constants.SAVE_FILE.CARPETA_FORMATOS)
                if (!mediaDir.exists()) {
                    mediaDir.mkdirs()
                }
                File(mediaDir, nombreDocumento + ".pdf")
            } else {
                // For Android 9 and below, use external storage with permissions
                val root = Environment.getExternalStorageDirectory().toString()
                val fileee: File = File("$root/" + Constants.SAVE_FILE.CARPETA_GENERAL + "/" + Constants.SAVE_FILE.CARPETA_FORMATOS)
                if (!fileee.exists()) {
                    fileee.mkdirs()
                }
                File(fileee, nombreDocumento + ".pdf")
            }

            pdfDocument.writeTo(FileOutputStream(file))
            return file
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback: try to save to app's internal storage
            try {
                val internalDir = File(context?.filesDir, Constants.SAVE_FILE.CARPETA_GENERAL + "/" + Constants.SAVE_FILE.CARPETA_FORMATOS)
                if (!internalDir.exists()) {
                    internalDir.mkdirs()
                }
                val file = File(internalDir, nombreDocumento + ".pdf")
                pdfDocument.writeTo(FileOutputStream(file))
                return file
            } catch (fallbackException: Exception) {
                fallbackException.printStackTrace()
                return null
            }
        }
    }

    fun loadBitmapFromLocalStorage(nombreDocumento: String): Bitmap? {
        try {
            val file: File = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // For Android 10+ (API 29+), use scoped storage
                val mediaDir = File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), Constants.SAVE_FILE.CARPETA_GENERAL + "/" + Constants.SAVE_FILE.CARPETA_FIRMA)
                File(mediaDir, nombreDocumento + ".png")
            } else {
                // For Android 9 and below, use external storage with permissions
                val root = Environment.getExternalStorageDirectory().toString()
                val fileee: File = File("$root/" + Constants.SAVE_FILE.CARPETA_GENERAL + "/" + Constants.SAVE_FILE.CARPETA_FIRMA)
                File(fileee, nombreDocumento + ".png")
            }

            if (file.exists()) {
                return BitmapFactory.decodeFile(file.absolutePath)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback: try to load from app's internal storage
            try {
                val internalDir = File(context?.filesDir, Constants.SAVE_FILE.CARPETA_GENERAL + "/" + Constants.SAVE_FILE.CARPETA_FIRMA)
                val file = File(internalDir, nombreDocumento + ".png")
                if (file.exists()) {
                    return BitmapFactory.decodeFile(file.absolutePath)
                }
            } catch (fallbackException: Exception) {
                fallbackException.printStackTrace()
            }
        }
        return null
    }

    fun loadPdfFromLocalStorage(nombreDocumento: String): File? {
        try {
            val file: File = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // For Android 10+ (API 29+), use scoped storage
                val mediaDir = File(context?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), Constants.SAVE_FILE.CARPETA_GENERAL + "/" + Constants.SAVE_FILE.CARPETA_FORMATOS)
                File(mediaDir, nombreDocumento + ".pdf")
            } else {
                // For Android 9 and below, use external storage with permissions
                val root = Environment.getExternalStorageDirectory().toString()
                val fileee: File = File("$root/" + Constants.SAVE_FILE.CARPETA_GENERAL + "/" + Constants.SAVE_FILE.CARPETA_FORMATOS)
                File(fileee, nombreDocumento + ".pdf")
            }

            if (file.exists()) {
                return file
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback: try to load from app's internal storage
            try {
                val internalDir = File(context?.filesDir, Constants.SAVE_FILE.CARPETA_GENERAL + "/" + Constants.SAVE_FILE.CARPETA_FORMATOS)
                val file = File(internalDir, nombreDocumento + ".pdf")
                if (file.exists()) {
                    return file
                }
            } catch (fallbackException: Exception) {
                fallbackException.printStackTrace()
            }
        }
        return null
    }

    fun getNombreAeronave(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES.AERONAVE, MODE_PRIVATE)
        val text = sharedPreferences.getString(Constants.SHARED_PREFERENCES.NOMBRE_AERONAVE, "")
        return text
    }

    fun showErrorDialog(message: String?) {
        val bundle = Bundle()
        bundle.putString("errorMessage", message)
        val df: ErrorMessageDialog = ErrorMessageDialog()
        df.setArguments(bundle)
        df.show(requireFragmentManager(), "")
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
        binding.signaturePad!!.post {
            // This code will run after the View has been measured and laid out
            if (binding.signaturePad!!.width > 0 && binding.signaturePad!!.height > 0) {
                val bitmap: Bitmap? = loadBitmapFromView(binding.signaturePad!!)
                // ... do something with the bitmap ...
            }
        }

 */


    }


     */

    fun loadBitmapFromView(v: View): Bitmap? {
        try {
            // Forzar la medición del view para obtener dimensiones correctas
            v.measure(
                android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED),
                android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED)
            )
            
            val width = v.measuredWidth
            val height = v.measuredHeight
            
            if (width <= 0 || height <= 0) {
                return null
            }
            
            // Crear bitmap con configuración de alta calidad
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            
            // Configurar el view
            v.layout(0, 0, width, height)
            
            // Dibujar el view en el canvas
            v.draw(canvas)
            
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }


    fun String.titlecaseFirstChar() = replaceFirstChar(Char::titlecase)


    fun generaFormatoPDF(nombreDocumento:String) {

        // Forzar la medición del layout para obtener dimensiones correctas
        binding.llDetalleFormato!!.measure(
            android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED),
            android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED)
        )
        
        // Obtener dimensiones reales del layout para mejor calidad
        val layoutWidth = binding.llDetalleFormato!!.measuredWidth
        val layoutHeight = binding.llDetalleFormato!!.measuredHeight
        
        // Usar dimensiones del layout si están disponibles, sino usar valores por defecto
        // Aumentar la resolución para mejor calidad (factor de escala 2x para alta resolución)
        val scaleFactor = 2.0f
        var pageHeight = if (layoutHeight > 0) (layoutHeight * scaleFactor).toInt() else 1950
        var pageWidth = if (layoutWidth > 0) (layoutWidth * scaleFactor).toInt() else 635

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
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 3).create()

        // below line is used for setting
        // start page for our PDF file.
        var myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        // creating a variable for canvas
        // from our page of PDF.
        var canvas: Canvas = myPage.canvas





        //acaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa


        // Agregar márgenes/padding general al PDF
        val margin = 40f  // Margen de 40px en todos los lados
        val startX = margin
        val startY = margin
        
        // Calcular dimensiones del contenido con márgenes
        val contentWidth = pageWidth - (margin * 2).toInt()
        val contentHeight = pageHeight - (margin * 2).toInt()
        
        // Dibujar fondo blanco para el margen
        paint.color = android.graphics.Color.WHITE
        paint.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, pageWidth.toFloat(), pageHeight.toFloat(), paint)
        
        val bitmap: Bitmap? = loadBitmapFromView(binding.llDetalleFormato!!)
        
        if (bitmap != null) {
            // Escalar el bitmap para que quepa en el área de contenido con márgenes
            val scaledBitmap: Bitmap = Bitmap.createScaledBitmap(bitmap, contentWidth, contentHeight, true)
            
            // Dibujar el bitmap en el canvas con márgenes
            canvas.drawBitmap(scaledBitmap, startX, startY, paint)
            
            // Liberar memoria del bitmap original
            bitmap.recycle()
        } else {
            // Fallback si no se puede cargar el bitmap
            paint.color = android.graphics.Color.RED
            paint.textSize = 40f  // Aumentar tamaño de texto para alta resolución
            canvas.drawText("Error al cargar el formulario", startX + 100f, startY + 200f, paint)
        }

        /*

        // Dibujar contorno de la tabla
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        canvas.drawRect(startX, startY, startX + tableWidth, startY + rowHeight * 2, paint)

        // Columnas principales
        val col1End = startX + col1Width
        val col2End = col1End + col2Width
        val col3End = col2End + col3Width

        canvas.drawLine(col1End, startY, col1End, startY + rowHeight * 2, paint) // Línea entre col 1 y 2
        canvas.drawLine(col2End, startY, col2End, startY + rowHeight * 2, paint) // Línea entre col 2 y 3

        // Segunda columna: Divisiones internas
        val col2HalfY = startY + rowHeight
        canvas.drawLine(col1End, col2HalfY, col2End, col2HalfY, paint) // Divide la segunda columna en 2 filas
        canvas.drawLine((col1End + col2End) / 2, col2HalfY, (col1End + col2End) / 2, startY + rowHeight * 2, paint) // Divide en 2 columnas pequeñas

        // Tercera columna: Divisiones internas
        val col3HalfY = startY + rowHeight
        canvas.drawLine(col2End, col3HalfY, col3End, col3HalfY, paint) // Divide la tercera columna en 2 filas

        // Cargar imagen y colocarla en la primera columna
        val bitmap: Bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.logomini)
        val scaledBitmap: Bitmap = Bitmap.createScaledBitmap(bitmap, col1Width.toInt(), (rowHeight * 2).toInt(), false)
        canvas.drawBitmap(scaledBitmap, startX, startY, paint)

        // Texto en cada sección con dos líneas
        paint.style = Paint.Style.FILL
        paint.textSize = 12f
        paint.typeface = Typeface.DEFAULT_BOLD

        // Segunda columna - Primera fila (Titulo general)
        canvas.drawText("Código: FPRGAC-7A", col1End + 10f, startY + 25f, paint)

        // Segunda columna - Sub columnas con dos líneas de texto
        paint.textSize = 10f
        canvas.drawText("Edición", col1End + 10f, col2HalfY + 20f, paint)
        canvas.drawText("Noviembre 2024", col1End + 10f, col2HalfY + 35f, paint)

        canvas.drawText("Revisión", (col1End + col2End) / 2 + 10f, col2HalfY + 20f, paint)
        canvas.drawText("01 (Reedición)", (col1End + col2End) / 2 + 10f, col2HalfY + 35f, paint)

        // Tercera columna - Filas con dos líneas de texto
        paint.textSize = 12f
        canvas.drawText("Página 1", col2End + 10f, startY + 25f, paint)
       // canvas.drawText("Fila 1 - Línea 2", col2End + 10f, startY + 40f, paint)

        canvas.drawText("Fecha Revisión", col2End + 10f, col3HalfY + 20f, paint)
        canvas.drawText("Noviembre 2024", col2End + 10f, col3HalfY + 35f, paint)

         */

        //acaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa



        pdfDocument.finishPage(myPage)

        // Use the helper function to save PDF with proper storage handling
        val file = savePdfToLocalStorage(nombreDocumento, pdfDocument)

        if (file != null) {
            // on below line we are displaying a toast message as PDF file generated..
            //   Toast.makeText(requireContext(), "PDF file generated..", Toast.LENGTH_SHORT).show()
            sendPdf(idDB_nuevoFormato, codFormato_nuevoFormato, file)
        } else {
            // on below line we are displaying a toast message as fail to generate PDF
            Toast.makeText(requireContext(), "El PDF no se genero correctamente", Toast.LENGTH_SHORT).show()

        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close()

        //    viewPdf("GFG.pdf",Environment.getExternalStorageDirectory().path)

    }

    fun sendPdf(idDb:String,codFormato:String, pdfFile: File)
    {
        var uri: Uri = pdfFile.toUri()
        val fileContent: String = ConvertToString(requireContext(), uri)

        formatosViewModel.enviaPdf(fileContent,idCloudNuevoFormato+".pdf")
    }

    fun pintaDocumento(formatoRegistro: FormatoRegistro,detalleFormatoRegistro:ArrayList<DetalleFormatoRegistro>)
    {

        val cal: Calendar = Calendar.getInstance()
        val month_date: SimpleDateFormat = SimpleDateFormat("MMMM")
        val year_date: SimpleDateFormat = SimpleDateFormat("yyyy")

        val month_name: String = month_date.format(cal.getTime()).titlecaseFirstChar()
        val year_name: String = year_date.format(cal.getTime()).titlecaseFirstChar()

        binding.tvMesEdicion!!.setText(month_name + " "+ year_name)
        binding.tvMesRevision!!.setText(month_name + " "+ year_name)

        binding.tvFormatoRTV!!.setText(formatoRegistro.numeroRTV)

        var nombreEstacion = ""

        for(item in listaEstacionesDb!!) {
            if(item.id_cloud.equals(formatoRegistro.codigoEstacion))
            {
                nombreEstacion = item.nombre!!
            }
        }

        binding.tvFormatoUBICACION!!.setText(nombreEstacion)

        var idModeloAeronave = ""
        var placaAeronave = ""

        for (item in listaAeronaves!!)
        {
            if(formatoRegistro.codigoPuestoTecnico.equals(item.codigoModeloPuesto))
            {
                idModeloAeronave = item.id_cloud!!
                placaAeronave = item.placa
            }
        }


        var nombreModelo = ""
        for(item in listaModelosAeronave!!)
        {
            if(item.id_cloud.equals(idModeloAeronave))
            {
                nombreModelo = item.nombre!!

            }

        }

        var nombreFormato = ""

        for(item in listaFormatos!!)
        {
            if(item.id_cloud.equals(formatoRegistro.codigoFormato))
            {
                nombreFormato = item.nombreFormato!!

            }

        }

        binding.tvTituloFormatoPdf!!.setText("FORMATO DE " +nombreFormato.toUpperCase()+ " PARA AERONAVE "+nombreModelo)

        binding.tvFormatoAERONAVE!!.setText(nombreModelo+"/"+formatoRegistro.nombreAeronave + "/" + placaAeronave)

        if(formatoRegistro.existenDiscrepancias.equals("1")) {
            binding.ivExistendiscrepanciasSi!!.setImageResource(R.drawable.ic_check)
            binding.ivExistendiscrepanciasNo!!.setImageResource(R.drawable.ic_uncheck)

        }
        else {
            binding.ivExistendiscrepanciasSi!!.setImageResource(R.drawable.ic_uncheck)
            binding.ivExistendiscrepanciasNo!!.setImageResource(R.drawable.ic_check)
        }


        binding.tvDiscrepanciasNroRTV!!.setText("b. Las discrepancias estan registradas en el RTV Nro "+formatoRegistro.numeroRTVDiscrepancias)



        if(formatoRegistro.accionesMantenimiento.equals("1")) {
            binding.ivAccionesmantenimientoSi!!.setImageResource(R.drawable.ic_check)
            binding.ivAccionesmantenimientoNo!!.setImageResource(R.drawable.ic_uncheck)

        }
        else {
            binding.ivAccionesmantenimientoSi!!.setImageResource(R.drawable.ic_uncheck)
            binding.ivAccionesmantenimientoNo!!.setImageResource(R.drawable.ic_check)
        }


        if(formatoRegistro.solicitaEncMotores.equals("1")) {
            binding.ivEncendidomotoresSi!!.setImageResource(R.drawable.ic_check)
            binding.ivEncendidomotoresNo!!.setImageResource(R.drawable.ic_uncheck)

        }
        else {
            binding.ivEncendidomotoresSi!!.setImageResource(R.drawable.ic_uncheck)
            binding.ivEncendidomotoresNo!!.setImageResource(R.drawable.ic_check)
        }

        // Load signature using the new helper function
        val firmaResponsableBitmap = loadBitmapFromLocalStorage(Constants.SAVE_FILE.PREFIJO_FIRMA + Constants.SAVE_FILE.PREFIJO_RESPONSABLE + formatoRegistro.id_db)
        if (firmaResponsableBitmap != null) {
            binding.ivFirmaResponsable!!.setImageBitmap(firmaResponsableBitmap)
        }

        binding.tvFechaResponsable!!.setText(formatoRegistro.fechaRegistro)



        var nombreRes = ""
        var licenciaRes = ""


        var nombreCopiloto = ""
        var licenciaCopiloto = ""
        var nombrePiloto = ""
        var licenciaPiloto = ""


        for(item in listaEmpleados!!)
        {
            if(item.id_cloud.equals(formatoRegistro.idEmpleadoResponsable))
            {
                nombreRes = item.nombreCompleto!!
                licenciaRes = item.licencia!!
            }

            if(item.id_cloud.equals(formatoRegistro.idEmpleadoCoPiloto))
            {
                nombreCopiloto = item.nombreCompleto!!
                licenciaCopiloto = item.licencia!!
            }

            if(item.id_cloud.equals(formatoRegistro.idEmpleadoPiloto))
            {
                nombrePiloto = item.nombreCompleto!!
                licenciaPiloto = item.licencia!!
            }
        }

        binding.tvNombreResponsable!!.text = nombreRes.toString()
        binding.tvLicenciaResponsable!!.text = licenciaRes

        binding.tvNombreCopiloto!!.text = nombreCopiloto.toString()
        binding.tvLicenciaCopiloto!!.text = licenciaCopiloto

        binding.tvNombrePiloto!!.text = nombrePiloto.toString()
        binding.tvLicenciaPiloto!!.text = licenciaPiloto

        // Load copilot signature using the new helper function
        val firmaCopilotoBitmap = loadBitmapFromLocalStorage(Constants.SAVE_FILE.PREFIJO_FIRMA + Constants.SAVE_FILE.PREFIJO_COPILOTO + formatoRegistro.id_db)
        if (firmaCopilotoBitmap != null) {
            binding.ivFirmaCopiloto!!.setImageBitmap(firmaCopilotoBitmap)
        }

        // Load pilot signature using the new helper function
        val firmaPilotoBitmap = loadBitmapFromLocalStorage(Constants.SAVE_FILE.PREFIJO_FIRMA + Constants.SAVE_FILE.PREFIJO_PILOTO + formatoRegistro.id_db)
        if (firmaPilotoBitmap != null) {
            binding.ivFirmaPiloto!!.setImageBitmap(firmaPilotoBitmap)
        }


        if(detalleFormatoRegistro!=null)
        {
            if(detalleFormatoRegistro.size==0)
            {
                binding.tvSinAnotaciones!!.visibility = View.VISIBLE
            }
            else
            {
                binding.tvSinAnotaciones!!.visibility = View.GONE
                for(reportajeItem in detalleFormatoRegistro)
                {
                    newCheckBox(reportajeItem.nombreReportaje,reportajeItem.codigoReportaje,binding.llcontenedorTareas!!,reportajeItem.indicadorSN!!,reportajeItem.indicadorBloqueo!!,reportajeItem.nombreTarea!!,reportajeItem.nombreSistema,reportajeItem.motivoReportaje)
                }

            }

        }



        //    showImage(fileee.path,binding.ivFirmaResponsable)

        /*
                if(formatoRegistro.existenDiscrepancias.equals("1")) {
                    binding.chbxDiscrepanciasSi.isChecked = true
                    binding.chbxDiscrepanciasNo.isChecked = false
                }
                else {
                    binding.chbxDiscrepanciasSi.isChecked = false
                    binding.chbxDiscrepanciasNo.isChecked = true
                }

                binding.tvDiscrepanciasNroRtv.setText("b. Las discrepancias surgidas estan registradas en el RTV Nro "+formatoRegistro.numeroRTVDiscrepancias)



                if(formatoRegistro.accionesMantenimiento.equals("1")) {
                    binding.chbxAccionesmantenimientoSi.isChecked = true
                    binding.chbxAccionesmantenimientoNo.isChecked = false
                }
                else {
                    binding.chbxAccionesmantenimientoSi.isChecked = false
                    binding.chbxAccionesmantenimientoNo.isChecked = true
                }


                if(formatoRegistro.solicitaEncMotores.equals("1")) {
                    binding.chbxEncendidomotoresSi.isChecked = true
                    binding.chbxEncendidomotoresNo.isChecked = false
                }
                else {
                    binding.chbxEncendidomotoresSi.isChecked = false
                    binding.chbxEncendidomotoresNo.isChecked = true
                }
        */

        var nommbreFile =Constants.SAVE_FILE.PREFIJO_FORMATO+formatoRegistro.codigoFormato+"_"+formatoRegistro.id_db
        generaFormatoPDF(nommbreFile)

    }

    fun newCheckBox(nombre:String, id:String, contenedor: LinearLayout, indicadorSN:String, indicadorBloqueo:String, nombreTarea:String, nombreSistemas:String, motivoReportaje:String)
    {

        val tituloTarea = TextView(requireContext())

        val nombreReportaje = TextView(requireContext())

        val nombreSistema = TextView(requireContext())

        val motivReportaje = TextView(requireContext())

        //  tituloTarea.setText("\n"+nombreTarea)
        tituloTarea.setText("  "+nombreTarea)

        nombreReportaje.setText("   "+nombre)

        nombreSistema.setText("- "+nombreSistemas)

        motivReportaje.setText("   Motivo : "+motivoReportaje)

        nombreSistema.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        tituloTarea.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        nombreReportaje.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey))
        motivReportaje.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey))

        val tabletSize = resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {
            tituloTarea.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.nombretarea_formatos_realizados))
            nombreReportaje.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.nombretarea_formatos_realizados))
            motivReportaje.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.nombrereportaje_cel_pdf))
            nombreSistema.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.nombretarea_formatos_realizados_cel))
            //     tituloTarea.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23f)
        } else {
            tituloTarea.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.nombretarea_cel_pdf))
            nombreSistema.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.nombretarea_cel_pdf))
            nombreReportaje.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.nombrereportaje_cel_pdf))
            motivReportaje.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.nombrereportaje_cel_pdf))
            //    tituloTarea.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        }
        //  CompoundButtonCompat.setButtonTintList(cb, ColorStateList.valueOf(getResources().getColor(R.color.titulo_pantalla_general)))
        // CompoundButtonCompat.setButtonTintList(tituloTarea, ColorStateList.valueOf(getResources().getColor(R.color.titulo_pantalla_general)))


        //  tituloTarea.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);

        //  val param = cb.layoutParams as ViewGroup.MarginLayoutParams
        //  param.setMargins(0,10,0,0)
        //  cb.layoutParams = param
        contenedor.addView(nombreSistema)
        contenedor.addView(tituloTarea)
        contenedor.addView(nombreReportaje)
        contenedor.addView(motivReportaje)

    }


    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray {
        val bufferSize = 1024
        val byteArrayOutputStream = ByteArrayOutputStream()
        val byteArray = ByteArray(bufferSize)
        var len: Int
        while ((inputStream.read(byteArray).also { len = it }) != -1) {
            byteArrayOutputStream.write(byteArray, 0, len)
        }
        return byteArrayOutputStream.toByteArray()
    }

    fun ConvertToString(context: Context, uri: Uri?): String {
        var encodedValue = ""
        try {
            val `in` = context.contentResolver.openInputStream(uri!!)
            val bytes = getBytes(`in`!!)
            encodedValue = Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return encodedValue
    }


    override fun onResume() {
        super.onResume()
        var prueba = ""
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {

            var listaAnotaciones:ArrayList<Anotacion> = arrayListOf()

            for(itemSistema in PreVueloTareasFragment.sistemasList!!)
            {
                if(itemSistema.tareas!=null)
                {
                    for(itemTarea in itemSistema.tareas!!)
                    {
                        if(itemTarea.reportaje_NoAplica || itemTarea.reportaje_RTV || itemTarea.reportaje_DanosMenores || itemTarea.reportaje_MELMDS)
                        {
                            //si tiene reportajes
                            var anotacion = Anotacion()

                            anotacion.codigoSistema = itemSistema.codigoSistema
                            anotacion.nombreSistema = itemSistema.nombrePosicion
                            anotacion.codigoTarea = itemTarea.codigoTarea
                            anotacion.nombreTarea = itemTarea.nombreTarea

                            anotacion.reportaje_NoAplica = itemTarea.reportaje_NoAplica
                            anotacion.reportaje_RTV = itemTarea.reportaje_RTV
                            anotacion.reportaje_DanosMenores = itemTarea.reportaje_DanosMenores
                            anotacion.reportaje_MELMDS = itemTarea.reportaje_MELMDS

                            anotacion.reportaje_Motivo = itemTarea.reportaje_Motivo

                            anotacion.instruccion = itemTarea.instruccion

                            listaAnotaciones.add(anotacion)

                        }

                    }
                }
            }

            if(listaAnotaciones.size>0)
            {
                setRecyclerViewAnotaciones(listaAnotaciones)
                binding.rvAnotaciones!!.visibility = View.VISIBLE
                binding.tituloAnotaciones!!.visibility = View.VISIBLE

                tareasObservados = arrayListOf()
                var iduser = PreVueloTabsFragment.idUsuario
                var helicopteroAPTO = true
                for(tareaObservada in listaAnotaciones)
                {
                    if(tareaObservada.reportaje_NoAplica)
                    {
                        tareasObservados!!.add(GuardaTareaCloudParameter("0",tareaObservada.codigoTarea!!,tareaObservada.id_NoAplica,"1",iduser,tareaObservada.reportaje_Motivo!!,tareaObservada.nombreTarea!!,tareaObservada.nombreSistema!!,tareaObservada.instruccion!!))
                    }

                    if(tareaObservada.reportaje_RTV)
                    {
                        helicopteroAPTO = false
                        tareasObservados!!.add(GuardaTareaCloudParameter("0",tareaObservada.codigoTarea!!,tareaObservada.id_RTV,"1",iduser,tareaObservada.reportaje_Motivo!!,tareaObservada.nombreTarea!!,tareaObservada.nombreSistema!!,tareaObservada.instruccion!!))
                    }

                    if(tareaObservada.reportaje_DanosMenores)
                    {
                        tareasObservados!!.add(GuardaTareaCloudParameter("0",tareaObservada.codigoTarea!!,tareaObservada.id_DanosMenores,"1",iduser,tareaObservada.reportaje_Motivo!!,tareaObservada.nombreTarea!!,tareaObservada.nombreSistema!!,tareaObservada.instruccion!!))
                    }

                    if(tareaObservada.reportaje_MELMDS)
                    {
                        tareasObservados!!.add(GuardaTareaCloudParameter("0",tareaObservada.codigoTarea!!,tareaObservada.id_MELMDS,"1",iduser,tareaObservada.reportaje_Motivo!!,tareaObservada.nombreTarea!!,tareaObservada.nombreSistema!!,tareaObservada.instruccion!!))
                    }
0
                }

                if(helicopteroAPTO)
                {
                    binding.condicionhelicoptero!!.text = "El helicóptero se encuentra en condición satisfactoria"
                    binding.condicionhelicoptero!!.setTextColor(Color.parseColor("#1A4905"))
                    binding.ivCondicionHelicoptero!!.setBackgroundResource(R.drawable.ic_success)
                    binding.chxSi.isEnabled = true
                }
                else
                {
                    binding.condicionhelicoptero!!.text = "El helicóptero se encuentra en condición no satisfactoria"
                    binding.condicionhelicoptero!!.setTextColor(Color.parseColor("#e11f21"))
                    binding.ivCondicionHelicoptero!!.setBackgroundResource(R.drawable.ic_failed)
                    binding.chxSi.isEnabled = false
                }


                PreVueloTabsFragment.formatoParameter.listaTareas = tareasObservados

            }
            else
            {
                binding.rvAnotaciones!!.visibility = View.GONE
                binding.tituloAnotaciones!!.visibility = View.GONE
                binding.condicionhelicoptero!!.text = "El helicóptero se encuentra en condición satisfactoria"
                binding.condicionhelicoptero!!.setTextColor(Color.parseColor("#1A4905"));
                binding.ivCondicionHelicoptero!!.setBackgroundResource(R.drawable.ic_success)
                binding.chxSi.isEnabled = true

            }


        } else {
        }
    }


    fun setRecyclerViewAnotaciones(
        listaAnotaciones: ArrayList<Anotacion>,
    ) {
        val adapter = PreVueloListaAnotacionesAdapter( listaAnotaciones)
        recyclerview!!.adapter = adapter
        adapter.onItemClick = { anotacion ->
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

        val etUsuario = dialog.findViewById(R.id.etUsuarioCredenciales) as EditText
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
                        if(idResponsable.equals(empleado.id_cloud))
                        {
                            userExist = true
                        }
                    }
                }
            }
            if(userExist)
            {
                dialog.dismiss()
                binding.signaturePad!!.isEnabled = false
                binding.llFirmaValidada!!.visibility = View.VISIBLE
                fimaValidada = true
                firmaResponsable = binding.signaturePad!!.transparentSignatureBitmap
                PreVueloTabsFragment.firmaResponsable = firmaResponsable

            }
            else
            {
                PreVueloTabsFragment.firmaResponsable = null
                showErrorDialog("Usuario inválido")
                binding.llFirmaValidada!!.visibility = View.GONE
                fimaValidada = false
            }


        }

        val noBtn = dialog.findViewById(R.id.btnNo) as RelativeLayout
        noBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


    fun saveBitmapOnLocalStorage(nombreDocumento:String,bitmap: Bitmap) {
        try {
            val file: File = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // For Android 10+ (API 29+), use scoped storage
                val mediaDir = File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), Constants.SAVE_FILE.CARPETA_GENERAL + "/" + Constants.SAVE_FILE.CARPETA_FIRMA)
                if (!mediaDir.exists()) {
                    mediaDir.mkdirs()
                }
                File(mediaDir, nombreDocumento + ".png")
            } else {
                // For Android 9 and below, use external storage with permissions
                val root = Environment.getExternalStorageDirectory().toString()
                val fileee: File = File("$root/" + Constants.SAVE_FILE.CARPETA_GENERAL + "/" + Constants.SAVE_FILE.CARPETA_FIRMA)
                if (!fileee.exists()) {
                    fileee.mkdirs()
                }
                File(fileee, nombreDocumento + ".png")
            }

            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback: try to save to app's internal storage
            try {
                val internalDir = File(context?.filesDir, Constants.SAVE_FILE.CARPETA_GENERAL + "/" + Constants.SAVE_FILE.CARPETA_FIRMA)
                if (!internalDir.exists()) {
                    internalDir.mkdirs()
                }
                val file = File(internalDir, nombreDocumento + ".png")
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
                out.flush()
                out.close()
            } catch (fallbackException: Exception) {
                fallbackException.printStackTrace()
            }
        }
    }


    private fun showDialogBorrarFirma() {
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

            binding.signaturePad!!.isEnabled = true
            binding.signaturePad!!.clear()
            binding.llFirmaValidada!!.visibility = View.GONE
            responsableFirmo = false
            dialog.dismiss()

        }

        val noBtn = dialog.findViewById(R.id.btnNo) as RelativeLayout
        noBtn.setOnClickListener {

            dialog.dismiss()

        }
        dialog.show()
    }


    private fun showDialogFormatoSeCerrar() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_formato_se_cerrara)
        dialog.getWindow()!!.getAttributes().windowAnimations = R.style.DialogAnimation

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.attributes.alpha = 1f
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val btnOk = dialog.findViewById(R.id.btnOk) as RelativeLayout
        btnOk.setOnClickListener {

            dialog.dismiss()

        }
        dialog.show()
    }

    fun getNombreFormato(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(Constants.SHARED_PREFERENCES.FORMATO, MODE_PRIVATE)
        val text = sharedPreferences.getString(Constants.SHARED_PREFERENCES.NOMBRE_FORMATO, "")
        return text
    }


    fun validatePOSTVUELO()
    {
        if(getNombreFormato(requireContext()).equals("POST-VUELO"))
        {
            binding.btnCerrarMomentaneamente!!.visibility = View.VISIBLE
            binding.llEntregaraAOperaciones!!.visibility = View.GONE
        }
        else
        {
            binding.btnCerrarMomentaneamente!!.visibility = View.GONE
            binding.llEntregaraAOperaciones!!.visibility = View.VISIBLE
        }
    }



}