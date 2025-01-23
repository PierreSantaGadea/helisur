package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.helisur.helisurapp.R
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasDataTableCloudResponse

class ListaTareasAdapter (private val mList: ArrayList<ObtieneTareasDataTableCloudResponse>) :
    RecyclerView.Adapter<ListaTareasAdapter.MyViewHolder>() {

    var onItemClick: ((ObtieneTareasDataTableCloudResponse) -> Unit)? = null

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nombreTarea: TextView = view.findViewById(R.id.nombreTarea)
        val contenedorReportajes: LinearLayout = view.findViewById(R.id.contenedorReportajes)

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
        val vieww =
            LayoutInflater.from(parent.context).inflate(R.layout.item_tareas, parent, false)
        return MyViewHolder(vieww)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val appItem = mList[position]
        holder.nombreTarea.text = appItem.nombreTarea

    }

    override fun getItemCount() = mList.size















}