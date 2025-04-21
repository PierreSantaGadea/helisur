package com.helisur.helisurapp.ui.hojaruta

import android.content.Context
import android.content.SharedPreferences
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
import com.helisur.helisurapp.data.cloud.hojaruta.model.response.ObtieneHojasRutaDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.hojaruta.model.response.ObtieneListaActividadesPorHojaRutaCloudResponse
import com.helisur.helisurapp.data.cloud.hojaruta.model.response.ObtieneListaActividadesPorHojaRutaDataTableCloudResponse
import com.helisur.helisurapp.data.repository.HojaRutaRepository
import com.helisur.helisurapp.data.repository.UsuarioRepository
import com.helisur.helisurapp.databinding.FragmentActividadesHojaRutaBinding
import com.helisur.helisurapp.domain.model.Empleado
import com.helisur.helisurapp.domain.model.FormatoRegistro
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.ErrorMessageDialog

import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.login.LoginViewModel
import com.helisur.helisurapp.ui.mantenimiento.formatos.ListaFormatosDiscrepanciasAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class NuevaHojaRutaActividadesFragment : Fragment() {

    var className = "NuevaHojaRutaActividadesFragment"
    private lateinit var binding: FragmentActividadesHojaRutaBinding
    var loading: TransparentProgressDialog? = null

    var listaActividadesHojaRuta = ArrayList<ObtieneListaActividadesPorHojaRutaDataTableCloudResponse>()
    var nroHojaRuta:String? = ""
    private val hojasRutaViewModel: HojaRutaViewModel by viewModels()
    private val usuarioViewModel: LoginViewModel by viewModels()
    private var empleadoListDB: ArrayList<Empleado>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentActividadesHojaRutaBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        observers()
        clickListener()
        return root
    }

    fun initUI() {
        loading = TransparentProgressDialog(requireContext())

        var prefs: SharedPreferences = requireContext().getSharedPreferences("HOJARUTA", Context.MODE_PRIVATE)
        usuarioViewModel.getEmpleadosListDB()
        nroHojaRuta = prefs.getString("nroHojaRuta", "")

    }

    fun clickListener()
    {
        binding.tvAtras.setOnClickListener {

            TabsHojaRuta.viewPager.setCurrentItem(0)
        }
    }

    private fun observers() {


        hojasRutaViewModel.isLoading.observe(viewLifecycleOwner, Observer {
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


        hojasRutaViewModel.hojaRutaState.observe(viewLifecycleOwner, Observer {
            if (it.toString().contains(Constants.ERROR.SUCCESS)) {
            } else {
                if (it.toString().contains(Constants.ERROR.FAILURE)) {
                    var message = it.toString().replace("FAILURE(Error=","")
                    var messagFinal = message.replace(")","")
                    showErrorDialog(messagFinal)
                    Log.e(className, it.toString())
                }
            }
        })


        hojasRutaViewModel.responseObtieneListaActividadesPorHojaRuta.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                listaActividadesHojaRuta = ArrayList(it.data!!.table!!)
                setRecyclerViewHojasRutaActividades(listaActividadesHojaRuta!!)

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        usuarioViewModel.responseGetEmpleadoListDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                empleadoListDB = ArrayList(it)
                hojasRutaViewModel.obtieneListaActividadesPorHojaRuta(nroHojaRuta!!)

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


    }


    fun setRecyclerViewHojasRutaActividades(lista: ArrayList<ObtieneListaActividadesPorHojaRutaDataTableCloudResponse>) {
        val recyclerview = binding.rvActividades
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ListaActividadesHojasRutaAdapter(lista,requireContext(),empleadoListDB!!)
        recyclerview.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                recyclerview.context,
                R.drawable.divider
            )!!
        )

      //  recyclerview.addItemDecoration(dividerItemDecoration)

        adapter.onItemClick = { hojaRuta ->

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
     //   _binding = null
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