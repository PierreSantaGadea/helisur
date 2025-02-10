package com.helisur.helisurapp.ui.login

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.databinding.ActivitySplashBinding
import com.helisur.helisurapp.domain.model.ModeloAeronave
import com.helisur.helisurapp.domain.model.toDomain
import com.helisur.helisurapp.domain.util.BaseActivity
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.SessionUserManager
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.mantenimiento.AeronavesViewModel
import com.helisur.helisurapp.ui.mantenimiento.formatos.FormatosViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity  : BaseActivity() {

    private val className = "SplashActivity"
    private lateinit var binding: ActivitySplashBinding
    private val aeronavesViewModel: AeronavesViewModel by viewModels()
    private val formatosViewModel: FormatosViewModel by viewModels()

    var listaModelosAeronaveDB:ArrayList<ModeloAeronave> = arrayListOf()
    var listaModelosAeronaveCloud:ArrayList<ObtieneAeronavesDataTableCloudResponse> = arrayListOf()
    var modelosAeronavesSync:Boolean = false

    var loading: TransparentProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getAllDataFromCloud()
        observers()
        timerSplash()
        disableBackButton()
    }




    fun getAllDataFromCloud()
    {
      //  aeronavesViewModel.obtieneAeronaves()
      //  aeronavesViewModel.getModelosAeronaves()
     //   formatosViewModel.obtieneFormatos()
     //   aeronavesViewModel.deleteTableModeloAeronaveDB()
    }


    fun observers()
    {
        /*
        aeronavesViewModel.isLoading.observe(this, Observer {
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

         */

        aeronavesViewModel.responseGetModeloAeronaveListDB.observe(this, Observer {
            if (it != null) {

                listaModelosAeronaveDB = ArrayList(it.data!!)
                aeronavesViewModel.getEstacionesListCloud()

            } else {
                Log.e(className, Constants.ERROR.ERROR)
            }
        })

        aeronavesViewModel.aeronavesState.observe(this, Observer {
            try {
                if (it.toString().contains(Constants.ERROR.SUCCESS)) {
                } else {
                    if (it.toString().contains(Constants.ERROR.FAILURE)) {
                        Log.e(className, Constants.ERROR.ERROR)
                    }
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })


        aeronavesViewModel.responseGetModeloAeronaveListCloud.observe(this, Observer {
            if (it != null) {
                listaModelosAeronaveCloud = ArrayList(it.data!!.table)
                if(listaModelosAeronaveCloud.size > listaModelosAeronaveDB.size)
                {
                    //hay nuevos registros
                    //mando a borrar tabla
                  //  aeronavesViewModel.deleteTableModeloAeronaveDB()
                    for(itemCloud in listaModelosAeronaveCloud)
                    {
                        var exist = false
                        for(itemDB in listaModelosAeronaveDB)
                        {
                            if(itemDB.id_cloud.equals(itemCloud.codigoModeloPuesto))
                            {
                                exist = true
                            }
                        }
                        if(!exist)
                        {
                            listaModelosAeronaveDB.add(itemCloud.toDomain())
                        }
                    }
                    if(listaModelosAeronaveDB.size>0)
                    {
                        aeronavesViewModel.insertModeloAeronaveListDB(listaModelosAeronave = listaModelosAeronaveDB)
                    }

                }
                else
                {
                    //puede q haya actualizacion de uno o mas registros
                }

            } else {
                Log.e(className, Constants.ERROR.ERROR)
            }
        })


        aeronavesViewModel.responseInsertModeloAeronaveListDB.observe(this, Observer {
            if (it.success) {
                modelosAeronavesSync = true
            } else {
                Log.e(className, Constants.ERROR.ERROR)
            }
        })


        aeronavesViewModel.responseDeleteModeloAeronaveListDB.observe(this, Observer {
            if (it.success) {


            } else {
                Log.e(className, Constants.ERROR.ERROR)
            }
        })



    }














    fun timerSplash() {
        val handler = Handler()
        handler.postDelayed({
            conditionToLogIn()
        }, 3000)
    }

    fun conditionToLogIn() {
        val sessionManager = SessionUserManager(baseContext)
        val userLogged = sessionManager.getLogged()
        if (userLogged!!) {
            next(ModulesActivity::class.java, null)
         //   next(MainActivityVendedor::class.java, null)
        } else {

            next(LoginActivity::class.java, null)
        }
    }

    fun disableBackButton() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
    }


}