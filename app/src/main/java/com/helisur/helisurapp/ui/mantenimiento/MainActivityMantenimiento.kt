package com.helisur.helisurapp.ui.mantenimiento

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.helisur.helisurapp.R
import com.helisur.helisurapp.databinding.ActivityMantenimientoBinding
import com.helisur.helisurapp.domain.util.BaseActivity
import com.helisur.helisurapp.domain.util.ConnectivityRepository
import com.helisur.helisurapp.domain.util.InternetViewModel
import com.helisur.helisurapp.domain.util.ServiceSyncData
import com.helisur.helisurapp.domain.util.SessionUserManager
import com.helisur.helisurapp.ui.login.LoginActivity
import com.helisur.helisurapp.ui.mantenimiento.formatos.EscogeAeronaveFragment
import com.helisur.helisurapp.ui.mantenimiento.formatos.FormatosDiscrepanciasFragment
import com.helisur.helisurapp.ui.sync.SyncActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivityMantenimiento : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMantenimientoBinding
    private var internetViewModel: InternetViewModel?=null
    var online:Boolean?=null
    var syncNow:Boolean = false

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }

    // Permission launcher for external storage permissions
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val writePermission = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: false
        val readPermission = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false
        
        if (writePermission && readPermission) {
            // Both permissions granted
            // You can add any initialization code here that requires these permissions
        } else {
            // Handle permission denial
            // You might want to show a message or disable certain features
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMantenimientoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        internetViewModel = InternetViewModel(ConnectivityRepository(baseContext))
        
        // Request external storage permissions
        requestExternalStoragePermissions()
        
        drawerSetListMenuItems()
        drawerSetHeader()
     //   disableBackButton()
     //   drawersetRedirections()
        observers()
      //  beginService()
    }

    private fun requestExternalStoragePermissions() {
        // Check if we're on Android 13+ (API 33+) where these permissions are not needed for scoped storage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For Android 13+, these permissions are not needed for scoped storage
            // The app can access files through the Storage Access Framework or scoped storage
            return
        }

        // For Android 11+ (API 30+), check for MANAGE_EXTERNAL_STORAGE permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!android.provider.Settings.System.canWrite(this)) {
                // Request MANAGE_EXTERNAL_STORAGE permission
                val intent = android.content.Intent(android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
                return
            }
        }

        // For Android 10 and below, request the traditional permissions
        val permissionsToRequest = mutableListOf<String>()
        
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (permissionsToRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    fun beginService()
    {
        Intent(applicationContext, ServiceSyncData::class.java).also{
            it.action = ServiceSyncData.Actions.START.toString()
            startService(it)
        }
    }


    fun observers()
    {
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


    fun drawerSetListMenuItems() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.item_formatos, R.id.item_discrepancias,R.id.item_historico,R.id.item_ejecucion,R.id.item_inicial, R.id.item_cerrar
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
        obtenerVersionApp(baseContext, headerView)
      //  navUsername.text = nombre
      //  navUserRol.text = roll
        navUsername.text = "Helicópteros del Sur S.A.".toUpperCase()
        navUserRol.text = ""
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun obtenerVersionApp(context: android.content.Context, view :View) {
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)

            val versionName = pInfo.versionName
            val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                pInfo.longVersionCode // API 28+
            } else {
                @Suppress("DEPRECATION")
                pInfo.versionCode
            }
            val navVersionText = view.findViewById<View>(R.id.header_drawer_version) as TextView
            navVersionText.text = "Versión: "+ versionCode + "."+versionName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
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
            val sessionManager = SessionUserManager(baseContext)
            sessionManager.saveUser("")
            sessionManager.savePass("")
            sessionManager.saveUserId("")
            sessionManager.saveUserNombres("")
            sessionManager.saveUserApellidoPaterno("")
            sessionManager.saveUserApellidoMaterno("")
            sessionManager.saveUserRol("")
            sessionManager.saveUserLogged(false)

            next(LoginActivity::class.java, null)
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

                R.id.item_cerrar -> {
                    showDialog("¿Desea cerrar sesión?")
                    true
                }
                R.id.item_discrepancias -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main, FormatosDiscrepanciasFragment())
                        .addToBackStack(null) // Optional: Add to back stack
                        .commit()

                    true
                }

                R.id.item_formatos -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main, EscogeAeronaveFragment())
                        .addToBackStack(null) // Optional: Add to back stack
                        .commit()
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