package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelofinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.helisur.helisurapp.R
import com.helisur.helisurapp.domain.model.DetalleFormatoRegistro

class ListaAnotacionesPreVueloFinalAdapter(private val mList: ArrayList<DetalleFormatoRegistro>) :
    RecyclerView.Adapter<ListaAnotacionesPreVueloFinalAdapter.MyViewHolder>() {

    var onItemClick: ((DetalleFormatoRegistro) -> Unit)? = null

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvNombreReportaje: TextView = view.findViewById(R.id.tvNombreReportaje)
        val tvNombreTarea: TextView = view.findViewById(R.id.tvNombreTarea)
        val viewItem: View = view


        init {
            itemView.setOnClickListener {
            //    onItemClick?.invoke(mList[adapterPosition])
             //   contenedorReportajes.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MyViewHolder {
        val vieww =
            LayoutInflater.from(parent.context).inflate(R.layout.item_anotaciones_postvuelo, parent, false)
        return MyViewHolder(vieww)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val appItem = mList[position]
        holder.tvNombreReportaje.text = appItem.nombreReportaje
        holder.tvNombreTarea.text = appItem.nombreTarea

    }

    override fun getItemCount() = mList.size

}