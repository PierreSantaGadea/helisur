package com.helisur.helisurapp.ui.mantenimiento.gestionotos

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.helisur.helisurapp.R
import com.helisur.helisurapp.data.cloud.gestionotos.model.response.ObtieneOTOSDataTableCloudResponse
import com.helisur.helisurapp.databinding.EjecucionFragmentBinding
import com.helisur.helisurapp.domain.model.ModeloAeronave
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.mantenimiento.AeronavesViewModel
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.ui.Inspeccion100HorasActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EjecucionFragment : Fragment() {

    var className = "EjecucionFragment"
    private val gestionOTOSViewModel: GestionOTOSViewModel by viewModels()
    private var otosList: ArrayList<ObtieneOTOSDataTableCloudResponse>? = null
    private var _binding: EjecucionFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adaptador: OrdenesAdapter
    private var ordenSeleccionada: ObtieneOTOSDataTableCloudResponse? = null
    var loading: TransparentProgressDialog? = null
    private var filtroNumeroOT: String? = null
    private var filtroCodigoOT: String? = null
    private var filtroEstacion: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EjecucionFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        setupRecyclerView()
        setupSwipeRefresh()
        setupFab()
        return root
    }

    fun initUI()
    {
        loading = TransparentProgressDialog(requireContext())

        gestionOTOSViewModel.getOTOSList()

        gestionOTOSViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            try {
                if (it) {
                    if (!loading!!.isShowing) {
                        loading!!.show()
                    }
                } else {
                    if (loading!!.isShowing) {
                        loading!!.dismiss()
                    }
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })

        gestionOTOSViewModel.gestionOTOSState.observe(viewLifecycleOwner, Observer {
            try {
                if (it.toString().contains(Constants.ERROR.SUCCESS)) {
                } else {
                    if (it.toString().contains(Constants.ERROR.FAILURE)) {
                        Log.e(className, Constants.ERROR.ERROR)
                    }
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })

        gestionOTOSViewModel.responseGetOTOSListCloud.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    otosList = ArrayList(it.data!!.table)
                    fetchOrdenesTrabajo()
                    /*
                    var modeloAeronaveEntityList: ArrayList<ModeloAeronave> = arrayListOf()
                    for(item in aeronavesList!!)
                    {
                        modeloAeronaveEntityList.add(item.toDomain())
                    }
                    aeronavesViewModel.insertModeloAeronaveListDB(modeloAeronaveEntityList)

                     */


               //     binding.rlAeronave!!.setBackgroundResource(R.drawable.shape_text_box)
                //    setSpinnerAeronave()
                } else {
                    Log.e(className, Constants.ERROR.ERROR)
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })

    }


    private fun setupRecyclerView() {
        adaptador = OrdenesAdapter(emptyList(),
            onLongClick = { orden ->

        }, onClick = { orden ->
            ordenSeleccionada = orden
            showDialogDetalleOrden(orden)
        })
        binding.rvOrdenesTrabajoAEjecutar!!.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrdenesTrabajoAEjecutar!!.adapter = adaptador
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout!!.setOnRefreshListener {
            refreshData()
        }
    }

    private fun refreshData() {
        binding.swipeRefreshLayout!!.isRefreshing = true
        gestionOTOSViewModel.getOTOSList()
    }

    private fun fetchOrdenesTrabajo() {
        adaptador.actualizarDatos(otosList!!)
        binding.swipeRefreshLayout!!.isRefreshing = false
    }

    private fun setupFab() {
     //   binding.addNewPreVuelo!!.visibility = View.GONE
        binding.btnFilter!!.setOnClickListener {
            mostrarPopupAcciones()
          /*  ordenSeleccionada?.let {
                mostrarPopupAcciones(it)
            }

           */
        }
    }

    private fun mostrarPopupAcciones() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_filtrar_ordenes)
        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.windowAnimations = R.style.DialogAnimation
            attributes.alpha = 1f
        }

        val etNumeroOT = dialog.findViewById<EditText>(R.id.etNumeroOtTOSDialogFiltrarOTOS)
        val etCodigoOT = dialog.findViewById<EditText>(R.id.etCodigoOTDialogFiltrarOTOS)
        val spiEstacion = dialog.findViewById<Spinner>(R.id.spiEstacionFiltroDialogFiltrarOTOS)
        val btnFiltrar = dialog.findViewById<RelativeLayout>(R.id.btnFiltrarDialogFiltrarOTOS)
        val btnCerrar = dialog.findViewById<RelativeLayout>(R.id.btnCerrarDialogFiltrarOTOS)

        // Establece los valores anteriores si existen
        etNumeroOT.setText(filtroNumeroOT)
        etCodigoOT.setText(filtroCodigoOT)

        // Establece el valor del spinner si ya estaba seleccionado
        filtroEstacion?.let { estacionSeleccionada ->
            val index = (0 until spiEstacion.count).firstOrNull {
                spiEstacion.getItemAtPosition(it).toString().equals(estacionSeleccionada, ignoreCase = true)
            }
            if (index != null) spiEstacion.setSelection(index)
        }

        btnFiltrar.setOnClickListener {
            filtroNumeroOT = etNumeroOT.text.toString().trim()
            filtroCodigoOT = etCodigoOT.text.toString().trim()
            filtroEstacion = spiEstacion.selectedItem?.toString()?.trim()

            val resultadosFiltrados = otosList!!.filter { item ->
                val coincideNumero = filtroNumeroOT.isNullOrEmpty() || item.numeroOrden?.contains(filtroNumeroOT!!, ignoreCase = true) == true
                val coincideCodigo = filtroCodigoOT.isNullOrEmpty() || item.codigoOrden?.contains(filtroCodigoOT!!, ignoreCase = true) == true
                val coincideEstacion = filtroEstacion.isNullOrEmpty() || item.nombreEstacion?.equals(filtroEstacion!!, ignoreCase = true) == true

                coincideNumero && coincideCodigo && coincideEstacion
            }

            actualizarLista(resultadosFiltrados)
            dialog.dismiss()
        }

        btnCerrar.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun actualizarLista(filtrados: List<ObtieneOTOSDataTableCloudResponse>) {
        adaptador.actualizarDatos(filtrados) // o cualquier métod para recargar tu RecyclerView
    }


    private fun showDialogDetalleOrden(orden: ObtieneOTOSDataTableCloudResponse) {
        val dialog = Dialog(requireContext())
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.dialog_detalle_orden)
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogAnimation

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.attributes.alpha = 1f
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val tvEstado = dialog!!.findViewById<TextView>(R.id.tvEstadoDetalleOrden)
        val tvRubro = dialog!!.findViewById<TextView>(R.id.tvRubroDetalleOrden)
        val tvCodigoOt = dialog!!.findViewById<TextView>(R.id.tvCodigoOtDetalleOrden)
        val tvNumeroOtos = dialog!!.findViewById<TextView>(R.id.tvNumeroOtosDetalleOrden)
        val tvCategoria = dialog!!.findViewById<TextView>(R.id.tvCategoriaDetalleOrden)
        val tvEstacion = dialog!!.findViewById<TextView>(R.id.tvEstacionDetalleOrden)
        val tvFechaInicio = dialog!!.findViewById<TextView>(R.id.tvFechaInicioDetalleOrden)
        val tvFechaTermino = dialog!!.findViewById<TextView>(R.id.tvFechaTerminoDetalleOrden)
        val tvLimiteTermino = dialog!!.findViewById<TextView>(R.id.tvLimiteTerminoDetalleOrden)
        val tvModeloNumeroParte = dialog!!.findViewById<TextView>(R.id.tvModeloNumeroParteDetalleOrden)
        val tvMatriculaNomenclatura = dialog!!.findViewById<TextView>(R.id.tvMatriculaNomenclaturaDetalleOrden)
        val tvTipoProgramacion = dialog!!.findViewById<TextView>(R.id.tvTipoProgramacionDetalleOrden)
        val tvTipoMantenimiento = dialog!!.findViewById<TextView>(R.id.tvTipoMantenimientoDetalleOrden)
        val tvAlcanceTrabajo = dialog!!.findViewById<TextView>(R.id.tvAlcanceTrabajoDetalleOrden)
        val tvDescripcion = dialog!!.findViewById<TextView>(R.id.tvDescripcionDetalleOrden)
        val tvTituloDetalleOrden = dialog!!.findViewById<TextView>(R.id.tvTituloDetalleOrden)
        val view_indicador = dialog!!.findViewById<View>(R.id.view_indicador)
        val btnDetalle = dialog!!.findViewById<RelativeLayout>(R.id.btnDetalle)
        val btnStatusInsumos = dialog!!.findViewById<RelativeLayout>(R.id.btnStatusInsumos)
        val btnEstatusComponentes = dialog!!.findViewById<RelativeLayout>(R.id.btnEstatusComponentes)
        val btnGestionarReserva = dialog!!.findViewById<RelativeLayout>(R.id.btnGestionarReserva)
        val btnGestionarResponsables = dialog!!.findViewById<RelativeLayout>(R.id.btnGestionarResponsables)

        val btnDocumentosFormatos = dialog!!.findViewById<RelativeLayout>(R.id.btnDocumentosFormatos)

        btnDetalle.setOnClickListener {
            var intent = Intent(requireContext(), DetalleOrdenActivity::class.java)
            startActivity(intent)
        }

        btnStatusInsumos.setOnClickListener {
            var intent = Intent(requireContext(), EstatusInsumosActivity::class.java)
            intent.putExtra("idOrden",orden.codigoOrden)
            intent.putExtra("numeroOrden",orden.numeroOrden)
            startActivity(intent)
        }

        btnEstatusComponentes.setOnClickListener {
            var intent = Intent(requireContext(), EstatusComponentesActivity::class.java)
            intent.putExtra("idOrden",orden.codigoOrden)
            intent.putExtra("numeroOrden",orden.numeroOrden)
            startActivity(intent)
        }

        btnGestionarReserva.setOnClickListener {
            var intent = Intent(requireContext(), GestionarReservaOrdenTrabajoActivity::class.java)
            startActivity(intent)
        }

        btnGestionarResponsables.setOnClickListener {
            var intent = Intent(requireContext(), GestionarResponsablesOrdenTrabajoActivity::class.java)
            startActivity(intent)
        }

        btnDocumentosFormatos.setOnClickListener {
            var intent = Intent(requireContext(), Inspeccion100HorasActivity::class.java)
            startActivity(intent)
        }

        tvEstado.setText(orden.descripcionSituacion)
        tvRubro.setText(orden.rubro)
        tvCodigoOt.setText(orden.codigoOrden)
        tvNumeroOtos.setText(orden.numeroOrden)
        tvCategoria.setText(orden.categoria)
        tvEstacion.setText(orden.nombreEstacion)
        tvFechaInicio.setText(orden.fechaCreacion)
        tvFechaTermino.setText(orden.fechaCierreReal)
        tvLimiteTermino.setText(orden.limiteTermino)
        tvModeloNumeroParte.setText(orden.modeloparte)
        tvMatriculaNomenclatura.setText(orden.matriculanomenclatura)
     //   tvTipoProgramacion.setText(orden.tipoProgramacion)
    //    tvTipoMantenimiento.setText(orden.tipoMantenimiento)
     //   tvAlcanceTrabajo.setText(orden.alcanceTrabajo)
        tvDescripcion.setText(orden.descripcionTrabajo)
        tvTituloDetalleOrden.setText(orden.codigoOrden)

        if(orden.codigoSituacion.equals("00003")){
            view_indicador.setBackgroundResource(R.drawable.indicador_iniciada)
        }
        if(orden.codigoSituacion.equals("00004")){
            view_indicador.setBackgroundResource(R.drawable.indicador_pausada)
        }
        if(orden.codigoSituacion.equals("00002")){
            view_indicador.setBackgroundResource(R.drawable.indicador_generada)
        }

        dialog!!.show()
    }



    class OrdenesAdapter(
        private var ordenes: List<ObtieneOTOSDataTableCloudResponse>,
        private val onLongClick: (ObtieneOTOSDataTableCloudResponse) -> Unit,
        private val onClick: (ObtieneOTOSDataTableCloudResponse) -> Unit
    ) : RecyclerView.Adapter<OrdenesAdapter.OrdenViewHolder>() {

        class OrdenViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val estado: TextView = view.findViewById(R.id.tvEstado)
            val codigoOt: TextView = view.findViewById(R.id.tvCodigoOt)
            val tvNUmeroOt: TextView = view.findViewById(R.id.tvNUmeroOt)
            val fechaInicio: TextView = view.findViewById(R.id.tvFechaInicio)
            val tvMatriculaComp: TextView = view.findViewById(R.id.tvMatriculaComp)
            val view_indicador: View = view.findViewById(R.id.view_indicador)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdenViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_orden_trabajo, parent, false)
            return OrdenViewHolder(view)
        }

        override fun onBindViewHolder(holder: OrdenViewHolder, position: Int) {
            val orden = ordenes[position]
            holder.estado.text = "${orden.descripcionSituacion}"
            holder.codigoOt.text = "Código: ${orden.codigoOrden}"
            holder.tvNUmeroOt.text = "Número: ${orden.numeroOrden}"
           holder.fechaInicio.text = "Creación: ${orden.fechaCreacion}"

            if(orden.rubro != "Aeronave") {
                holder.tvMatriculaComp.text = "Componente: ${orden.matriculanomenclatura}"
            }
            else {
                holder.tvMatriculaComp.text = "Matrícula: ${orden.matriculanomenclatura}"
            }


            holder.itemView.setOnLongClickListener {
                onLongClick(orden)
                true
            }

            holder.itemView.setOnClickListener {
                onClick(orden)
            }

            if(orden.codigoSituacion.equals("00003")){
                holder.view_indicador.setBackgroundResource(R.drawable.indicador_iniciada)
            }
            if(orden.codigoSituacion.equals("00004")){
                holder.view_indicador.setBackgroundResource(R.drawable.indicador_pausada)
            }
            if(orden.codigoSituacion.equals("00002")){
                holder.view_indicador.setBackgroundResource(R.drawable.indicador_generada)
            }


        }

        override fun getItemCount() = ordenes.size

        fun actualizarDatos(nuevos: List<ObtieneOTOSDataTableCloudResponse>) {
            ordenes = nuevos
            notifyDataSetChanged()
        }
    }

    fun showErrorDialog(message: String?) {
        val bundle = Bundle()
        bundle.putString("errorMessage", message)
        val df = ErrorMessageDialog()
        df.arguments = bundle
        df.show(parentFragmentManager, "")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}