package com.helisur.helisurapp.ui.mantenimiento.formatos.postvuelo

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.helisur.helisurapp.databinding.FragmentTareasBinding
import com.helisur.helisurapp.domain.model.Reportaje
import com.helisur.helisurapp.domain.model.Sistema
import com.helisur.helisurapp.domain.model.Tarea
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.mantenimiento.formatos.FormatosViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostVueloTareasFragment : Fragment() {

    var className = "TareasFragment"
    private lateinit var binding: FragmentTareasBinding
    var loading: TransparentProgressDialog? = null
    private val formatosViewModel: FormatosViewModel by viewModels()

    var adapaterSistemas: PostVueloListaSistemasAdapter? = null
    var showDetailSistemas = false
    var posicionClick: Int? = null

    companion object {
        var sistemasList: ArrayList<Sistema>? = null
        var tareasList: ArrayList<Tarea>? = null
        var reportajesList: ArrayList<Reportaje>? = null

        fun getTareasRealizadas(): ArrayList<Tarea> = tareasList!!
        fun getSistemasRealizadas(): ArrayList<Sistema> = sistemasList!!

        fun getReportajesByTarea(idTarea: String): ArrayList<Reportaje> {
            val lista = arrayListOf<Reportaje>()
            for (item in reportajesList!!) {
                if (item.codigoTarea == idTarea) lista.add(item)
            }
            return if (lista.isEmpty()) reportajesList!! else lista
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTareasBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        observers()
        clickListener()
        binding.llHums!!.visibility = View.GONE
        return root
    }

    fun initUI() {
        loading = TransparentProgressDialog(requireContext())
        formatosViewModel.getSistemnasByFormatoDB(getFormato(requireContext())!!)
        formatosViewModel.getReportajesListDB()
    }

    fun clickListener() {
        binding.tvAtras.setOnClickListener {
            PostVueloTabsFragment.viewPager.setCurrentItem(Constants.TABS_PRE_VUELO.AERONAVE_ANTECEDENTE_REQUERIMIENTO)
        }
        binding.tvSiguiente.setOnClickListener {
            PostVueloTabsFragment.viewPager.setCurrentItem(Constants.TABS_PRE_VUELO.ANOTACIONES)
        }
        binding.contenedorSistemas!!.setOnClickListener {
            showDetailSistemas = !showDetailSistemas
            binding.rvSistemas!!.visibility = if (showDetailSistemas) View.VISIBLE else View.GONE
        }
    }

    fun getFormato(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(Constants.SHARED_PREFERENCES.FORMATO, MODE_PRIVATE)
        return sharedPreferences.getString(Constants.SHARED_PREFERENCES.ID_FORMATO, "")
    }

    fun getNombreFormato(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(Constants.SHARED_PREFERENCES.FORMATO, MODE_PRIVATE)
        return sharedPreferences.getString(Constants.SHARED_PREFERENCES.NOMBRE_FORMATO, "")
    }

    fun observers() {
        formatosViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            try {
                if (it) {
                    if (!loading!!.isShowing) loading!!.show()
                } else {
                    if (loading!!.isShowing) loading!!.dismiss()
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace()
                showErrorDialog(e.toString())
            }
        })

        formatosViewModel.formatosState.observe(viewLifecycleOwner, Observer {
            try {
                if (!it.toString().contains(Constants.ERROR.SUCCESS)) {
                    if (it.toString().contains(Constants.ERROR.FAILURE)) {
                        val messageError = it.toString()
                        Log.e(className, messageError)
                        showErrorDialog(messageError)
                        tareasList = arrayListOf()
                        adapaterSistemas?.updateItem(posicionClick!!, tareasList)
                    }
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace()
                showErrorDialog(e.toString())
            }
        })

        formatosViewModel.responseGetSistemaByFormatoDB.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    sistemasList = ArrayList(it)
                    setRecyclerViewSistemas(sistemasList!!)
                } else Log.e(className, Constants.ERROR.ERROR)
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace()
                showErrorDialog(e.toString())
            }
        })

        formatosViewModel.responseObtieneSistemas.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    sistemasList = it
                    setRecyclerViewSistemas(sistemasList!!)
                } else Log.e(className, Constants.ERROR.ERROR)
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace()
                showErrorDialog(e.toString())
            }
        })

        formatosViewModel.responseGetTareaBySistemaListDB.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    tareasList = ArrayList(it)
                    adapaterSistemas?.updateItem(posicionClick!!, tareasList!!)
                } else Log.e(className, Constants.ERROR.ERROR)
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace()
                showErrorDialog(e.toString())
            }
        })

        formatosViewModel.responseObtieneTareas.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    tareasList = it
                    adapaterSistemas?.updateItem(posicionClick!!, tareasList!!)
                } else Log.e(className, Constants.ERROR.ERROR)
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace()
                showErrorDialog(e.toString())
            }
        })

        formatosViewModel.responseGetReportajeListDB.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    reportajesList = ArrayList(it)
                } else Log.e(className, Constants.ERROR.ERROR)
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace()
                showErrorDialog(e.toString())
            }
        })
    }

    fun setRecyclerViewSistemas(listaSistemas: ArrayList<Sistema>) {
        val rv = binding.rvSistemas
        rv!!.layoutManager = LinearLayoutManager(requireContext())
        val adapter = PostVueloListaSistemasAdapter(requireContext(), listaSistemas)
        rv.adapter = adapter

        adapter.onItemClick = { sistema, pos ->
            posicionClick = pos
            // Si aún no se cargaron las tareas de este sistema, las pedimos
            if (sistema.tareas.isNullOrEmpty()) {
                formatosViewModel.getTareasBySistema(sistema.codigoSistema!!,getFormato(requireContext())!!)
            }
            // No colapsamos otros ítems: el adapter maneja su set de expandidos
        }
        adapaterSistemas = adapter
    }

    fun showErrorDialog(message: String?) {
        val bundle = Bundle()
        bundle.putString("errorMessage", message)
        val df = ErrorMessageDialog()
        df.arguments = bundle
        df.show(requireFragmentManager(), "")
    }
}
