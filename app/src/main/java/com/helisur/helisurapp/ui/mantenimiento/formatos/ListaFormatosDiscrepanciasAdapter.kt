package com.helisur.helisurapp.ui.mantenimiento.formatos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.helisur.helisurapp.R
import com.helisur.helisurapp.domain.model.FormatoRegistro

class ListaFormatosDiscrepanciasAdapter (private val mList: ArrayList<FormatoRegistro>) :
    RecyclerView.Adapter<ListaFormatosDiscrepanciasAdapter.MyViewHolder>() {

    var onItemClick: ((FormatoRegistro) -> Unit)? = null

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvItemNombre: TextView = view.findViewById(R.id.tvItemNombre)

        val tvModeloAeronave: TextView = view.findViewById(R.id.tvModeloAeronave)
        val tvAeronave: TextView = view.findViewById(R.id.tvAeronave)
        val tvFecha: TextView = view.findViewById(R.id.tvFecha)

        val tvNombreFormato: TextView = view.findViewById(R.id.tvNombreFormato)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(mList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MyViewHolder {
        val vieww =
            LayoutInflater.from(parent.context).inflate(R.layout.item_lista_prevuelos_realizados, parent, false)
        return MyViewHolder(vieww)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val appItem = mList[position]
     //   holder.tvItemNombre.text = appItem.message

        if(appItem.codigoFormato.equals("00001")){

            holder.tvNombreFormato.text = "Pre-Vuelo"
        }
        else
        {
            holder.tvNombreFormato.text = "Post-Vuelo"
        }

        holder.tvModeloAeronave.text = appItem.codigoEstacion
        holder.tvAeronave.text = appItem.nombreAeronave
        holder.tvFecha.text = appItem.fechaRegistro

    }

    override fun getItemCount() = mList.size


}