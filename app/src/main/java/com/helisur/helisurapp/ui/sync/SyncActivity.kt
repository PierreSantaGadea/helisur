package com.helisur.helisurapp.ui.sync

import android.os.Bundle

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse

import com.helisur.helisurapp.databinding.ActivitySyncBinding
import com.helisur.helisurapp.domain.model.ModeloAeronave
import com.helisur.helisurapp.domain.model.toDomain
import com.helisur.helisurapp.domain.util.BaseActivity
import com.helisur.helisurapp.domain.util.Constants

import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.login.ModulesActivity
import com.helisur.helisurapp.ui.mantenimiento.AeronavesViewModel
import com.helisur.helisurapp.ui.mantenimiento.formatos.FormatosViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SyncActivity  : BaseActivity() {

    private val className = "SyncActivity"
    private lateinit var binding: ActivitySyncBinding
    private val aeronavesViewModel: AeronavesViewModel by viewModels()
    private val formatosViewModel: FormatosViewModel by viewModels()

    var listaModelosAeronaveDB:ArrayList<ModeloAeronave> = arrayListOf()
    var listaModelosAeronaveCloud:ArrayList<ObtieneAeronavesDataTableCloudResponse> = arrayListOf()
    var modelosAeronavesSync:Boolean = false

    var loading: TransparentProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySyncBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observers()
        disableBackButton()

        getAllDataFromCloud()
    }


    //PRIMERO ENVIAR DATA
    fun sendDataToCloud()
    {
        //ORDEN
        //GRABA FORMATO
        //ACTUALIZA FORMATO


    }


    //LUEGO RECIBIR DATA
    fun getAllDataFromCloud()
    {
       // binding.progressBar.progress = 50

        //ORDEN

        //MODELOS AERONAVE
        //AERONAVES
        //ESTACIONES

        //FORMATOS
        //SISTEMAS
        //TAREAS
        //REPORTAJES

        //FORMATOS REALIZADOS
        //REPORTAJES FORMATO REALIZADO


        setPerct(2)
        aeronavesViewModel.getModelosAeronavesListDB()

    }



    fun setPerct(value:Int)
    {
        binding.progressBar.progress = value
        binding.txtper.setText(value.toString()+"%")
    }


    fun observers()
    {

        aeronavesViewModel.responseGetModeloAeronaveListDB.observe(this, Observer {
            if (it != null) {
                setPerct(3)
                listaModelosAeronaveDB = ArrayList(it.data!!)
                aeronavesViewModel.getModeloAeronaveListCloud()
            } else {
                Log.e(className, Constants.ERROR.ERROR)
            }
        })



        aeronavesViewModel.responseGetModeloAeronaveListCloud.observe(this, Observer {
            if (it != null) {
                setPerct(6)
                listaModelosAeronaveCloud = ArrayList(it.data!!.table)
                aeronavesViewModel.deleteTableModeloAeronaveDB()

            } else {
                Log.e(className, Constants.ERROR.ERROR)
            }
        })

        aeronavesViewModel.responseDeleteModeloAeronaveListDB.observe(this, Observer {
            if (it.success) {
                setPerct(8)
                for(itemCloud in listaModelosAeronaveCloud)
                {
                    listaModelosAeronaveDB.add(itemCloud.toDomain())
                }
                if(listaModelosAeronaveDB.size>0)
                {
                    aeronavesViewModel.insertModeloAeronaveListDB(listaModelosAeronave = listaModelosAeronaveDB)
                }

                /*
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
                    //puede q haya actualizacion de uno o mas registros pero no se hara nada por ahora
                    modelosAeronavesSync = true
                    aeronavesViewModel.getEstacionesListCloud()
                }

                 */

            } else {
                Log.e(className, Constants.ERROR.ERROR)
            }
        })


        aeronavesViewModel.responseInsertModeloAeronaveListDB.observe(this, Observer {
            if (it.success) {
                setPerct(10)
                modelosAeronavesSync = true
                aeronavesViewModel.getEstacionesListCloud()
            } else {
                Log.e(className, Constants.ERROR.ERROR)
            }
        })






        aeronavesViewModel.responseGetEstacionesListCloud.observe(this, Observer {
            if (it != null) {
                setPerct(12)
                modelosAeronavesSync = true
              finish()
              //  aeronavesViewModel.getEstacionesListCloud()
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


    }











    fun disableBackButton() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
    }


}