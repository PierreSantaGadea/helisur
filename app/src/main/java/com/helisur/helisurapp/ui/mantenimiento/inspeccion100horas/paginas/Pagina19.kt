package com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas

import android.R
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView

import androidx.fragment.app.Fragment
import com.helisur.helisurapp.databinding.InicialFragmentMantenimientoBinding
import com.helisur.helisurapp.databinding.P14Binding
import com.helisur.helisurapp.databinding.P15Binding
import com.helisur.helisurapp.databinding.P16Binding
import com.helisur.helisurapp.databinding.P17Binding
import com.helisur.helisurapp.databinding.P19Binding
import com.helisur.helisurapp.databinding.PruebaBinding
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.SpinenrItemSelectUserSmall
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.ui.Inspecion100horasTabs
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.ui.SpinenrItemSelectUser

import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class Pagina19  : Fragment() {

    var className = "Pagina14"
    private var _binding: P19Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = P19Binding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        clickListener()
        return root
    }

    fun initUI() {


        setSpinnerr()



    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            Inspecion100horasTabs.tvPaginaFormato.text = "Página PM-19"
        } else {
        }
    }



    fun clickListener()
    {

    }


    fun setSpinnerr(
    ) {
        var spinnerTipo = binding!!.spiEjemplo
        val spinnerArray: MutableList<String> = ArrayList()
        val spinnerArrayImages: MutableList<Int> = ArrayList()
        spinnerArray.add("Seleccione")
        spinnerArrayImages.add(com.helisur.helisurapp.R.drawable.empty)


        spinnerArray.add("Marco De la Vega")
        spinnerArrayImages.add(com.helisur.helisurapp.R.drawable.ic_user)

        spinnerArray.add("Leonel Messi")
        spinnerArrayImages.add(com.helisur.helisurapp.R.drawable.ic_user)

        spinnerArray.add("Teofilo Cubillas")
        spinnerArrayImages.add(com.helisur.helisurapp.R.drawable.ic_user)

        spinnerArray.add("Christian Roman")
        spinnerArrayImages.add(com.helisur.helisurapp.R.drawable.ic_user)

        spinnerArray.add("Luis Suarez")
        spinnerArrayImages.add(com.helisur.helisurapp.R.drawable.ic_user)

        spinnerArray.add("Diego Maradona")
        spinnerArrayImages.add(com.helisur.helisurapp.R.drawable.ic_user)

        spinnerArray.add("Ronaldinho")
        spinnerArrayImages.add(com.helisur.helisurapp.R.drawable.ic_user)

        spinnerArray.add("Kukin Flores")
        spinnerArrayImages.add(com.helisur.helisurapp.R.drawable.ic_user)




        val adapter =
            SpinenrItemSelectUser(
                requireContext(), 0,
                spinnerArray.toTypedArray(),  spinnerArrayImages.toTypedArray()
            )
        //   adapter.setDropDownViewResource(R.layout.spinner_item_aeronave)

        spinnerTipo.adapter = adapter

        spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {

                if(position>0)
                {
                    binding.tvlicenciaEjemplo.text = "51515151"
                    binding.tvlicenciaEjemplo.visibility = View.VISIBLE
                    binding.ivFirmaEjemplo.visibility = View.VISIBLE
                }
                else{
                    binding.tvlicenciaEjemplo.visibility = View.GONE
                    binding.ivFirmaEjemplo.visibility = View.GONE
                }



                /**    periodoSelected = periodosList[position].id
                nombrePeriodoSelected = periodosList[position].detalle
                periodoPositionSelected = position
                val sessionManager = SessionUserManager(requireContext())
                concursosList = ArrayList()
                cocursosViewModel.listaConcursos(sessionManager.getToken()!!, periodoSelected)*/
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
        _binding = null
    }


    override fun onResume() {
        super.onResume()
        var prueba = ""
    }


    fun showDatePickerDialog(textView: TextView, context: Context) {
        // Obtener la fecha actual como valor inicial
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Crear el DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Formatear fecha seleccionada
                val fecha = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                // Mostrar en el TextView
                textView.text = fecha
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }



}