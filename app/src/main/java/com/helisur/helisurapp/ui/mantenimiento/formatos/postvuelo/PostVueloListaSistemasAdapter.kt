package com.helisur.helisurapp.ui.mantenimiento.formatos.postvuelo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.helisur.helisurapp.R
import com.helisur.helisurapp.domain.model.Sistema
import com.helisur.helisurapp.domain.model.Tarea

class PostVueloListaSistemasAdapter(
    private val ctx: Context,
    private val mListSistemas: ArrayList<Sistema>
) : RecyclerView.Adapter<PostVueloListaSistemasAdapter.MyViewHolder>() {

    // Igual que en PreVuelo: devolvemos también la posición
    var onItemClick: ((Sistema, Int) -> Unit)? = null

    // Permite múltiples ítems expandidos a la vez
    private val expandedPositions = mutableSetOf<Int>()

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nombreSistema: TextView = view.findViewById(R.id.nombreSistemas)
        val rvTareas: RecyclerView = view.findViewById(R.id.rvTareas)

        init {
            itemView.setOnClickListener {
                val pos = adapterPosition
                if (pos == RecyclerView.NO_POSITION) return@setOnClickListener

                // Alternar solo el clicado, sin colapsar los demás
                if (expandedPositions.contains(pos)) {
                    expandedPositions.remove(pos)
                } else {
                    expandedPositions.add(pos)
                }
                notifyItemChanged(pos)

                onItemClick?.invoke(mListSistemas[pos], pos)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sistemas, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = mListSistemas[position]
        holder.nombreSistema.text = item.nombrePosicion

        val isExpanded = expandedPositions.contains(position)
        val tareas = item.tareas

        holder.rvTareas.visibility =
            if (isExpanded && !tareas.isNullOrEmpty()) View.VISIBLE else View.GONE

        if (isExpanded && !tareas.isNullOrEmpty()) {
            if (holder.rvTareas.layoutManager == null) {
                holder.rvTareas.layoutManager = LinearLayoutManager(ctx)
            }
            val current = holder.rvTareas.adapter as? PostVueloListaTareasAdapter
            if (current == null || current.itemCount != tareas.size) {
                holder.rvTareas.adapter = PostVueloListaTareasAdapter(ctx, tareas)
            }
        }
    }

    override fun getItemCount() = mListSistemas.size

    fun updateItem(position: Int, tareas: ArrayList<Tarea>?) {
        mListSistemas[position].tareas = tareas
        // Si estaba expandido, mostrará el rvTareas al rebind
        notifyItemChanged(position)
    }

    // (Opcional) utilidades
    fun expand(position: Int) { expandedPositions.add(position); notifyItemChanged(position) }
    fun collapse(position: Int) { expandedPositions.remove(position); notifyItemChanged(position) }
    fun isExpanded(position: Int) = expandedPositions.contains(position)
}
