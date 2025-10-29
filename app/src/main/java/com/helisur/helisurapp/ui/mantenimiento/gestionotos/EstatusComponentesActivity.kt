package com.helisur.helisurapp.ui.mantenimiento.gestionotos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.helisur.helisurapp.R
import com.helisur.helisurapp.databinding.ActivityOrdenEstatusComponentesBinding
import com.helisur.helisurapp.databinding.ActivityOrdenMovimientosInsumosBinding

import com.helisur.helisurapp.databinding.ActivityPrevueloBinding
import com.helisur.helisurapp.domain.util.BaseActivity
import com.helisur.helisurapp.ui.mantenimiento.gestionotos.EstatusInsumosActivity.MovimientosInsumosAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EstatusComponentesActivity  : BaseActivity() {

    private lateinit var binding: ActivityOrdenEstatusComponentesBinding

    private lateinit var adaptador: EstatusComponentesAdapter
    private var componenteSeleccionado: EstatusComponentes? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdenEstatusComponentesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
        setupRecyclerView()
        setupSwipeRefresh()
        fetchComponentes()
    }

    private fun setupListeners() {
        binding.llBack.setOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        adaptador = EstatusComponentesAdapter(
            emptyList(),
            onLongClick = {},
            onClick = { componente -> componenteSeleccionado = componente },
            this
        )
        binding.rvEstatusInsumos.layoutManager = LinearLayoutManager(this)
        binding.rvEstatusInsumos.adapter = adaptador
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchComponentes()
        }
    }

    private fun fetchComponentes() {
        val movimientos = listOf(
            MovimientosComponentes("INSTALADO", "Almacén 1", "Almacén 2", "10/07/2024", "SAP-00001"),
            MovimientosComponentes("DESPACHO", "Almacén 3", "Almacén 4", "11/07/2024", "SAP-00002"),
        )

        val componentes = List(5) {
            EstatusComponentes(
                "0001234", "ART-456", "SN12345", "Nomenclatura de Prueba",
                "PART-PARENT", "SN-PARENT", "INSTALADO", "5", "4", "3", "1", movimientos
            )
        }

        adaptador.actualizarDatos(componentes)
        binding.swipeRefreshLayout.isRefreshing = false
    }

    data class EstatusComponentes(
        val codigoOTOS: String,
        val codigoArticulo: String,
        val numeroSerie: String,
        val nomenclatura: String,
        val nroPartePadre: String,
        val nroSeriePadre: String,
        val accion: String,
        val solicitado: String,
        val despachado: String,
        val instalado: String,
        val devuelto: String,
        val movimientos: List<MovimientosComponentes>
    )

    data class MovimientosComponentes(
        val accion: String,
        val almacenOrigen: String,
        val almacenDestino: String,
        val fechaMovimiento: String,
        val nroDocSAP: String
    )

    class EstatusComponentesAdapter(
        private var componentes: List<EstatusComponentes>,
        private val onLongClick: (EstatusComponentes) -> Unit,
        private val onClick: (EstatusComponentes) -> Unit,
        private val context: Context
    ) : RecyclerView.Adapter<EstatusComponentesAdapter.ComponentesViewHolder>() {

        private lateinit var adaptadorMovimientos: MovimientosComponentesAdapter

        class ComponentesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvNomenclatura: TextView = view.findViewById(R.id.tvNomenclatura)
            val tvCodigoOTOS: TextView = view.findViewById(R.id.tvCodigoOTOS)
            val tvCodigoArticulo: TextView = view.findViewById(R.id.tvCodigoArticulo)
            val tvNumeroSerie: TextView = view.findViewById(R.id.tvNroSerie)
            val tvNroPartePadre: TextView = view.findViewById(R.id.tvNroPartePadre)
            val tvNroSeriePadre: TextView = view.findViewById(R.id.tvNroSeriePadre)
            val tvAccion: TextView = view.findViewById(R.id.tvaccion)
            val tvSolicitado: TextView = view.findViewById(R.id.tvSolicitados)
            val tvDespachado: TextView = view.findViewById(R.id.tvDespachado)
            val tvInstalado: TextView = view.findViewById(R.id.tvInstalado)
            val tvDevuelto: TextView = view.findViewById(R.id.tvDevuelto)
            val llMovimientos: LinearLayout = view.findViewById(R.id.llMovimientos)
            val rvMovimientosComponentes: RecyclerView = view.findViewById(R.id.rvMovimientosComponentes)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentesViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_estatus_componentes, parent, false)
            return ComponentesViewHolder(view)
        }

        override fun onBindViewHolder(holder: ComponentesViewHolder, position: Int) {
            val componente = componentes[position]
            holder.tvNomenclatura.text = componente.nomenclatura
            holder.tvCodigoOTOS.text = componente.codigoOTOS
            holder.tvCodigoArticulo.text = componente.codigoArticulo
            holder.tvNumeroSerie.text = componente.numeroSerie
            holder.tvNroPartePadre.text = componente.nroPartePadre
            holder.tvNroSeriePadre.text = componente.nroSeriePadre
            holder.tvAccion.text = componente.accion
            holder.tvSolicitado.text = componente.solicitado
            holder.tvDespachado.text = componente.despachado
            holder.tvInstalado.text = componente.instalado
            holder.tvDevuelto.text = componente.devuelto

          //  setupRecyclerViewMovimientos(holder.rvMovimientosComponentes)

         /*   val adaptadorMovimientos = MovimientosComponentesAdapter(componente.movimientos)
            holder.rvMovimientosComponentes.layoutManager = LinearLayoutManager(context)
            holder.rvMovimientosComponentes.adapter = adaptadorMovimientos
            val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            holder.rvMovimientosComponentes.addItemDecoration(divider)


            holder.llMovimientos.setOnClickListener {
                holder.rvMovimientosComponentes.visibility =
                    if (holder.rvMovimientosComponentes.isVisible) View.GONE else View.VISIBLE
            }
*/
            holder.itemView.setOnClickListener { onClick(componente) }
            holder.itemView.setOnLongClickListener {
                onLongClick(componente)
                true
            }
        }


        fun setupRecyclerViewMovimientos(lista: RecyclerView) {
            adaptadorMovimientos = MovimientosComponentesAdapter(
                emptyList()
            )

            lista.layoutManager = LinearLayoutManager(context)
            lista.adapter = adaptadorMovimientos

            // Agregar línea divisoria con margen
            val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(context, R.drawable.divider)?.let {
                divider.setDrawable(it)
                lista.addItemDecoration(divider)
            }
        }


        override fun getItemCount() = componentes.size

        fun actualizarDatos(nuevos: List<EstatusComponentes>) {
            componentes = nuevos
            notifyDataSetChanged()
        }
    }

    class MovimientosComponentesAdapter(
        private var movimientos: List<MovimientosComponentes>
    ) : RecyclerView.Adapter<MovimientosComponentesAdapter.MovimientoViewHolder>() {

        class MovimientoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvAlmacenOrigen: TextView = view.findViewById(R.id.tvAlmacenOrigenCompo)
            val tvAlmacenDestino: TextView = view.findViewById(R.id.tvAlmacenDestinoCompo)
            val tvAccionMovimiento: TextView = view.findViewById(R.id.tvaccionmovcomponetes)
            val tvFechaMovimiento: TextView = view.findViewById(R.id.tvFechaMovimientoComponente)
            val tvNroDocSAP: TextView = view.findViewById(R.id.tvNroDodSapmovComp)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovimientoViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movimientos_componentes, parent, false)
            return MovimientoViewHolder(view)
        }

        override fun onBindViewHolder(holder: MovimientoViewHolder, position: Int) {
            val movimiento = movimientos[position]
            holder.tvAlmacenOrigen.text = movimiento.almacenOrigen
            holder.tvAlmacenDestino.text = movimiento.almacenDestino
            holder.tvAccionMovimiento.text = movimiento.accion
            holder.tvFechaMovimiento.text = movimiento.fechaMovimiento
            holder.tvNroDocSAP.text = movimiento.nroDocSAP
        }

        override fun getItemCount(): Int = movimientos.size
    }

}