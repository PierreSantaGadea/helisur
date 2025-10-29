package com.helisur.helisurapp.ui.mantenimiento.gestionotos

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.helisur.helisurapp.R
import com.helisur.helisurapp.databinding.ActivityOrdenEstatusInsumosBinding

import com.helisur.helisurapp.domain.util.BaseActivity

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EstatusInsumosActivity  : BaseActivity() {

    private lateinit var binding: ActivityOrdenEstatusInsumosBinding

    private lateinit var adaptador: EstatusInsumosAdapter

    private var ordenSeleccionada: EstatusInsumos? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdenEstatusInsumosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickListener()
        setupRecyclerView()
        setupSwipeRefresh()
        fetchOrdenesTrabajo()
        initViews()
    }

    fun initViews()
    {
        var idOrden = intent.getStringExtra("idOrden")
        binding.tvOrdenId.setText("OT #"+idOrden)

    }

    fun clickListener() {
        binding.llBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        adaptador =
            EstatusInsumosAdapter(
                emptyList(),
                onLongClick = { orden ->
                    //ordenSeleccionada = orden
                    // binding.addNewPreVuelo!!.visibility = View.VISIBLE
                },
                onClick = { orden ->
                    ordenSeleccionada = orden
                    // showDialogDetalleOrden(orden)
                },this)
        binding.rvEstatusInsumos!!.layoutManager = LinearLayoutManager(this)
        binding.rvEstatusInsumos!!.adapter = adaptador
    }



    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout!!.setOnRefreshListener {
            refreshData()
        }
    }

    private fun refreshData() {
        binding.swipeRefreshLayout!!.isRefreshing = true
        fetchOrdenesTrabajo()
    }

    private fun fetchOrdenesTrabajo() {

        val nuevosMovimientos = listOf(
            MovimientoInsumos("EA", "Despacho", "2.0", "MALVINAS SERVICLAES CON CTO",
                "ALMACEN MGA CON COSTO", "12/05/2024 10:10:10", "51515"),
            MovimientoInsumos("EA", "Consumo", "2.0", "MALVINAS SERVICLAES CON CTO",
                "ALMACEN MGA CON COSTO", "12/05/2024 10:10:10", "51515"),
            MovimientoInsumos("EA", "Devuelto", "2.0", "MALVINAS SERVICLAES CON CTO",
                "ALMACEN MGA CON COSTO", "12/05/2024 10:10:10", "51515"),
            MovimientoInsumos("EA", "Despacho", "2.0", "MALVINAS SERVICLAES CON CTO",
                "ALMACEN MGA CON COSTO", "12/05/2024 10:10:10", "51515"),

        )


        val nuevasOrdenes = listOf(
            EstatusInsumos("00019984", "45154-4", "ARANDELA DE SEGURO ABRAZADERA", "4.0",
                "4.0", "0.0", "0.0", "0.0", "0.0",nuevosMovimientos),
            EstatusInsumos("00019984", "45154-4", "ARANDELA DE SEGURO ABRAZADERA", "4.0",
                "4.0", "0.0", "0.0", "0.0", "0.0",nuevosMovimientos),
            EstatusInsumos("00019984", "45154-4", "ARANDELA DE SEGURO ABRAZADERA", "4.0",
                "4.0", "0.0", "0.0", "0.0", "0.0",nuevosMovimientos),
            EstatusInsumos("00019984", "45154-4", "ARANDELA DE SEGURO ABRAZADERA", "4.0",
                "4.0", "0.0", "0.0", "0.0", "0.0",nuevosMovimientos),
            EstatusInsumos("00019984", "45154-4", "ARANDELA DE SEGURO ABRAZADERA", "4.0",
                "4.0", "0.0", "0.0", "0.0", "0.0",nuevosMovimientos),
            EstatusInsumos("00019984", "45154-4", "ARANDELA DE SEGURO ABRAZADERA", "4.0",
                "4.0", "0.0", "0.0", "0.0", "0.0",nuevosMovimientos),
            EstatusInsumos("00019984", "45154-4", "ARANDELA DE SEGURO ABRAZADERA", "4.0",
                "4.0", "0.0", "0.0", "0.0", "0.0",nuevosMovimientos),



            )
   //     adaptadorMovimientos.actualizarDatosMovimientosInsumos(nuevosMovimientos)
        adaptador.actualizarDatosStatusInsumos(nuevasOrdenes)
        binding.swipeRefreshLayout!!.isRefreshing = false
    }


    data class EstatusInsumos(
        val codigoOTOS: String,
        val codigoArticulo: String,
        val nomenclatura: String,
        val cantidadOriginal: String,
        val solicitados: String,
        val reservados: String,
        val despachados: String,
        val consumidos: String,
        val devueltos: String,
        val movimientos:List<MovimientoInsumos>?
    )

    data class MovimientoInsumos(
        val um: String,
        val tipoMovimiento: String,
        val cantidadMovimiento: String,
        val alamcenOrigen: String,
        val almacenDestino: String,
        val fechaMovimiento: String,
        val nroDocSAP: String
    )

    class EstatusInsumosAdapter(
        private var ordenes: List<EstatusInsumos>,
        private val onLongClick: (EstatusInsumos) -> Unit,
        private val onClick: (EstatusInsumos) -> Unit,
        private val ctx:Context
    ) : RecyclerView.Adapter<EstatusInsumosAdapter.OrdenViewHolder>() {

        private lateinit var adaptadorMovimientos: MovimientosInsumosAdapter
        private var movimientoSeleccionada: MovimientoInsumos? = null

        class OrdenViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvNomenclatura: TextView = view.findViewById(R.id.tvNomenclatura)
            val tvCodigoOTOS: TextView = view.findViewById(R.id.tvCodigoOTOS)
            val tvCodigoArticulo: TextView = view.findViewById(R.id.tvCodigoArticulo)
            val tvCantidadOriginal: TextView = view.findViewById(R.id.tvCantidadOriginal)
            val tvSolicitados: TextView = view.findViewById(R.id.tvSolicitados)
            val tvReservados: TextView = view.findViewById(R.id.tvReservados)
            val tvDespachados: TextView = view.findViewById(R.id.tvDespachados)
            val tvConsumidos: TextView = view.findViewById(R.id.tvConsumidos)
            val tvDevueltos: TextView = view.findViewById(R.id.tvDevueltos)

            val rvMovimientosInsumos: RecyclerView = view.findViewById(R.id.rvMovimientosInsumos)
            val llMovimientos: LinearLayout = view.findViewById(R.id.llMovimientos)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdenViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_estatus_insumos, parent, false)
            return OrdenViewHolder(view)
        }

        override fun onBindViewHolder(holder: OrdenViewHolder, position: Int) {
            val orden = ordenes[position]
            holder.tvNomenclatura.text = "${orden.nomenclatura}"
            holder.tvCodigoOTOS.text = "${orden.codigoOTOS}"
            holder.tvCodigoArticulo.text = "${orden.codigoArticulo}"
            holder.tvCantidadOriginal.text = "${orden.cantidadOriginal}"
            holder.tvSolicitados.text = "${orden.solicitados}"
            holder.tvReservados.text = "${orden.reservados}"
            holder.tvDespachados.text = "${orden.despachados}"
            holder.tvConsumidos.text = "${orden.consumidos}"
            holder.tvDevueltos.text = "${orden.devueltos}"


            setupRecyclerViewMovimientos(holder.rvMovimientosInsumos)

            adaptadorMovimientos.actualizarDatosMovimientosInsumos(orden.movimientos!!)

            holder.itemView.setOnLongClickListener {
                onLongClick(orden)
                true
            }

            holder.itemView.setOnClickListener {
                onClick(orden)
            }

            /*
            holder.llMovimientos.setOnClickListener {
                if (holder.rvMovimientosInsumos.isVisible) {
                    holder.rvMovimientosInsumos.visibility = View.GONE
                }
                else {
                    holder.rvMovimientosInsumos.visibility = View.VISIBLE
                }
            }

             */

        }

        fun setupRecyclerViewMovimientos(lista: RecyclerView) {
            adaptadorMovimientos = MovimientosInsumosAdapter(
                emptyList(),
                onLongClick = { orden ->
                    //ordenSeleccionada = orden
                    // binding.addNewPreVuelo!!.visibility = View.VISIBLE
                },
                onClick = { orden ->
                    movimientoSeleccionada = orden
                    // showDialogDetalleOrden(orden)
                }
            )

            lista.layoutManager = LinearLayoutManager(ctx)
            lista.adapter = adaptadorMovimientos

            // Agregar línea divisoria con margen
            val divider = DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(ctx, R.drawable.divider)?.let {
                divider.setDrawable(it)
                lista.addItemDecoration(divider)
            }
        }


        override fun getItemCount() = ordenes.size

        fun actualizarDatosStatusInsumos(nuevos: List<EstatusInsumos>) {
            ordenes = nuevos
            notifyDataSetChanged()

        }
    }



    class MovimientosInsumosAdapter(
        private var movimientos: List<MovimientoInsumos>,
        private val onLongClick: (MovimientoInsumos) -> Unit,
        private val onClick: (MovimientoInsumos) -> Unit
    ) : RecyclerView.Adapter<MovimientosInsumosAdapter.MovimientosOrdenViewHolder>() {

        class MovimientosOrdenViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvAlmacenOrigen: TextView = view.findViewById(R.id.tvAlmacenOrigenInsumos)
            val tvAlmacenDestino: TextView = view.findViewById(R.id.tvAlmacenDestinoInsumos)
            val tvUM: TextView = view.findViewById(R.id.tvUM)
            val tvTipoMovimientoInsumo: TextView = view.findViewById(R.id.tvTipoMovimientoInsumo)
            val tvCantidadMovimientoInsumo: TextView = view.findViewById(R.id.tvCantidadMovimientoInsumo)
            val tvFechaMovimientoInsumo: TextView = view.findViewById(R.id.tvFechaMovimientoInsumo)
            val tvNroDodSap: TextView = view.findViewById(R.id.tvNroDodSap)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovimientosOrdenViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movimientos_insumos_vdos, parent, false)
            return MovimientosOrdenViewHolder(view)
        }

        override fun onBindViewHolder(holder: MovimientosOrdenViewHolder, position: Int) {
            val orden = movimientos[position]
            holder.tvAlmacenOrigen.text = "${orden.alamcenOrigen}"
            holder.tvAlmacenDestino.text = "${orden.almacenDestino}"
            holder.tvUM.text = "${orden.um}"
            holder.tvTipoMovimientoInsumo.text = "${orden.tipoMovimiento}"
            holder.tvCantidadMovimientoInsumo.text = "${orden.cantidadMovimiento}"
            holder.tvFechaMovimientoInsumo.text = "${orden.fechaMovimiento}"
            holder.tvNroDodSap.text = "${orden.nroDocSAP}"


            holder.itemView.setOnLongClickListener {
                onLongClick(orden)
                true
            }

            holder.itemView.setOnClickListener {
                onClick(orden)
            }



        }

        override fun getItemCount() = movimientos.size

        fun actualizarDatosMovimientosInsumos(nuevos: List<MovimientoInsumos>) {
            movimientos = nuevos
            notifyDataSetChanged()

        }
    }


    private fun showDialogMovimientosInsumos(movimientoInsumos: MovimientoInsumos) {
        val dialog = Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.dialog_movimientos_insumos)
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogAnimation

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.attributes.alpha = 1f
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }


        val tvEstado = dialog!!.findViewById<TextView>(R.id.tvEstadoDetalleOrden)


     //   tvEstado.setText(orden.estado)


        dialog!!.show()
    }




}