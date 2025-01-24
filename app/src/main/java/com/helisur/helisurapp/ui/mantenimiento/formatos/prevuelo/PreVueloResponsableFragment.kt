package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.helisur.helisurapp.R
import com.helisur.helisurapp.databinding.FragmentResponsableBinding
import com.helisur.helisurapp.domain.model.Anotacion
import com.helisur.helisurapp.domain.model.Sistema
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.login.LoginViewModel
import com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo.TareasFragment.Companion.sistemasList
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream


@AndroidEntryPoint
class PreVueloResponsableFragment : Fragment() {


    private lateinit var binding: FragmentResponsableBinding
    var loading: TransparentProgressDialog? = null

    private val loginViewModel: LoginViewModel by viewModels()

    var dialogg:Dialog? = null

    var showDetail1 = false
    var showDetail2 = false

    var hasReportajes = false

    var sistemasObservados: ArrayList<Sistema>? = null

    var recyclerview:RecyclerView?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentResponsableBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        clicListener()
        observers()
        return root
    }

    fun initUI() {
        loading = TransparentProgressDialog(requireContext())

        recyclerview = binding.rvAnotaciones
        recyclerview!!.layoutManager = LinearLayoutManager(requireContext())

        ///   val sessionManager = SessionUserManager(requireContext())
        //  val tokennn = sessionManager.getToken()!!
        //  cocursosViewModel.listaPeriodos(sessionManager.getToken()!!)
        //   llenaLista()
        //   setRecyclerView(concursosList)
        //   setSpinnerPeriodo()
    }


    fun observers()
    {

        loginViewModel.responseObtieneTokenCloud.observe(viewLifecycleOwner, Observer {
            try {

                binding.signaturePad!!.isEnabled = false
                dialogg!!.dismiss()

            } catch (e: Exception) {
              //  Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })


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





        binding.chxNo.setOnClickListener {
            showDialog()
        }


        binding.chxSi.setOnClickListener {

            binding.tvSiguiente.visibility = View.VISIBLE
        }



        binding.btnGuardarFirma!!.setOnClickListener{

            showDialogLogin()

        }


        binding.btnBorrarFirma!!.setOnClickListener{

            showDialogBorrarFirma()

        }





    }

    fun showDialog()
    {

        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {}
                    DialogInterface.BUTTON_NEGATIVE -> {}
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage("Este prevuelo se cerrara sin entrega a operaciones").setPositiveButton("Ok", dialogClickListener)
            .setNegativeButton("Cancelar", dialogClickListener).show()

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
      //  _binding = null
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
                setRecyclerViewSistemas(listaAnotaciones)
                binding.rvAnotaciones!!.visibility = View.VISIBLE
                binding.tituloAnotaciones!!.visibility = View.VISIBLE
                binding.condicionhelicoptero!!.text = "El helicóptero se encuentra en condición no satisfactoria"
                binding.condicionhelicoptero!!.setTextColor(Color.parseColor("#e11f21"));
            }
            else
            {
                binding.rvAnotaciones!!.visibility = View.GONE
                binding.tituloAnotaciones!!.visibility = View.GONE
                binding.condicionhelicoptero!!.text = "El helicóptero se encuentra en condición satisfactoria"
                binding.condicionhelicoptero!!.setTextColor(Color.parseColor("#1A4905"));
            }



        } else {
        }
    }


    fun setRecyclerViewSistemas(
        listaAnotaciones: ArrayList<Anotacion>
    ) {
        val adapter = ListaAnotacionesAdapter( listaAnotaciones)
        recyclerview!!.adapter = adapter
        adapter.onItemClick = { sistema ->

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

            var user = etUsuario.text
            var pass = etPass.text
       //     var user = "analista_app"
       //     var pass = "helisur2024."

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


}