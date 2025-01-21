package com.helisur.helisurapp.ui.mantenimiento

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.helisur.helisurapp.R
import com.helisur.helisurapp.databinding.ActivityMantenimientoBinding
import com.helisur.helisurapp.domain.util.BaseActivity
import com.helisur.helisurapp.domain.util.SessionUserManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivityMantenimiento : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMantenimientoBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMantenimientoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        drawerSetListMenuItems()
        drawerSetHeader()
        disableBackButton()
    }


    fun drawerSetListMenuItems() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.item_formatos, R.id.item_mecanicas, R.id.item_cartera, R.id.item_cerrar
            ), binding.drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

    }


    fun drawerSetHeader() {
        val sessionManager = SessionUserManager(baseContext)
        var nombre: String =
            sessionManager.getNombres()!! + " " + sessionManager.getApellidoPaterno()!!
        var roll: String = sessionManager.getRol()!!
        val headerView: View = binding.navView.getHeaderView(0)
        val navUsername = headerView.findViewById<View>(R.id.header_drawer_nombre) as TextView
        val navUserRol = headerView.findViewById<View>(R.id.header_drawer_rol) as TextView
        navUsername.text = nombre
        navUserRol.text = roll
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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



    fun drawersetRedirections() {
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_formatos -> {
                    // addFragment(FragmentFactory.Screens.SERVICE_HISTORY_FRAGMENT,null)
                    // Toast.makeText(this, Constants.ERROR.ERROR, Toast.LENGTH_SHORT).show()
                    // loadTabHomeFragment()
                    true
                }

                R.id.item_mecanicas -> {
                    // addFragment(FragmentFactory.Screens.SERVICE_HISTORY_FRAGMENT,null)
                    //   Toast.makeText(this, "Mecanicas", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.item_cartera -> {
                 //   next(CarteraClientesActivity::class.java, null)
                    true
                }

                R.id.item_cerrar -> {
                    showDialog("¿Desea cerrar sesión?")
                    true
                }

                else -> {
                    false
                }
            }
        }
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