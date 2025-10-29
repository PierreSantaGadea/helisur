package com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas


import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast

import androidx.fragment.app.Fragment
import com.helisur.helisurapp.R
import com.helisur.helisurapp.databinding.InicialFragmentMantenimientoBinding
import com.helisur.helisurapp.databinding.P14Binding
import com.helisur.helisurapp.databinding.P15Binding
import com.helisur.helisurapp.databinding.P16Binding
import com.helisur.helisurapp.databinding.P17Binding
import com.helisur.helisurapp.databinding.P19Binding
import com.helisur.helisurapp.databinding.P20Binding
import com.helisur.helisurapp.databinding.P21Binding
import com.helisur.helisurapp.databinding.PruebaBinding
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.SpinenrItemSelectUserSmall
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.ui.Inspecion100horasTabs
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.ui.SpinenrItemSelectUser

import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class Pagina21  : Fragment() {

    var className = "Pagina14"
    private var _binding: P21Binding? = null
    private val binding get() = _binding!!
    var campoBloqueado = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = P21Binding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        clickListener()
        return root
    }

    fun initUI() {

        binding.editTexto.setOnLongClickListener {

            if(campoBloqueado)
            {
                campoBloqueado = false
                binding.editTexto.setBackgroundResource(R.drawable.bg_cell_row)
                binding.editTexto.isEnabled = true
                true
            }
            else
            {
                campoBloqueado = true
                binding.editTexto.setBackgroundResource(R.drawable.linea_diagonal)
                binding.editTexto.isEnabled = false
                true
            }


        }


        binding.chvbxx.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.editTexto.isEnabled = false
                binding.editTexto.setText("")
                binding.conteeeee.setBackgroundResource(R.drawable.linea_diagonal)
                // Acción cuando el checkbox es marcado
               // Toast.makeText(this, "El checkbox ha sido chequeado", Toast.LENGTH_SHORT).show()
            } else {
                binding.editTexto.isEnabled = true
                binding.conteeeee.setBackgroundResource(R.drawable.bg_cell_row)
                // Acción cuando el checkbox es desmarcado
              //  Toast.makeText(this, "El checkbox ha sido deschequeado", Toast.LENGTH_SHORT).show()
            }
        }



    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            Inspecion100horasTabs.tvPaginaFormato.text = "Página PM-21"
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