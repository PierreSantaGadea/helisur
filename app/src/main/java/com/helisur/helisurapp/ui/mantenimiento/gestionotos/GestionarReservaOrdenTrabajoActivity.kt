package com.helisur.helisurapp.ui.mantenimiento.gestionotos

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
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
import com.helisur.helisurapp.databinding.ActivityOrdenGestionarReservaBinding

import com.helisur.helisurapp.domain.util.BaseActivity

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GestionarReservaOrdenTrabajoActivity  : BaseActivity() {

    private lateinit var binding: ActivityOrdenGestionarReservaBinding
    private lateinit var adaptador: ArticulosListosAdapter
    private var articuloGestionarReservaSeleccionada: ArticuloGestionarReserva? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdenGestionarReservaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickListener()

        setupRecyclerView()
        setupSwipeRefresh()
        fetchArticulosListos()
    }

    fun clickListener() {
        binding.llBack.setOnClickListener {
            finish()
        }

        binding.btnSolicitarIncrementoActividasd.setOnClickListener {
            showDialogSolicitarIncremento()
        }
    }

    private fun setupRecyclerView() {
        adaptador =
            ArticulosListosAdapter(
                emptyList(),
                onLongClick = { articulo ->
                },
                onClick = { articulo ->
                    articuloGestionarReservaSeleccionada = articulo
                },this)
        binding.rvGestionarReserva!!.layoutManager = LinearLayoutManager(this)
        binding.rvGestionarReserva!!.adapter = adaptador
    }



    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout!!.setOnRefreshListener {
            refreshData()
        }
    }

    private fun refreshData() {
        binding.swipeRefreshLayout!!.isRefreshing = true
        fetchArticulosListos()
    }

    private fun fetchArticulosListos() {
        val nuevosArticulos = listOf(
            ArticuloGestionarReserva("NAS00019984", "ACEITE MOBIL JET", "QT", "4.0",
                "4.0", "0.0"),
            ArticuloGestionarReserva("NAS00019984", "#6 WASHER", "QT", "4.0",
                "4.0", "0.0"),
            ArticuloGestionarReserva("NAS00019984", "ACEITE MOBIL JET", "QT", "4.0",
                "4.0", "0.0"),
            ArticuloGestionarReserva("NAS00019984", "#6 WASHER", "QT", "4.0",
                "4.0", "0.0")
            )
        adaptador.actualizarDatosArticulosListos(nuevosArticulos)
        binding.swipeRefreshLayout!!.isRefreshing = false
    }

    data class ArticuloGestionarReserva(
        var nroParte: String,
        var nomenclatura: String,
        var um: String,
        var solicitados: String,
        var reservados: String,
        var consumidos: String
    )


    class ArticulosListosAdapter(
        var articulosListos: List<ArticuloGestionarReserva>,
        private val onLongClick: (ArticuloGestionarReserva) -> Unit,
        private val onClick: (ArticuloGestionarReserva) -> Unit,
        private val ctx:Context
    ) : RecyclerView.Adapter<ArticulosListosAdapter.OrdenViewHolder>() {

        class OrdenViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvNomenclatura: TextView = view.findViewById(R.id.tvNomenclaturaGestionarReserva)

            val tvSolicitados: TextView = view.findViewById(R.id.tvSolicitadosGestionarReserva)
            val tvReservados: TextView = view.findViewById(R.id.tvReservadosGestionarReserva)
            val tvConsumidos: TextView = view.findViewById(R.id.tvConsumidosGestionarReserva)
            val tvNroParteGestionarReserva: TextView = view.findViewById(R.id.tvNroParteGestionarReserva)
            val tvUMGestionarReserva: TextView = view.findViewById(R.id.tvUMGestionarReserva)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdenViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gestionar_reserva, parent, false)
            return OrdenViewHolder(view)
        }

        override fun onBindViewHolder(holder: OrdenViewHolder, position: Int) {
            val articuloListo = articulosListos[position]
            holder.tvNomenclatura.text = "${articuloListo.nomenclatura}"

            holder.tvNroParteGestionarReserva.text = "${articuloListo.nroParte}"
            holder.tvUMGestionarReserva.text = "${articuloListo.um}"

            holder.tvSolicitados.text = "${articuloListo.solicitados}"
            holder.tvReservados.text = "${articuloListo.reservados}"

            holder.tvConsumidos.text = "${articuloListo.consumidos}"

            holder.itemView.setOnLongClickListener {
                onLongClick(articuloListo)
                true
            }

            holder.itemView.setOnClickListener {
                onClick(articuloListo)
            }
        }

        override fun getItemCount() = articulosListos.size

        fun actualizarDatosArticulosListos(nuevos: List<ArticuloGestionarReserva>) {
            articulosListos = nuevos
            notifyDataSetChanged()

        }
    }


    class BuscarArticulosListosAdapter(
        var buscarArticulosList: List<ArticuloGestionarReserva>,
        private val onClick: (ArticuloGestionarReserva) -> Unit,
        private val ctx:Context
    ) : RecyclerView.Adapter<BuscarArticulosListosAdapter.OrdenViewHolder>() {

        class OrdenViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvNomenclaturaBuscarArticulosDialogSolicitarIncr: TextView = view.findViewById(R.id.tvNomenclaturaBuscarArticulosDialogSolicitarIncr)
            val tvNroParteBuscarArticulosDialogSolicitarIncr: TextView = view.findViewById(R.id.tvNroParteBuscarArticulosDialogSolicitarIncr)
            val tvUMBuscarArticulosDialogSolicitarIncr: TextView = view.findViewById(R.id.tvUMBuscarArticulosDialogSolicitarIncr)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdenViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_buscar_articulo, parent, false)
            return OrdenViewHolder(view)
        }

        override fun onBindViewHolder(holder: OrdenViewHolder, position: Int) {
            val articuloListo = buscarArticulosList[position]
            holder.tvNomenclaturaBuscarArticulosDialogSolicitarIncr.text = "${articuloListo.nomenclatura}"

            holder.tvNroParteBuscarArticulosDialogSolicitarIncr.text = "Nro Parte : ${articuloListo.nroParte}"
            holder.tvUMBuscarArticulosDialogSolicitarIncr.text = "UM : ${articuloListo.um}"

            holder.itemView.setOnClickListener {
                onClick(articuloListo)
            }
        }

        override fun getItemCount() = buscarArticulosList.size

        fun actualizarDatosBuscarArticulosListos(nuevosBuscar: List<ArticuloGestionarReserva>) {
            buscarArticulosList = nuevosBuscar
            notifyDataSetChanged()

        }
    }

    class ArticulosSeleccionadosListosAdapter(
        var articulosSeleccionadosList: List<ArticuloGestionarReserva>,
        private val onClick: (ArticuloGestionarReserva) -> Unit,
        private val ctx:Context
    ) : RecyclerView.Adapter<ArticulosSeleccionadosListosAdapter.OrdenViewHolder>() {

        class OrdenViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvNomenclaturaArticulosSeleccionadosDialogSolicitarIncr: TextView = view.findViewById(R.id.tvNomenclaturaArticulosSeleccionadosDialogSolicitarIncr)
            val tvNroParteArticulosSeleccionadosDialogSolicitarIncr: TextView = view.findViewById(R.id.tvNroParteArticulosSeleccionadosDialogSolicitarIncr)
            val tvUMArticulosSeleccionadosDialogSolicitarIncr: TextView = view.findViewById(R.id.tvUMArticulosSeleccionadosDialogSolicitarIncr)
            val tvSolicitadosArticulosSeleccionadosDialogSolicitarIncr: TextView = view.findViewById(R.id.tvSolicitadosArticulosSeleccionadosDialogSolicitarIncr)
            val etCantASolicitarArticulosSeleccionadosDialogSolicitarIncr: EditText = view.findViewById(R.id.etCantASolicitarArticulosSeleccionadosDialogSolicitarIncr)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdenViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_solicitar_incremento_arcitulo, parent, false)
            return OrdenViewHolder(view)
        }

        override fun onBindViewHolder(holder: OrdenViewHolder, position: Int) {
            val articuloListo = articulosSeleccionadosList[position]
            holder.tvNomenclaturaArticulosSeleccionadosDialogSolicitarIncr.text = "${articuloListo.nomenclatura}"

            holder.tvNroParteArticulosSeleccionadosDialogSolicitarIncr.text = "${articuloListo.nroParte}"
            holder.tvUMArticulosSeleccionadosDialogSolicitarIncr.text = "${articuloListo.um}"
            holder.tvSolicitadosArticulosSeleccionadosDialogSolicitarIncr.text = "${articuloListo.solicitados}"

            holder.etCantASolicitarArticulosSeleccionadosDialogSolicitarIncr.addTextChangedListener {
                val texto = it.toString().trim()
                if (texto.isNotEmpty()) {
                    articuloListo.solicitados = texto
                } else {

                }
            }

            holder.itemView.setOnClickListener {
                onClick(articuloListo)
            }
        }

        override fun getItemCount() = articulosSeleccionadosList.size

        fun actualizarDatosArticulosSeleccionadosListos(nuevosBuscar: List<ArticuloGestionarReserva>) {
            articulosSeleccionadosList = nuevosBuscar
            notifyDataSetChanged()

        }
    }



    private fun showDialogSolicitarIncremento() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_solicitar_incremento_cantidad)
        dialog.window?.apply {
            attributes.windowAnimations = R.style.DialogAnimation
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            attributes.alpha = 1f
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val etBuscar = dialog.findViewById<EditText>(R.id.etBuscarPorDialogSolicitar)
        val etNroParte = dialog.findViewById<EditText>(R.id.etNroParteDialogSolicitar)
        val etNomenclatura = dialog.findViewById<EditText>(R.id.etNomenclaturaDialogSolicitar)
        val btnAgregar = dialog.findViewById<RelativeLayout>(R.id.btnAgregarDialogSolicitar)
        val btnRetirar = dialog.findViewById<RelativeLayout>(R.id.btnRetirarDialogSolicitar)
        val btnCerrar = dialog.findViewById<RelativeLayout>(R.id.btnCerrarDialogSolicitar)
        val btnSolicitarIncremento = dialog.findViewById<RelativeLayout>(R.id.btnSolicitarIncrementoDialogSolicitar)

        val rvBusqueda = dialog.findViewById<RecyclerView>(R.id.rvArticulosPorBuscarDialogSolicitar)
        val rvSeleccionados = dialog.findViewById<RecyclerView>(R.id.rvArticulosDialogSolicitar)

        // Limitar la altura máxima a 250dp en píxeles
        val maxHeightInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            250f,
            resources.displayMetrics
        ).toInt()

        val maxHeightInPxSeleccionados = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            350f,
            resources.displayMetrics
        ).toInt()

        rvBusqueda.viewTreeObserver.addOnGlobalLayoutListener {
            if (rvBusqueda.height > maxHeightInPx) {
                rvBusqueda.layoutParams.height = maxHeightInPx
                rvBusqueda.requestLayout()
            }
        }

        rvSeleccionados.viewTreeObserver.addOnGlobalLayoutListener {
            if (rvSeleccionados.height > maxHeightInPxSeleccionados) {
                rvSeleccionados.layoutParams.height = maxHeightInPxSeleccionados
                rvSeleccionados.requestLayout()
            }
        }

        // Lista base de 10 artículos
        val listaBase = List(10) {
            ArticuloGestionarReserva(
                nroParte = "PART-${1000 + it}",
                nomenclatura = "Articulo $it",
                um = "UM",
                solicitados = "3",
                reservados = "5",
                consumidos = "0"
            )
        }

        // Variables auxiliares
        val seleccionados = mutableListOf<ArticuloGestionarReserva>()
        var articuloSeleccionado: ArticuloGestionarReserva? = null

        // Adaptadores
        val adaptadorBuscar = BuscarArticulosListosAdapter(listaBase, { articulo ->
            articuloSeleccionado = articulo
            rvBusqueda.visibility = View.GONE
            etBuscar.setText("")
            etNroParte.setText(articulo.nroParte)
            etNomenclatura.setText(articulo.nomenclatura)


            val inputMethodManager = dialog.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(etBuscar.windowToken, 0)


        }, this)

        val adaptadorSeleccionados = ArticulosSeleccionadosListosAdapter(seleccionados, {}, this)

        rvBusqueda.layoutManager = LinearLayoutManager(this)
        rvBusqueda.adapter = adaptadorBuscar
        rvBusqueda.visibility = View.GONE



        rvSeleccionados.layoutManager = LinearLayoutManager(this)
        rvSeleccionados.adapter = adaptadorSeleccionados

        // Agregar línea divisoria con margen
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(this, R.drawable.divider)?.let {
            divider.setDrawable(it)
            rvSeleccionados.addItemDecoration(divider)
        }

        // Filtrado en tiempo real
        etBuscar.addTextChangedListener {
            val texto = it.toString().trim()
            if (texto.isNotEmpty()) {
                rvBusqueda.visibility = View.VISIBLE
                val filtrado = listaBase.filter { articulo ->
                    articulo.nroParte.contains(texto, ignoreCase = true) ||
                            articulo.nomenclatura.contains(texto, ignoreCase = true) ||
                            articulo.um.contains(texto, ignoreCase = true)
                }
                adaptadorBuscar.actualizarDatosBuscarArticulosListos(filtrado)
            } else {
                rvBusqueda.visibility = View.GONE
            }
        }

        // Agregar artículo a seleccionados
        btnAgregar.setOnClickListener {
            articuloSeleccionado?.let { articulo ->
                if (!seleccionados.contains(articulo)) {
                    seleccionados.add(articulo)
                    adaptadorSeleccionados.actualizarDatosArticulosSeleccionadosListos(seleccionados)
                    etNroParte.setText("")
                    etNomenclatura.setText("")
                }
            }
        }

        // Retirar artículo de seleccionados
        btnRetirar.setOnClickListener {
            articuloSeleccionado?.let { articulo ->
                if (seleccionados.contains(articulo)) {
                    seleccionados.remove(articulo)
                    adaptadorSeleccionados.actualizarDatosArticulosSeleccionadosListos(seleccionados)
                    etNroParte.setText("")
                    etNomenclatura.setText("")
                }
            }
        }

        // Transferir seleccionados a lista principal
        btnSolicitarIncremento.setOnClickListener {
            val nuevos = adaptadorSeleccionados.articulosSeleccionadosList
            val actual = (binding.rvGestionarReserva.adapter as? ArticulosListosAdapter)?.let {
                it.articulosListos.toMutableList()
            } ?: mutableListOf()

            actual.addAll(nuevos)
            adaptador.actualizarDatosArticulosListos(actual)
            dialog.dismiss()
        }

        btnCerrar.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    fun Context.hideKeyboard(view: View?) {
        view?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }


}