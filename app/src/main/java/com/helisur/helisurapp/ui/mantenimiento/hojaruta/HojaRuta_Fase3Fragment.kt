package com.helisur.helisurapp.ui.mantenimiento.hojaruta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.helisur.helisurapp.databinding.FragmentCabeceraHojaRutaBinding
import com.helisur.helisurapp.databinding.FragmentHojaRutaFaseUnoUnoBinding
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HojaRuta_Fase3Fragment : Fragment() {

    var className = "CabeceraFragment"
    private lateinit var binding: FragmentHojaRutaFaseUnoUnoBinding
    var loading: TransparentProgressDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHojaRutaFaseUnoUnoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        clickListener()
        return root
    }

    fun initUI() {
        loading = TransparentProgressDialog(requireContext())

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