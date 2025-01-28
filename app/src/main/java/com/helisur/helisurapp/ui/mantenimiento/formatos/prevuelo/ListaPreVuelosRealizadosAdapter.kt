package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.helisur.helisurapp.R
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosRealizadosDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtienePrevuelosRealizadosCloudResponse

class ListaPreVuelosRealizadosAdapter (private val mList: ArrayList<ObtieneFormatosRealizadosDataTableCloudResponse>) :
    RecyclerView.Adapter<ListaPreVuelosRealizadosAdapter.MyViewHolder>() {

    var onItemClick: ((ObtieneFormatosRealizadosDataTableCloudResponse) -> Unit)? = null

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvItemNombre: TextView = view.findViewById(R.id.tvItemNombre)

        val tvModeloAeronave: TextView = view.findViewById(R.id.tvModeloAeronave)
        val tvAeronave: TextView = view.findViewById(R.id.tvAeronave)
        val tvFecha: TextView = view.findViewById(R.id.tvFecha)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(mList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ListaPreVuelosRealizadosAdapter.MyViewHolder {
        val vieww =
            LayoutInflater.from(parent.context).inflate(R.layout.item_lista_prevuelos_realizados, parent, false)
        return MyViewHolder(vieww)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val appItem = mList[position]
     //   holder.tvItemNombre.text = appItem.message

        holder.tvModeloAeronave.text = appItem.modeloAeronave
        holder.tvAeronave.text = appItem.aeronave
        holder.tvFecha.text = appItem.fechaHoraFinRegistro

    }

    override fun getItemCount() = mList.size


}