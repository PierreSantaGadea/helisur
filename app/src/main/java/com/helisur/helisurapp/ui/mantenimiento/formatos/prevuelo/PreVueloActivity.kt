package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.navigation.ui.AppBarConfiguration
import com.helisur.helisurapp.R
import com.helisur.helisurapp.databinding.ActivityPrevueloBinding
import com.helisur.helisurapp.domain.util.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreVueloActivity  : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityPrevueloBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrevueloBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadTabHomeFragment()
    }



    fun disableBackButton() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // No hagas nada aquí para deshabilitar el botón "Atrás"
            }
        })
    }

    @SuppressLint("SuspiciousIndentation")
    fun loadTabHomeFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val homeFragment: TabsPreVuelo = TabsPreVuelo()
        fragmentTransaction.replace(binding.containerView.id, homeFragment)
        fragmentTransaction.commit()
    }

    private fun showDialog(title: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_logout)
        dialog.getWindow()!!.getAttributes().windowAnimations = R.style.DialogAnimation

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.attributes.alpha = 1f
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val body = dialog.findViewById(R.id.tvMensajeInfo) as TextView
        body.text = title

        val yesBtn = dialog.findViewById(R.id.btnSi) as RelativeLayout
        yesBtn.setOnClickListener {
            // logout()
        }

        val noBtn = dialog.findViewById(R.id.btnNo) as RelativeLayout
        noBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }




    fun playWithItemsMenu() {
        //   navView.menu.findItem(R.id.nav_gallery).isEnabled = false
        //    navView.menu.findItem(R.id.nav_gallery).isVisible = false
        /*
                val menu: Menu = binding.navView.getMenu()
                for (i in 1..10) {
                    menu.add("Runtime item $i")
                }

         */
    }


}