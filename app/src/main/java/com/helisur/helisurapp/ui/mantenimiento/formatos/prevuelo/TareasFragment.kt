package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.helisur.helisurapp.R
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneEstacionesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtienePrevuelosRealizadosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasDataTableCloudResponse
import com.helisur.helisurapp.databinding.FragmentTareasBinding
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.mantenimiento.formatos.FormatosViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TareasFragment : Fragment() {

    var className = "TareasFragment"
    private lateinit var binding: FragmentTareasBinding
    var loading: TransparentProgressDialog? = null

    var showDetail = false
    var isChecked = false


    var showDetailAbuelo = false
    var showDetailPadre = false
    var showDetailhijo = false
    var showDetailhijo2 = false

    var showDetailAbuelo2 = false
    var showDetailPadre2 = false
    var showDetailhijo222 = false
    var showDetailhijo2222 = false


    private val formatosViewModel: FormatosViewModel by viewModels()
    private var sistemasList: ArrayList<ObtieneSistemasDataTableCloudResponse>? = null
    private var tareasList: ArrayList<ObtieneTareasDataTableCloudResponse>? = null

    var adapaterSistemas:ListaSistemasAdapter?= null

    var showDetailSistemas = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTareasBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        observers()
        clickListener()
        return root
    }

    fun initUI() {
        loading = TransparentProgressDialog(requireContext())
        formatosViewModel.obtieneSistemas(getFormato(requireContext())!!)

  //      sistemasList= arrayListOf()
  //      sistemasList!!.add(ObtieneSistemasDataTableCloudResponse("1","2","3"))
  //      setRecyclerViewSistemas(sistemasList!!,null)

    }


    fun clickListener()
    {

        binding.tvAtras.setOnClickListener {
            TabsPreVuelo.viewPager.setCurrentItem(Constants.TABS_PRE_VUELO.AERONAVE_ANTECEDENTE_REQUERIMIENTO)
        }

        binding.tvSiguiente.setOnClickListener {
            TabsPreVuelo.viewPager.setCurrentItem(Constants.TABS_PRE_VUELO.ANOTACIONES)
        }


        binding.contenedorSistemas!!.setOnClickListener {

            if(showDetailSistemas)
            {
                showDetailSistemas = false
                binding.rvSistemas!!.visibility = View.GONE
            }
            else
            {
                showDetailSistemas = true
                binding.rvSistemas!!.visibility = View.VISIBLE
            }

        }



    }


    fun getFormato(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES.FORMATO, MODE_PRIVATE)
        val text = sharedPreferences.getString(Constants.SHARED_PREFERENCES.ID_FORMATO, "")
        return text
    }

    fun getNombreFormato(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES.FORMATO, MODE_PRIVATE)
        val text = sharedPreferences.getString(Constants.SHARED_PREFERENCES.NOMBRE_FORMATO, "")
        return text
    }

    fun observers(){

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

        formatosViewModel.formatosState.observe(viewLifecycleOwner, Observer {
            try {
                if (it.toString().contains(Constants.ERROR.SUCCESS)) {
                } else {
                    if (it.toString().contains(Constants.ERROR.FAILURE)) {
                        var messageError = it.toString().replace("FAILURE","ERROR")
                        Log.e(className,messageError )
                        showErrorDialog(messageError)
                    }
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })


        formatosViewModel.responseObtieneSistemas.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    sistemasList = ArrayList(it.data!!.table)
                    setRecyclerViewSistemas(sistemasList!!,null)

                } else {
                    Log.e(className, Constants.ERROR.ERROR)
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })


        formatosViewModel.responseObtieneTareas.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    tareasList = ArrayList(it.data!!.table)
                    setRecyclerViewSistemas(sistemasList!!,tareasList)

                } else {
                    Log.e(className, Constants.ERROR.ERROR)
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })



    }



    fun setRecyclerViewSistemas(listaSistemas: ArrayList<ObtieneSistemasDataTableCloudResponse>,listaTareas: ArrayList<ObtieneTareasDataTableCloudResponse>?) {
        val recyclerview = binding.rvSistemas
        recyclerview!!.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ListaSistemasAdapter(requireContext(),listaSistemas,listaTareas)
     //   val adapter = ListaSistemasAdapter(listaSistemas)
        recyclerview.adapter = adapter
       // adapaterSistemas = adapter

        adapter.onItemClick = { sistema ->

            formatosViewModel.obtieneTareas(sistema.codigoSistema)

        }
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
        //_binding = null
    }


    override fun onResume() {
        super.onResume()
        var prueba = ""
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            //  val sessionManager = SessionUserManager(requireContext())
            //  cocursosViewModel.listaPeriodos(sessionManager!!.getToken()!!)
        } else {
        }
    }



}