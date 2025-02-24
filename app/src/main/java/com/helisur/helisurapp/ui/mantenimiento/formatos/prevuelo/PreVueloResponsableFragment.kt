package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo

import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.helisur.helisurapp.R
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaFormatoCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaTareaCloudParameter
import com.helisur.helisurapp.databinding.FragmentResponsableBinding
import com.helisur.helisurapp.domain.model.Anotacion
import com.helisur.helisurapp.domain.model.DetalleFormatoRegistro
import com.helisur.helisurapp.domain.model.Empleado
import com.helisur.helisurapp.domain.model.FormatoRegistro
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.login.LoginViewModel
import com.helisur.helisurapp.ui.mantenimiento.MainActivityMantenimiento
import com.helisur.helisurapp.ui.mantenimiento.formatos.FormatosViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.GregorianCalendar
import java.util.UUID


@AndroidEntryPoint
class PreVueloResponsableFragment : Fragment() {

    var className = "PreVueloResponsableFragment"
    private lateinit var binding: FragmentResponsableBinding
    var loading: TransparentProgressDialog? = null
    private val loginViewModel: LoginViewModel by viewModels()
    private val formatosViewModel: FormatosViewModel by viewModels()
    var dialogg:Dialog? = null
    var tareasObservados: ArrayList<GuardaTareaCloudParameter>? = null
    var recyclerview:RecyclerView?=null

  //  var empleadosList: ArrayList<ObtieneEmpleadosDataTableCloudResponse>? = null
    var empleadosList: ArrayList<Empleado>? = null
    var empleadosListAll: ArrayList<Empleado>? = null

    var idResponsable = ""
    var licenciaResponsable = ""
    var urlFirmaResponsable = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentResponsableBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        clicListener()
        observers()
        validatePOSTVUELO()
        root!!.post {
         //   binding.signaturePad!!.signatureBitmap.height = 20
          //  binding.signaturePad!!.signatureBitmap.width = 20
        }
        return root
    }

    fun initUI() {
        loading = TransparentProgressDialog(requireContext())
//        binding.signaturePad!!.autofillId!!

        loginViewModel.getEmpleadosListDB()
    //    loginViewModel.obtieneEmpleados("00091")

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



    fun observers()
    {

        loginViewModel.responseObtieneTokenCloud.observe(viewLifecycleOwner, Observer {
            try {
                Toast.makeText(getActivity(),"Firma validada",Toast.LENGTH_SHORT).show()
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
                    var mensaje = it.toString().replace("FAILURE(Error=","")
                    showErrorDialog(mensaje.replace(")",""))
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

                    empleadosList = arrayListOf()
                    var empleadosListaTotal: ArrayList<Empleado>? = ArrayList(it)
                    empleadosListAll=ArrayList(it)

                    for(item in empleadosListaTotal!!)
                    {
                        if(item.codigoArea.equals("00091"))
                        {
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


    }

    fun setSpinnerEmpleados(
    ) {
        var spinnerTipo = binding.spiEmpleados

      //  val spinnerArray: MutableList<String> = ArrayList()
        val spinnerArrayImages: MutableList<Int> = ArrayList()

        val spinnerArray: MutableList<String> = ArrayList()
        spinnerArray.add("Seleccione empleado")
        spinnerArrayImages.add(R.drawable.empty)
        for(item in empleadosList!!)
        {
            spinnerArray.add(item.nombreCompleto!!)
            spinnerArrayImages.add(R.drawable.ic_user)
        }
        val adapter = SpinenrItemEmpleado(requireContext(),0,
            spinnerArray.toTypedArray(), spinnerArrayImages.toTypedArray())

      //  val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, spinnerArray)
      //  adapter.setDropDownViewResource(R.layout.spinner_item)
        spinnerTipo!!.adapter = adapter

        spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                if (position == 0) {
                    idResponsable = ""
                    binding.etLicencia!!.setText("")
                } else {
                    idResponsable  =  empleadosList!![position-1].id_cloud!!
                    licenciaResponsable = empleadosList!![position-1].licencia!!
                    TabsPreVuelo.formatoParameter.idEmpleadoResponsable = idResponsable
                    TabsPreVuelo.formatoParameter.urlFirmaResponsable = urlFirmaResponsable
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


    fun sendToStorage()
    {
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


    fun clicListener()
    {

        binding.tvAtras.setOnClickListener {
            TabsPreVuelo.viewPager.setCurrentItem(Constants.TABS_PRE_VUELO.SISTEMAS)
        }

        binding.tvSiguiente.setOnClickListener {
            TabsPreVuelo.viewPager.setCurrentItem(Constants.TABS_PRE_VUELO.FIRMA_RESPONSABLE)
        }


        binding.chxNo!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                showDialogFormatoSeCerrar()
                binding.chxSi.isChecked = false
                binding.tvSiguiente.visibility = View.GONE
                binding.btnCerrarMomentaneamente!!.visibility = View.VISIBLE
            } else {

            }
        }


        binding.chxSi!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.chxNo.isChecked = false
                binding.tvSiguiente.visibility = View.VISIBLE
                binding.btnCerrarMomentaneamente!!.visibility = View.GONE
            } else {

            }
        }

        binding.btnCerrarMomentaneamente!!.setOnClickListener {


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
            val gc: GregorianCalendar = GregorianCalendar()
            val pattern = "yyyy-MM-dd HH:mm:ss"
            val simpleDateFormat = SimpleDateFormat(pattern)
            simpleDateFormat.calendar = gc
            fechaHoy = simpleDateFormat.format(gc.time)

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
                    var detalle:DetalleFormatoRegistro = DetalleFormatoRegistro("",uniqueID,item.codigoRegistroFormato,item.codigoTarea,item.nombreTarea,item.codigoReportaje,
                        "",item.indicadorSN,"",fechaHoy,"")

                    listaDetalleDB.add(detalle)
                }

                formatosViewModel.insertDetalleFormatoRegistroDB(listaDetalleDB)
            }


        }




        binding.btnGuardarFirma!!.setOnClickListener{

            showDialogLogin()

        }


        binding.btnBorrarFirma!!.setOnClickListener{

            showDialogBorrarFirma()

        }





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

            var listaAnotaciones:ArrayList<Anotacion> = arrayListOf()

            for(itemSistema in TareasFragment.sistemasList!!)
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
                var iduser = TabsPreVuelo.idUsuario
                var helicopteroAPTO = true
                for(tareaObservada in listaAnotaciones)
                {
                    if(tareaObservada.reportaje_NoAplica)
                    {
                        tareasObservados!!.add(GuardaTareaCloudParameter("0",tareaObservada.codigoTarea!!,tareaObservada.id_NoAplica,"1",iduser,tareaObservada.reportaje_Motivo!!,tareaObservada.nombreTarea!!))
                    }

                    if(tareaObservada.reportaje_RTV)
                    {
                        helicopteroAPTO = false
                        tareasObservados!!.add(GuardaTareaCloudParameter("0",tareaObservada.codigoTarea!!,tareaObservada.id_RTV,"1",iduser,tareaObservada.reportaje_Motivo!!,tareaObservada.nombreTarea!!))
                    }

                    if(tareaObservada.reportaje_DanosMenores)
                    {
                        tareasObservados!!.add(GuardaTareaCloudParameter("0",tareaObservada.codigoTarea!!,tareaObservada.id_DanosMenores,"1",iduser,tareaObservada.reportaje_Motivo!!,tareaObservada.nombreTarea!!))
                    }

                    if(tareaObservada.reportaje_MELMDS)
                    {
                        tareasObservados!!.add(GuardaTareaCloudParameter("0",tareaObservada.codigoTarea!!,tareaObservada.id_MELMDS,"1",iduser,tareaObservada.reportaje_Motivo!!,tareaObservada.nombreTarea!!))
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


                TabsPreVuelo.formatoParameter.listaTareas = tareasObservados

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
        listaAnotaciones: ArrayList<Anotacion>
    ) {
        val adapter = ListaAnotacionesAdapter( listaAnotaciones)
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
                        userExist = true
                    }
                }
            }
            if(userExist)
            {
                dialog.dismiss()
            }
            else
            {
                showErrorDialog("Usuario o contraseña incorrectos")
            }


        }

        val noBtn = dialog.findViewById(R.id.btnNo) as RelativeLayout
        noBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
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