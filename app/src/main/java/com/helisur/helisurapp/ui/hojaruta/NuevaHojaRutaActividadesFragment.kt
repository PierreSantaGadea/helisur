package com.helisur.helisurapp.ui.hojaruta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.helisur.helisurapp.R
import com.helisur.helisurapp.databinding.FragmentActividadesHojaRutaBinding
import com.helisur.helisurapp.domain.model.FormatoRegistro
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.mantenimiento.formatos.ListaFormatosDiscrepanciasAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NuevaHojaRutaActividadesFragment : Fragment() {

    var className = "NuevaHojaRutaActividadesFragment"
    private lateinit var binding: FragmentActividadesHojaRutaBinding
    var loading: TransparentProgressDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentActividadesHojaRutaBinding.inflate(inflater, container, false)
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
        binding.tvAtras.setOnClickListener {

            TabsHojaRuta.viewPager.setCurrentItem(0)
        }
    }



    fun setRecyclerViewHojasRutaActividades(lista: ArrayList<FormatoRegistro>) {
        val recyclerview = binding.rvActividades
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ListaActividadesHojasRutaAdapter(lista,requireContext())
        recyclerview.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                recyclerview.context,
                R.drawable.divider
            )!!
        )

        recyclerview.addItemDecoration(dividerItemDecoration)

        adapter.onItemClick = { formatoRegistro ->

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