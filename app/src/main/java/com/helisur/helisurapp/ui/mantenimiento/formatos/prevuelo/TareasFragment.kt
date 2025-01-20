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


    var showDetailAbuelo = false
    var showDetailPadre = false
    var showDetailhijo = false
    var showDetailhijo2 = false

    var showDetailAbuelo2 = false
    var showDetailPadre2 = false
    var showDetailhijo222 = false
    var showDetailhijo2222 = false

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


        binding.contenedorAbuelo!!.setOnClickListener {

            if(showDetailAbuelo)
            {
                showDetailAbuelo = false
                binding.contenedorPadre!!.visibility = View.GONE
                binding.contenedorPadre2!!.visibility = View.GONE
            }
            else
            {
                showDetailAbuelo = true
                binding.contenedorPadre!!.visibility = View.VISIBLE
                binding.contenedorPadre2!!.visibility = View.VISIBLE
            }


        }





        binding.contenedorPadre!!.setOnClickListener {

            if(showDetailPadre)
            {
                showDetailPadre = false
                binding.contenedorMadre!!.visibility = View.GONE
            }
            else
            {
                showDetailPadre = true
                binding.contenedorMadre!!.visibility = View.VISIBLE
            }


        }



        binding.contenedorHijo1!!.setOnClickListener {

            if(showDetailhijo)
            {
                showDetailhijo = false
                binding.contenedorNieto1!!.visibility = View.GONE
            }
            else
            {
                showDetailhijo = true
                binding.contenedorNieto1!!.visibility = View.VISIBLE
            }


        }



        binding.contenedorHijo2!!.setOnClickListener {

            if(showDetailhijo2)
            {
                showDetailhijo2 = false
                binding.contenedorNieto2!!.visibility = View.GONE
            }
            else
            {
                showDetailhijo2 = true
                binding.contenedorNieto2!!.visibility = View.VISIBLE
            }


        }







        binding.contenedorPadre2!!.setOnClickListener {

            if(showDetailPadre2)
            {
                showDetailPadre2 = false
                binding.contenedorMadre2!!.visibility = View.GONE
            }
            else
            {
                showDetailPadre2 = true
                binding.contenedorMadre2!!.visibility = View.VISIBLE
            }


        }



        binding.contenedorHijo12!!.setOnClickListener {

            if(showDetailhijo222)
            {
                showDetailhijo222 = false
                binding.contenedorNieto12!!.visibility = View.GONE
            }
            else
            {
                showDetailhijo222 = true
                binding.contenedorNieto12!!.visibility = View.VISIBLE
            }


        }



        binding.contenedorHijo22!!.setOnClickListener {

            if(showDetailhijo2222)
            {
                showDetailhijo2222 = false
                binding.contenedorNieto22!!.visibility = View.GONE
            }
            else
            {
                showDetailhijo2222 = true
                binding.contenedorNieto22!!.visibility = View.VISIBLE
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