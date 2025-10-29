package com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.helisur.helisurapp.databinding.P7Binding
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo.PreVueloTabsFragment
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.ui.Inspecion100horasTabs

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Pagina7  : Fragment() {

    var className = "Pagina1"
    private var _binding: P7Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = P7Binding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        clickListener()
        return root
    }

    fun initUI() {



    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            Inspecion100horasTabs.tvPaginaFormato.text = "Página PM-7"
        } else {
        }
    }


    fun clickListener()
    {

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
        _binding = null
    }


    override fun onResume() {
        super.onResume()
        var prueba = ""
    }



}