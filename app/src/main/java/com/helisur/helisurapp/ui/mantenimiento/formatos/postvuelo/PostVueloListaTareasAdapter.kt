package com.helisur.helisurapp.ui.mantenimiento.formatos.postvuelo

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.helisur.helisurapp.R
import com.helisur.helisurapp.domain.model.Reportaje
import com.helisur.helisurapp.domain.model.Tarea
import com.helisur.helisurapp.domain.model.TareaFormato
import com.helisur.helisurapp.domain.util.FormatoBorradorManager
import com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo.PreVueloListaTareasAdapter

class PostVueloListaTareasAdapter(
    private val ctx: Context,
    private val mList: ArrayList<Tarea>
) : RecyclerView.Adapter<PostVueloListaTareasAdapter.MyViewHolder>() {

    var onItemClick: ((Tarea) -> Unit)? = null

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nombreTarea: TextView = view.findViewById(R.id.tvNombreTarea)
        val contenedorReportajes: LinearLayout = view.findViewById(R.id.contenedorReportajes)
        val viewItem: View = view

        val reportaje_NoAplica: CheckBox = view.findViewById(R.id.chbx_Reportaje_No_Aplica)
        val reportaje_RTV: CheckBox = view.findViewById(R.id.chbx_Reportaje_RTV)
        val reportaje_DanosMenores: CheckBox = view.findViewById(R.id.chbx_Reportaje_DanosMenores)
        val reportaje_MELMDS: CheckBox = view.findViewById(R.id.chbx_Reportaje_MELMDS)
        val reportaje_Motivo: EditText = view.findViewById(R.id.et_Reportaje_Motivo)
        val contenedor_Motivo: RelativeLayout = view.findViewById(R.id.rl_Reportaje_Motivo)

        val ivInfo: ImageView = view.findViewById(R.id.ivInfo)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(mList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_tareas, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = mList[position]
        val idTarea = item.codigoTarea ?: return
        val manager = FormatoBorradorManager(ctx)

        holder.nombreTarea.text = item.nombreTarea

        // 1) Desacoplar listeners antes de setear checks
        holder.reportaje_NoAplica.setOnCheckedChangeListener(null)
        holder.reportaje_RTV.setOnCheckedChangeListener(null)
        holder.reportaje_DanosMenores.setOnCheckedChangeListener(null)
        holder.reportaje_MELMDS.setOnCheckedChangeListener(null)

        // 2) Setear estado desde el modelo (idempotente)
        holder.reportaje_NoAplica.isChecked = item.reportaje_NoAplica
        holder.reportaje_RTV.isChecked = item.reportaje_RTV
        holder.reportaje_DanosMenores.isChecked = item.reportaje_DanosMenores
        holder.reportaje_MELMDS.isChecked = item.reportaje_MELMDS

        // 3) Re-agregar listeners
        holder.reportaje_NoAplica.setOnCheckedChangeListener { _, checked ->
            item.reportaje_NoAplica = checked
            actualizarCampoTarea(idTarea, "noAplica", checked, manager)
            toggleMotivoEnabled(holder, item)
        }
        holder.reportaje_RTV.setOnCheckedChangeListener { _, checked ->
            item.reportaje_RTV = checked
            actualizarCampoTarea(idTarea, "rtv", checked, manager)
            toggleMotivoEnabled(holder, item)
        }
        holder.reportaje_DanosMenores.setOnCheckedChangeListener { _, checked ->
            item.reportaje_DanosMenores = checked
            actualizarCampoTarea(idTarea, "danosMenores", checked, manager)
            toggleMotivoEnabled(holder, item)
        }
        holder.reportaje_MELMDS.setOnCheckedChangeListener { _, checked ->
            item.reportaje_MELMDS = checked
            actualizarCampoTarea(idTarea, "melMds", checked, manager)
            toggleMotivoEnabled(holder, item)
        }

        // 4) TextWatcher sin duplicados (usa R.id.tag_text_watcher)
        (holder.reportaje_Motivo.getTag(R.id.tag_text_watcher) as? TextWatcher)?.let {
            holder.reportaje_Motivo.removeTextChangedListener(it)
        }
        val textoNuevo = item.reportaje_Motivo ?: ""
        if (holder.reportaje_Motivo.text.toString() != textoNuevo) {
            holder.reportaje_Motivo.setText(textoNuevo)
        }
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                item.reportaje_Motivo = s.toString()
                if (s.isNotEmpty()) actualizarCampoTarea(idTarea, "motivo", s.toString(), manager)
            }
        }
        holder.reportaje_Motivo.addTextChangedListener(watcher)
        holder.reportaje_Motivo.setTag(R.id.tag_text_watcher, watcher)

        // 5) Click para mostrar/ocultar el bloque de reportajes
        holder.viewItem.setOnClickListener {
            val visible = holder.contenedorReportajes.isVisible
            holder.contenedorReportajes.visibility = if (visible) View.GONE else View.VISIBLE
            holder.ivInfo.visibility = holder.contenedorReportajes.visibility
            if (!visible) {
                // Aplicar defaults de reportajes solo al abrir (idempotente)
                val listaReportajes: ArrayList<Reportaje> =
                    PostVueloTareasFragment.getReportajesByTarea(idTarea)
                applyDefaultReportajesIfNeeded(holder, item, listaReportajes, manager)
            }
            toggleMotivoEnabled(holder, item)
        }

        // 6) Restaurar desde manager si existe
        manager.getListaTareaFormato().firstOrNull { it.idTareaFormato == idTarea }?.let { t ->
            // Evitar disparar listeners: quitar, setear, volver a agregar
            holder.reportaje_NoAplica.setOnCheckedChangeListener(null)
            holder.reportaje_RTV.setOnCheckedChangeListener(null)
            holder.reportaje_DanosMenores.setOnCheckedChangeListener(null)
            holder.reportaje_MELMDS.setOnCheckedChangeListener(null)

            if (holder.reportaje_NoAplica.isChecked != t.noAplica) {
                holder.reportaje_NoAplica.isChecked = t.noAplica
                item.reportaje_NoAplica = t.noAplica
            }
            if (holder.reportaje_RTV.isChecked != t.rtv) {
                holder.reportaje_RTV.isChecked = t.rtv
                item.reportaje_RTV = t.rtv
            }
            if (holder.reportaje_DanosMenores.isChecked != t.danosMenores) {
                holder.reportaje_DanosMenores.isChecked = t.danosMenores
                item.reportaje_DanosMenores = t.danosMenores
            }
            if (holder.reportaje_MELMDS.isChecked != t.melMds) {
                holder.reportaje_MELMDS.isChecked = t.melMds
                item.reportaje_MELMDS = t.melMds
            }
            if ((t.motivo ?: "") != holder.reportaje_Motivo.text.toString()) {
                holder.reportaje_Motivo.setText(t.motivo ?: "")
                item.reportaje_Motivo = t.motivo ?: ""
            }

            // Re-enganchar listeners
            holder.reportaje_NoAplica.setOnCheckedChangeListener { _, checked ->
                item.reportaje_NoAplica = checked
                actualizarCampoTarea(idTarea, "noAplica", checked, manager)
                toggleMotivoEnabled(holder, item)
            }
            holder.reportaje_RTV.setOnCheckedChangeListener { _, checked ->
                item.reportaje_RTV = checked
                actualizarCampoTarea(idTarea, "rtv", checked, manager)
                toggleMotivoEnabled(holder, item)
            }
            holder.reportaje_DanosMenores.setOnCheckedChangeListener { _, checked ->
                item.reportaje_DanosMenores = checked
                actualizarCampoTarea(idTarea, "danosMenores", checked, manager)
                toggleMotivoEnabled(holder, item)
            }
            holder.reportaje_MELMDS.setOnCheckedChangeListener { _, checked ->
                item.reportaje_MELMDS = checked
                actualizarCampoTarea(idTarea, "melMds", checked, manager)
                toggleMotivoEnabled(holder, item)
            }
        }

        // 7) Estado inicial del campo motivo (enabled/disabled)
        toggleMotivoEnabled(holder, item)

        // 8) Info de tarea
        holder.ivInfo.setOnClickListener {
            // Si tienes un dialog similar al de PreVuelo, llámalo aquí.
             showDialogInfo(item.instruccion ?: "")
        }
    }

    override fun getItemCount() = mList.size

    fun getTareasRevisadas(): ArrayList<Tarea> = mList

    private fun toggleMotivoEnabled(holder: MyViewHolder, item: Tarea) {
        val algunoMarcado = item.reportaje_NoAplica || item.reportaje_RTV ||
                item.reportaje_DanosMenores || item.reportaje_MELMDS
        holder.contenedor_Motivo.setBackgroundResource(
            if (algunoMarcado) R.drawable.shape_text_box else R.drawable.shape_control_disabled
        )
        holder.reportaje_Motivo.isEnabled = algunoMarcado
    }



    private fun actualizarCampoTarea(
        idTarea: String,
        campo: String,
        valor: Any,
        manager: FormatoBorradorManager
    ) {
        val lista = manager.getListaTareaFormato().toMutableList()
        val index = lista.indexOfFirst { it.idTareaFormato == idTarea }

        if (index != -1) {
            val t = lista[index]
            val nueva = when (campo) {
                "noAplica" -> t.copy(noAplica = valor as Boolean)
                "rtv" -> t.copy(rtv = valor as Boolean)
                "danosMenores" -> t.copy(danosMenores = valor as Boolean)
                "melMds" -> t.copy(melMds = valor as Boolean)
                "motivo" -> t.copy(motivo = valor as String)
                else -> t
            }
            lista[index] = nueva
        } else {
            val nueva = TareaFormato(
                idTareaFormato = idTarea,
                noAplica = if (campo == "noAplica") valor as Boolean else false,
                rtv = if (campo == "rtv") valor as Boolean else false,
                danosMenores = if (campo == "danosMenores") valor as Boolean else false,
                melMds = if (campo == "melMds") valor as Boolean else false,
                motivo = if (campo == "motivo") valor as String else ""
            )
            lista.add(nueva)
        }
        manager.saveListaTareaFormato(lista)
    }

    private fun applyDefaultReportajesIfNeeded(
        holder: PostVueloListaTareasAdapter.MyViewHolder,
        item: Tarea,
        listaReportajes: ArrayList<Reportaje>,
        manager: FormatoBorradorManager
    ) {
        // Si ya hay algo marcado o motivo no vacío, no fuerces defaults.
        if (item.reportaje_NoAplica || item.reportaje_RTV ||
            item.reportaje_DanosMenores || item.reportaje_MELMDS ||
            !item.reportaje_Motivo.isNullOrEmpty()
        ) return

        // Quitar listeners temporariamente
        holder.reportaje_NoAplica.setOnCheckedChangeListener(null)
        holder.reportaje_RTV.setOnCheckedChangeListener(null)
        holder.reportaje_DanosMenores.setOnCheckedChangeListener(null)
        holder.reportaje_MELMDS.setOnCheckedChangeListener(null)

        for (r in listaReportajes) {
            if (r.defaultt == "1") {
                if (r.nombreReportaje.contains("No Aplica", ignoreCase = true)) {
                    holder.reportaje_NoAplica.isChecked = true
                    item.reportaje_NoAplica = true
                    actualizarCampoTarea(item.codigoTarea!!, "noAplica", true, manager)
                }
                if (r.nombreReportaje.contains("MEL", ignoreCase = true)) {
                    holder.reportaje_MELMDS.isChecked = true
                    item.reportaje_MELMDS = true
                    actualizarCampoTarea(item.codigoTarea!!, "melMds", true, manager)
                }
                if (r.nombreReportaje.contains("RTV", ignoreCase = true)) {
                    holder.reportaje_RTV.isChecked = true
                    item.reportaje_RTV = true
                    actualizarCampoTarea(item.codigoTarea!!, "rtv", true, manager)
                }
                if (r.nombreReportaje.contains("danos", ignoreCase = true) ||
                    r.nombreReportaje.contains("daños", ignoreCase = true)) {
                    holder.reportaje_DanosMenores.isChecked = true
                    item.reportaje_DanosMenores = true
                    actualizarCampoTarea(item.codigoTarea!!, "danosMenores", true, manager)
                }
            }
        }

        // Re-enganchar listeners
        holder.reportaje_NoAplica.setOnCheckedChangeListener { _, checked ->
            item.reportaje_NoAplica = checked
            actualizarCampoTarea(item.codigoTarea!!, "noAplica", checked, manager)
            toggleMotivoEnabled(holder, item)
        }
        holder.reportaje_RTV.setOnCheckedChangeListener { _, checked ->
            item.reportaje_RTV = checked
            actualizarCampoTarea(item.codigoTarea!!, "rtv", checked, manager)
            toggleMotivoEnabled(holder, item)
        }
        holder.reportaje_DanosMenores.setOnCheckedChangeListener { _, checked ->
            item.reportaje_DanosMenores = checked
            actualizarCampoTarea(item.codigoTarea!!, "danosMenores", checked, manager)
            toggleMotivoEnabled(holder, item)
        }
        holder.reportaje_MELMDS.setOnCheckedChangeListener { _, checked ->
            item.reportaje_MELMDS = checked
            actualizarCampoTarea(item.codigoTarea!!, "melMds", checked, manager)
            toggleMotivoEnabled(holder, item)
        }
    }


    private fun showDialogInfo(info:String) {
        val dialog = Dialog(ctx)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_info_tarea)
        dialog.getWindow()!!.getAttributes().windowAnimations = R.style.DialogAnimation

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.attributes.alpha = 1f
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val body = dialog.findViewById(R.id.tvMensaje) as TextView
        body.text = info


        val btnCerrar = dialog.findViewById(R.id.btnCerrar) as RelativeLayout
        btnCerrar.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

}
