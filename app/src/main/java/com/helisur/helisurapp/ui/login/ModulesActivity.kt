package com.helisur.helisurapp.ui.login

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.helisur.helisurapp.R
import com.helisur.helisurapp.databinding.ActivityModulosBinding
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import com.helisur.helisurapp.domain.util.BaseActivity
import com.helisur.helisurapp.domain.util.ConnectivityRepository
import com.helisur.helisurapp.domain.util.FormatoBorradorManager
import com.helisur.helisurapp.domain.util.InternetViewModel
import com.helisur.helisurapp.domain.util.ServiceSyncData
import com.helisur.helisurapp.ui.mantenimiento.MainActivityMantenimiento
import com.helisur.helisurapp.ui.hojaruta.ListaHojaRutaActivity
import com.helisur.helisurapp.ui.mantenimiento.formatos.postvuelo.PostVueloActivity
import com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo.PreVueloActivity

@AndroidEntryPoint
class ModulesActivity : BaseActivity() {

    private lateinit var binding: ActivityModulosBinding
    private val loginViewModel: LoginViewModel by viewModels()
    var passView: Boolean = false
    var loading: TransparentProgressDialog? = null
    var className = "ModulesActivity"
    private var internetViewModel: InternetViewModel? = null
    var online: Boolean? = null
    var syncNow: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModulosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        clickListener()
        disableBackButton()
        observers()

     /*   if(FormatoBorradorManager(this).getHasBorrador()!!)
        {
            showDialog("")
        }
*/
    }

    fun beginService() {
        Intent(applicationContext, ServiceSyncData::class.java).also {
            it.action = ServiceSyncData.Actions.START.toString()
            startService(it)
        }
    }

    fun observers() {
        internetViewModel!!.isOnline.observe(this) { isOnline ->
            if (isOnline) {
                // Handle online state
                online = true
                //intent to syncActivity
                if (syncNow) {
                    // next(SyncActivity::class.java,null)
                    beginService()
                    syncNow = false
                }

            } else {
                // Handle offline state
                online = false
                syncNow = true
            }
        }
    }


    fun initUI() {

        loading = TransparentProgressDialog(this)
        internetViewModel = InternetViewModel(ConnectivityRepository(baseContext))
    }


    fun clickListener() {

        binding.llMantenimeinto.setOnClickListener { view ->
            // if(validations())
            next(MainActivityMantenimiento::class.java, null)
        }

        binding.llsms!!.setOnClickListener { view ->
            // if(validations())
            next(ListaHojaRutaActivity::class.java, null)
        }


    }


    fun disableBackButton() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // No hagas nada aquí para deshabilitar el botón "Atrás"
            }
        })
    }

    private fun showDialog(title: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_formatoborrador)
        dialog.getWindow()!!.getAttributes().windowAnimations = R.style.DialogAnimation

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.attributes.alpha = 1f
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val body = dialog.findViewById(R.id.tvMensajeInfo) as TextView
    //    body.text = title

        val yesBtn = dialog.findViewById(R.id.btnSi) as RelativeLayout
        yesBtn.setOnClickListener {

            var tipoFormato = FormatoBorradorManager(this).getTipoFormato()
            if(tipoFormato.equals("PREVUELO"))
            {

                FormatoBorradorManager(this).saveDesdeBorrador(true)
                val intent = Intent (this, PreVueloActivity::class.java)
                this.startActivity(intent)
            }
            else
            {
                FormatoBorradorManager(this).saveDesdeBorrador(true)
                val intent = Intent (this, PostVueloActivity::class.java)
                this.startActivity(intent)
            }



        }

        val noBtn = dialog.findViewById(R.id.btnNo) as RelativeLayout
        noBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


}