package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.helisur.helisurapp.databinding.FragmentDatosAeronaveBinding
import com.helisur.helisurapp.databinding.FragmentResponsableBinding
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreVueloResponsableFragment : Fragment() {


    private lateinit var binding: FragmentResponsableBinding
    var loading: TransparentProgressDialog? = null

    var showDetail1 = false
    var showDetail2 = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentResponsableBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        clicListener()
        //   observers()
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




    fun clicListener()
    {
        binding.chxNo.setOnClickListener {
            showDialog()
        }


        binding.chxSi.setOnClickListener {

            binding.tvSiguiente.visibility = View.VISIBLE
        }




        binding.llitem1!!.setOnClickListener {

            if(showDetail1)
            {
                showDetail1 = false
                binding.llDetalle1!!.visibility = View.GONE
                binding.llDetalle1!!.visibility = View.GONE
            }
            else
            {
                showDetail1 = true
                binding.llDetalle1!!.visibility = View.VISIBLE
                binding.llDetalle1!!.visibility = View.VISIBLE
            }


        }

        binding.llitem2!!.setOnClickListener {

            if(showDetail2)
            {
                showDetail2 = false
                binding.llDetalle2!!.visibility = View.GONE
                binding.llDetalle2!!.visibility = View.GONE
            }
            else
            {
                showDetail2 = true
                binding.llDetalle2!!.visibility = View.VISIBLE
                binding.llDetalle2!!.visibility = View.VISIBLE
            }

        }

    }

    fun showDialog()
    {

        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {}
                    DialogInterface.BUTTON_NEGATIVE -> {}
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage("Este prevuelo se cerrara sin entrega a operaciones").setPositiveButton("Ok", dialogClickListener)
            .setNegativeButton("Cancelar", dialogClickListener).show()

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
      //  _binding = null
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