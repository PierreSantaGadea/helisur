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
import com.helisur.helisurapp.databinding.P20Binding
import com.helisur.helisurapp.databinding.P21Binding
import com.helisur.helisurapp.databinding.P22Binding
import com.helisur.helisurapp.databinding.PruebaBinding
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.SpinenrItemSelectUserSmall
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.ui.Inspecion100horasTabs
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.ui.SpinenrItemSelectUser

import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class Pagina22  : Fragment() {

    var className = "Pagina14"
    private var _binding: P22Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = P22Binding.inflate(inflater, container, false)
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
            Inspecion100horasTabs.tvPaginaFormato.text = "Página PM-22"
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