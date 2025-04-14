package com.helisur.helisurapp.ui.hojaruta

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.helisur.helisurapp.databinding.FragmentCabeceraHojaRutaBinding
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.SessionUserManager.Companion.USER
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo.PreVueloTabsFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NuevaHojaRutaCabeceraFragment : Fragment() {

    var className = "CabeceraFragment"
    private lateinit var binding: FragmentCabeceraHojaRutaBinding
    var loading: TransparentProgressDialog? = null

    var nroHojaRuta:String? = ""
    var servMantenimiento:String? = ""
    var cliente:String? = ""
    var nroRequerimiento:String? = ""
    var modeloAeronave:String? = ""
    var matriculaAeronave:String? = ""
    var ubicacion:String? = ""
    var plazo:String? = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCabeceraHojaRutaBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        clickListener()
        return root
    }

    fun initUI() {
        loading = TransparentProgressDialog(requireContext())
        getData()
    }


    fun getData()
    {
        var prefs: SharedPreferences = requireContext().getSharedPreferences("HOJARUTA", Context.MODE_PRIVATE)

        nroHojaRuta = prefs.getString("nroHojaRuta", "")
        servMantenimiento = prefs.getString("servMantenimiento", "")
        cliente = prefs.getString("cliente", "")
        nroRequerimiento = prefs.getString("nroRequerimiento", "")
        modeloAeronave = prefs.getString("modeloAeronave", "")
        matriculaAeronave = prefs.getString("matriculaAeronave", "")
        ubicacion = prefs.getString("ubicacion", "")
        plazo = prefs.getString("plazo", "")

        binding.tvNroHojaRuta.text = nroHojaRuta
        binding.tvServMantenimiento.text = servMantenimiento
        binding.tvCliente.text = cliente
        binding.tvNroRequerimiento.text = nroRequerimiento
        binding.tvModeloAeronave.text = modeloAeronave
        binding.tvMatriculaAeronave.text = matriculaAeronave
        binding.tvUbicacion.text = ubicacion


    }


    fun clickListener()
    {
        binding.tvSiguiente.setOnClickListener {

            TabsHojaRuta.viewPager.setCurrentItem(1)
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