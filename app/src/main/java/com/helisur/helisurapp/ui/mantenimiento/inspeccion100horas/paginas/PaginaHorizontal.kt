package com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import com.helisur.helisurapp.databinding.InicialFragmentMantenimientoBinding
import com.helisur.helisurapp.databinding.P14Binding
import com.helisur.helisurapp.databinding.PruebaBinding
import com.helisur.helisurapp.domain.util.ErrorMessageDialog

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaginaHorizontal  : Fragment() {

    var className = "Pagina14"
    private var _binding: P14Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = P14Binding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        clickListener()
        return root
    }

    fun initUI() {


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