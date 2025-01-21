package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.helisur.helisurapp.databinding.FragmentTareasBinding
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TareasFragment : Fragment() {

    var className = "TareasFragment"
    private lateinit var binding: FragmentTareasBinding
    var loading: TransparentProgressDialog? = null

    var showDetail = false
    var isChecked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTareasBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        //   observers()
        clickListener()
        return root
    }

    fun initUI() {
        loading = TransparentProgressDialog(requireContext())
        ///   val sessionManager = SessionUserManager(requireContext())
        //  val tokennn = sessionManager.getToken()!!
        //  cocursosViewModel.listaPeriodos(sessionManager.getToken()!!)
        //   llenaLista()
        //   setRecyclerView(concursosList)
        //   setSpinnerPeriodo()
    }


    fun clickListener()
    {
        binding.item1.setOnClickListener {

            if(showDetail)
            {
                binding.llDetalle.visibility = View.GONE
                showDetail = false
            }
            else{
                binding.llDetalle.visibility = View.VISIBLE
                showDetail = true
            }

        }


        binding.cbxNoAplica.setOnClickListener {

            if(binding.cbxNoAplica.isChecked)
            {
                binding.llMotivo.visibility = View.VISIBLE
            }
            else
            {
                if(!binding.cbxReporRTV.isChecked && !binding.cbxReporDanosMenores.isChecked && !binding.cbxReporMELMDS.isChecked)
                {
                    binding.llMotivo.visibility = View.GONE
                }
            }

        }

        binding.cbxReporRTV.setOnClickListener {

            if(binding.cbxReporRTV.isChecked)
            {
                binding.llMotivo.visibility = View.VISIBLE
            }
            else
            {
                if(!binding.cbxNoAplica.isChecked && !binding.cbxReporDanosMenores.isChecked && !binding.cbxReporMELMDS.isChecked)
                {
                    binding.llMotivo.visibility = View.GONE
                }
            }

        }


        binding.cbxReporDanosMenores.setOnClickListener {

            if(binding.cbxReporDanosMenores.isChecked)
            {
                binding.llMotivo.visibility = View.VISIBLE
            }
            else
            {
                if(!binding.cbxNoAplica.isChecked && !binding.cbxReporRTV.isChecked && !binding.cbxReporMELMDS.isChecked)
                {
                    binding.llMotivo.visibility = View.GONE
                }
            }

        }


        binding.cbxReporMELMDS.setOnClickListener {

            if(binding.cbxReporMELMDS.isChecked)
            {
                binding.llMotivo.visibility = View.VISIBLE
            }
            else
            {
                if(!binding.cbxNoAplica.isChecked && !binding.cbxReporRTV.isChecked && !binding.cbxReporDanosMenores.isChecked)
                {
                    binding.llMotivo.visibility = View.GONE
                }
            }

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