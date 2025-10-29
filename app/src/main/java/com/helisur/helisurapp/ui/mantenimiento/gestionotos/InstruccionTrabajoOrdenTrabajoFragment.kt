package com.helisur.helisurapp.ui.mantenimiento.gestionotos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.helisur.helisurapp.databinding.FragmentDetalleOrdenDatosGeneralesBinding
import com.helisur.helisurapp.databinding.FragmentDetalleOrdenInstruccionTrabajoBinding
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class InstruccionTrabajoOrdenTrabajoFragment : Fragment() {

    var className = "DatosGeneralesOrdenTrabajoFragment"
    private lateinit var binding: FragmentDetalleOrdenInstruccionTrabajoBinding
    var loading: TransparentProgressDialog? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetalleOrdenInstruccionTrabajoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }




}