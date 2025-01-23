package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.helisur.helisurapp.R
import com.helisur.helisurapp.domain.model.Sistema
import com.helisur.helisurapp.domain.model.Tarea

class ListaSistemasAdapter(
    val ctx: Context, private val mListSistemas: ArrayList<Sistema>
) : RecyclerView.Adapter<ListaSistemasAdapter.MyViewHolder>() {

    var onItemClick: ((Sistema) -> Unit)? = null

    var posss: Int? = null

    var myViewholder: MyViewHolder? = null

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nombreSistema: TextView = view.findViewById(R.id.nombreSistema)
        val rvTareas: RecyclerView = view.findViewById(R.id.rvTareas)
        init {
            itemView.setOnClickListener {
                posss = adapterPosition
                onItemClick?.invoke(mListSistemas[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ListaSistemasAdapter.MyViewHolder {
        val vieww =
            LayoutInflater.from(parent.context).inflate(R.layout.item_sistemas, parent, false)
        return MyViewHolder(vieww)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val appItem = mListSistemas[position]
        holder.nombreSistema.text = appItem.nombrePosicion
        if (appItem.isSelected!!) {
            myViewholder = holder
            if (holder.rvTareas.isVisible) {
                holder.rvTareas.visibility = View.GONE
            } else {

                if (appItem.tareas != null) {
                    if (appItem.tareas!!.size > 0) {
                        holder.rvTareas.layoutManager = LinearLayoutManager(ctx)
                        val adapter = ListaTareasAdapter(appItem.tareas!!)
                        holder.rvTareas.adapter = adapter
                        holder.rvTareas.visibility = View.VISIBLE
                    } else {
                        holder.rvTareas.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun getItemCount() = mListSistemas.size


    fun updateItem(position: Int, tareas: ArrayList<Tarea>?) {
        mListSistemas.get(position).tareas = tareas

        notifyItemChanged(position)

    }

    fun getPosition(): Int {
        return posss!!
    }


}