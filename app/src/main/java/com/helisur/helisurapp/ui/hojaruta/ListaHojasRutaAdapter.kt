package com.helisur.helisurapp.ui.hojaruta

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.helisur.helisurapp.R
import com.helisur.helisurapp.data.cloud.hojaruta.model.response.ObtieneHojasRutaDataTableCloudResponse
import com.helisur.helisurapp.domain.model.FormatoRegistro

class ListaHojasRutaAdapter (private val mList: ArrayList<ObtieneHojasRutaDataTableCloudResponse>, val ctx:Context) :
    RecyclerView.Adapter<ListaHojasRutaAdapter.MyViewHolder>() {

    var onItemClick: ((ObtieneHojasRutaDataTableCloudResponse) -> Unit)? = null

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvItemNombre: TextView = view.findViewById(R.id.tvItemNombre)
        val tvModeloAeronave: TextView = view.findViewById(R.id.tvModeloAeronave)
        val tvAeronave: TextView = view.findViewById(R.id.tvAeronave)
        val tvFecha: TextView = view.findViewById(R.id.tvFecha)
        val ivEnviarPdf: ImageView = view.findViewById(R.id.ivEnviarPdf)
        val tvUbicacion: TextView = view.findViewById(R.id.tvNombreFormato)

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
            LayoutInflater.from(parent.context).inflate(R.layout.item_hojas_ruta, parent, false)
        return MyViewHolder(vieww)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val appItem = mList[position]
        holder.tvAeronave.text = appItem.modeloAeronave+" - "+appItem.aeronave
        holder.tvUbicacion.text = appItem.nombreEstacion

    }

    override fun getItemCount() = mList.size


}