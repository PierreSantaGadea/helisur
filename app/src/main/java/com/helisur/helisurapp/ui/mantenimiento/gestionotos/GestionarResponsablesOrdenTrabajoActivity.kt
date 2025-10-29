package com.helisur.helisurapp.ui.mantenimiento.gestionotos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.helisur.helisurapp.R
import com.helisur.helisurapp.databinding.ActivityOrdenGestionarResponsbalesBinding
import com.helisur.helisurapp.domain.util.BaseActivity
import com.helisur.helisurapp.ui.mantenimiento.gestionotos.GestionarReservaOrdenTrabajoActivity.ArticulosListosAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GestionarResponsablesOrdenTrabajoActivity  : BaseActivity() {

    private lateinit var binding: ActivityOrdenGestionarResponsbalesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdenGestionarResponsbalesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickListener()
        initAll()
    }

    fun clickListener() {
        binding.llBack.setOnClickListener {
            finish()
        }

        binding.btnGrabarGestionarResponsabl.setOnClickListener {

            finish()
        }
    }

    fun initAll()
    {

   /*     val etBuscar = binding.etBuscarPorGestionarResponsable //dialog.findViewById<EditText>(R.id.etBuscarPorDialogSolicitar)
        val etNroParte = dialog.findViewById<EditText>(R.id.etNroParteDialogSolicitar)
        val etNomenclatura = dialog.findViewById<EditText>(R.id.etNomenclaturaDialogSolicitar)
        val btnAgregar = dialog.findViewById<RelativeLayout>(R.id.btnAgregarDialogSolicitar)
        val btnRetirar = dialog.findViewById<RelativeLayout>(R.id.btnRetirarDialogSolicitar)
        val btnCerrar = dialog.findViewById<RelativeLayout>(R.id.btnCerrarDialogSolicitar)
        val btnSolicitarIncremento = dialog.findViewById<RelativeLayout>(R.id.btnSolicitarIncrementoDialogSolicitar)

        val rvBusqueda = dialog.findViewById<RecyclerView>(R.id.rvArticulosPorBuscarDialogSolicitar)
        val rvSeleccionados = dialog.findViewById<RecyclerView>(R.id.rvArticulosDialogSolicitar)
*/
        val listaBase = List(10) {
            PersonalResponsable(
                dni = "4575824-${1000 + it}",
                nombre = "Juan Perez $it",
                cargo = "Externo",
                esResponsable = "Si"
            )
        }


        // Variables auxiliares
        val seleccionados = mutableListOf<PersonalResponsable>()
        var articuloSeleccionado: PersonalResponsable? = null

        // Adaptadores
        val adaptadorBuscar = BuscarPersonalResponsableListosAdapter(listaBase, { articulo ->
            articuloSeleccionado = articulo
            binding.rvResponsableABuscarGestionarResponsabl.visibility = View.GONE
            binding.etBuscarPorGestionarResponsable.setText("")
            binding.etPersonalGestionarResponsable.setText(articulo.nombre)

            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.etBuscarPorGestionarResponsable.windowToken, 0)


        }, this)

        val adaptadorSeleccionados = PersonalResponsableSeleccionadosListosAdapter(seleccionados, {}, this)

        binding.rvResponsableABuscarGestionarResponsabl.layoutManager = LinearLayoutManager(this)
        binding.rvResponsableABuscarGestionarResponsabl.adapter = adaptadorBuscar
        binding.rvResponsableABuscarGestionarResponsabl.visibility = View.GONE

        binding.rvResponsablesSeleccionadosGestionarResponsabl.layoutManager = LinearLayoutManager(this)
        binding.rvResponsablesSeleccionadosGestionarResponsabl.adapter = adaptadorSeleccionados

        // Agregar línea divisoria con margen
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(this, R.drawable.divider)?.let {
            divider.setDrawable(it)
            binding.rvResponsablesSeleccionadosGestionarResponsabl.addItemDecoration(divider)
        }



        // Filtrado en tiempo real
        binding.etBuscarPorGestionarResponsable.addTextChangedListener {
            val texto = it.toString().trim()
            if (texto.isNotEmpty()) {
                binding.rvResponsableABuscarGestionarResponsabl.visibility = View.VISIBLE
                val filtrado = listaBase.filter { articulo ->
                    articulo.nombre.contains(texto, ignoreCase = true) ||
                            articulo.cargo.contains(texto, ignoreCase = true) ||
                            articulo.dni.contains(texto, ignoreCase = true)
                }
                adaptadorBuscar.actualizarDatosBuscarArticulosListos(filtrado)
            } else {
                binding.rvResponsableABuscarGestionarResponsabl.visibility = View.GONE
            }
        }

        // Agregar artículo a seleccionados
        binding.btnAgregarGestionarResponsable.setOnClickListener {
            articuloSeleccionado?.let { articulo ->
                if (!seleccionados.contains(articulo)) {
                    seleccionados.add(articulo)
                    adaptadorSeleccionados.actualizarDatosArticulosSeleccionadosListos(seleccionados)
                    binding.etPersonalGestionarResponsable.setText("")
                }
            }
        }


        // Retirar artículo de seleccionados
        binding.btnRetirarGestionarResponsable.setOnClickListener {
            articuloSeleccionado?.let { articulo ->
                if (seleccionados.contains(articulo)) {
                    seleccionados.remove(articulo)
                    adaptadorSeleccionados.actualizarDatosArticulosSeleccionadosListos(seleccionados)
                    binding.etPersonalGestionarResponsable.setText("")
                }
            }
        }





    }



    class BuscarPersonalResponsableListosAdapter(
        var buscarPersonalResponsableList: List<PersonalResponsable>,
        private val onClick: (PersonalResponsable) -> Unit,
        private val ctx:Context
    ) : RecyclerView.Adapter<BuscarPersonalResponsableListosAdapter.OrdenViewHolder>() {

        class OrdenViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvPersonalGestionarResponsableActivity: TextView = view.findViewById(R.id.tvPersonalBuscarGestionarResponsableActivity)
            val tvDniGestionarResponsableActivity: TextView = view.findViewById(R.id.tvDniBuscarGestionarResponsableActivity)
            val tvCargoGestionarResponsableActivity: TextView = view.findViewById(R.id.tvCargoBuscarGestionarResponsableActivity)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdenViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_buscar_personal, parent, false)
            return OrdenViewHolder(view)
        }

        override fun onBindViewHolder(holder: OrdenViewHolder, position: Int) {
            val articuloListo = buscarPersonalResponsableList[position]
            holder.tvPersonalGestionarResponsableActivity.text = "${articuloListo.nombre}"

            holder.tvDniGestionarResponsableActivity.text = "DNI : ${articuloListo.dni}"
            holder.tvCargoGestionarResponsableActivity.text = "Cargo : ${articuloListo.cargo}"

            holder.itemView.setOnClickListener {
                onClick(articuloListo)
            }
        }

        override fun getItemCount() = buscarPersonalResponsableList.size

        fun actualizarDatosBuscarArticulosListos(nuevosBuscar: List<PersonalResponsable>) {
            buscarPersonalResponsableList = nuevosBuscar
            notifyDataSetChanged()

        }
    }

    class PersonalResponsableSeleccionadosListosAdapter(
        var personalResponsableSeleccionadosList: List<PersonalResponsable>,
        private val onClick: (PersonalResponsable) -> Unit,
        private val ctx:Context
    ) : RecyclerView.Adapter<PersonalResponsableSeleccionadosListosAdapter.OrdenViewHolder>() {

        class OrdenViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvPersonalGestionarResponsableActivity: TextView = view.findViewById(R.id.tvPersonalGestionarResponsableActivity)
            val tvDniGestionarResponsableActivity: TextView = view.findViewById(R.id.tvDniGestionarResponsableActivity)
            val tvCargoGestionarResponsableActivity: TextView = view.findViewById(R.id.tvCargoGestionarResponsableActivity)
            val tvEsResponsableGestionarResponsableActivity: TextView = view.findViewById(R.id.tvEsResponsableGestionarResponsableActivity)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdenViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_personal_gestionar_responsable, parent, false)
            return OrdenViewHolder(view)
        }

        override fun onBindViewHolder(holder: OrdenViewHolder, position: Int) {
            val articuloListo = personalResponsableSeleccionadosList[position]
            holder.tvPersonalGestionarResponsableActivity.text = "${articuloListo.nombre}"

            holder.tvDniGestionarResponsableActivity.text = "${articuloListo.dni}"
            holder.tvCargoGestionarResponsableActivity.text = "${articuloListo.cargo}"
            holder.tvEsResponsableGestionarResponsableActivity.text = "${articuloListo.esResponsable}"

            holder.itemView.setOnClickListener {
                onClick(articuloListo)
            }
        }

        override fun getItemCount() = personalResponsableSeleccionadosList.size

        fun actualizarDatosArticulosSeleccionadosListos(nuevosBuscar: List<PersonalResponsable>) {
            personalResponsableSeleccionadosList = nuevosBuscar
            notifyDataSetChanged()

        }
    }


    data class PersonalResponsable(
        var dni: String,
        var nombre: String,
        var cargo: String,
        var esResponsable: String
    )


}