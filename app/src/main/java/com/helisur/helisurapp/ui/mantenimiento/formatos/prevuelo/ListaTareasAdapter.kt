package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.helisur.helisurapp.R
import com.helisur.helisurapp.domain.model.Tarea

class ListaTareasAdapter(private val mList: ArrayList<Tarea>) :
    RecyclerView.Adapter<ListaTareasAdapter.MyViewHolder>() {

    var onItemClick: ((Tarea) -> Unit)? = null

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nombreTarea: TextView = view.findViewById(R.id.nombreTarea)
        val contenedorReportajes: LinearLayout = view.findViewById(R.id.contenedorReportajes)
        val viewItem: View = view

        val reportaje_NoAplica: CheckBox = view.findViewById(R.id.chbx_Reportaje_No_Aplica)
        val reportaje_RTV: CheckBox = view.findViewById(R.id.chbx_Reportaje_RTV)
        val reportaje_DanosMenores: CheckBox = view.findViewById(R.id.chbx_Reportaje_DanosMenores)
        val reportaje_MELMDS: CheckBox = view.findViewById(R.id.chbx_Reportaje_MELMDS)
        val reportaje_Motivo: EditText = view.findViewById(R.id.et_Reportaje_Motivo)
        val contenedor_Motivo: RelativeLayout = view.findViewById(R.id.rl_Reportaje_Motivo)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(mList[adapterPosition])
                contenedorReportajes.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ListaTareasAdapter.MyViewHolder {
        val vieww = LayoutInflater.from(parent.context).inflate(R.layout.item_tareas, parent, false)
        return MyViewHolder(vieww)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val appItem = mList[position]
        holder.nombreTarea.text = appItem.nombreTarea


        holder.viewItem.setOnClickListener {
            if (holder.contenedorReportajes.isVisible) {
                holder.contenedorReportajes.visibility = View.GONE
            } else {
                holder.contenedorReportajes.visibility = View.VISIBLE
            }
        }


        holder.reportaje_NoAplica.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_text_box)
                holder.reportaje_Motivo.isEnabled = true
                appItem.reportaje_NoAplica = true
            } else {
                appItem.reportaje_NoAplica = false
                if (!appItem.reportaje_NoAplica && !appItem.reportaje_RTV && !appItem.reportaje_DanosMenores && !appItem.reportaje_MELMDS) {
                    holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_control_disabled)
                    holder.reportaje_Motivo.isEnabled = false
                } else {
                    holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_text_box)
                    holder.reportaje_Motivo.isEnabled = true
                }
            }
        }


        holder.reportaje_RTV.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_text_box)
                holder.reportaje_Motivo.isEnabled = true
                appItem.reportaje_RTV = true
            } else {
                appItem.reportaje_RTV = false
                if (!appItem.reportaje_NoAplica && !appItem.reportaje_RTV && !appItem.reportaje_DanosMenores && !appItem.reportaje_MELMDS) {
                    holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_control_disabled)
                    holder.reportaje_Motivo.isEnabled = false
                } else {
                    holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_text_box)
                    holder.reportaje_Motivo.isEnabled = true
                }
            }
        }


        holder.reportaje_DanosMenores.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_text_box)
                holder.reportaje_Motivo.isEnabled = true
                appItem.reportaje_DanosMenores = true
            } else {
                appItem.reportaje_DanosMenores = false
                if (!appItem.reportaje_NoAplica && !appItem.reportaje_RTV && !appItem.reportaje_DanosMenores && !appItem.reportaje_MELMDS) {
                    holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_control_disabled)
                    holder.reportaje_Motivo.isEnabled = false
                } else {
                    holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_text_box)
                    holder.reportaje_Motivo.isEnabled = true
                }
            }
        }


        holder.reportaje_MELMDS.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_text_box)
                holder.reportaje_Motivo.isEnabled = true
                appItem.reportaje_MELMDS = true
            } else {
                appItem.reportaje_MELMDS = false
                if (!appItem.reportaje_NoAplica && !appItem.reportaje_RTV && !appItem.reportaje_DanosMenores && !appItem.reportaje_MELMDS) {
                    holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_control_disabled)
                    holder.reportaje_Motivo.isEnabled = false
                } else {
                    holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_text_box)
                    holder.reportaje_Motivo.isEnabled = true
                }
            }
        }


        holder.reportaje_Motivo.doAfterTextChanged {
            appItem.reportaje_Motivo = holder.reportaje_Motivo.text.toString()
        }

    }

    override fun getItemCount() = mList.size

}