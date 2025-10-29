package com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas


import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import com.helisur.helisurapp.R
import androidx.fragment.app.Fragment
import com.helisur.helisurapp.databinding.P10Binding
import com.helisur.helisurapp.databinding.P9Binding
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.SpinenrItemSelectUserSmall
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.ui.Inspecion100horasTabs
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.ui.SpinenrItemEspecialidadSelected
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.ui.SpinenrItemSelectUser

import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class Pagina10  : Fragment() {

    var className = "Pagina14"
    private var _binding: P10Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = P10Binding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        clickListener()
        return root
    }

    fun initUI() {


        setSpinnerr()
        setSpinnerrPie()
        setSpinnerrEspecialidad()

        binding.tvFechaEjemplo.setOnClickListener {
            showDatePickerDialog(binding.tvFechaEjemplo, requireContext())
        }

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            Inspecion100horasTabs.tvPaginaFormato.text = "Página PM-10"
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
        spinnerArrayImages.add(R.drawable.empty)


        spinnerArray.add("Marco De la Vega")
        spinnerArrayImages.add(R.drawable.ic_user)

        spinnerArray.add("Leonel Messi")
        spinnerArrayImages.add(R.drawable.ic_user)

        spinnerArray.add("Teofilo Cubillas")
        spinnerArrayImages.add(R.drawable.ic_user)

        spinnerArray.add("Christian Roman")
        spinnerArrayImages.add(R.drawable.ic_user)

        spinnerArray.add("Luis Suarez")
        spinnerArrayImages.add(R.drawable.ic_user)

        spinnerArray.add("Diego Maradona")
        spinnerArrayImages.add(R.drawable.ic_user)

        spinnerArray.add("Ronaldinho")
        spinnerArrayImages.add(R.drawable.ic_user)

        spinnerArray.add("Kukin Flores")
        spinnerArrayImages.add(R.drawable.ic_user)




        val adapter =
            SpinenrItemSelectUserSmall(
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

    fun setSpinnerrPie(
    ) {
        var spinnerTipo = binding!!.spiEjemploPie
        val spinnerArray: MutableList<String> = ArrayList()
        val spinnerArrayImages: MutableList<Int> = ArrayList()
        spinnerArray.add("Seleccione")
        spinnerArrayImages.add(R.drawable.empty)


        spinnerArray.add("Marco De la Vega")
        spinnerArrayImages.add(R.drawable.ic_user)

        spinnerArray.add("Leonel Messi")
        spinnerArrayImages.add(R.drawable.ic_user)

        spinnerArray.add("Teofilo Cubillas")
        spinnerArrayImages.add(R.drawable.ic_user)

        spinnerArray.add("Christian Roman")
        spinnerArrayImages.add(R.drawable.ic_user)

        spinnerArray.add("Luis Suarez")
        spinnerArrayImages.add(R.drawable.ic_user)

        spinnerArray.add("Diego Maradona")
        spinnerArrayImages.add(R.drawable.ic_user)

        spinnerArray.add("Ronaldinho")
        spinnerArrayImages.add(R.drawable.ic_user)

        spinnerArray.add("Kukin Flores")
        spinnerArrayImages.add(R.drawable.ic_user)




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
                    binding.tvlicenciaEjemploPie.text = "51515151"
                    binding.tvlicenciaEjemploPie.visibility = View.VISIBLE
                    binding.ivFirmaEjemploPie.visibility = View.VISIBLE
                }
                else{
                    binding.tvlicenciaEjemploPie.visibility = View.GONE
                    binding.ivFirmaEjemploPie.visibility = View.GONE
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

    fun setSpinnerrEspecialidad(
    ) {
        var spinnerTipo = binding!!.spiEspecialidadEjemplo
        val spinnerArray: MutableList<String> = ArrayList()
        val spinnerArrayImages: MutableList<Int> = ArrayList()
        spinnerArray.add("Seleccione")
        spinnerArrayImages.add(R.drawable.empty)

        spinnerArray.add("Mecánico")
        spinnerArray.add("Aviónico")


        val adapter =
            SpinenrItemEspecialidadSelected(
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

                }
                else{

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