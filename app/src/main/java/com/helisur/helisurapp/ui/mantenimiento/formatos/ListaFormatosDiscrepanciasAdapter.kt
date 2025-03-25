package com.helisur.helisurapp.ui.mantenimiento.formatos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.helisur.helisurapp.R
import com.helisur.helisurapp.domain.model.FormatoRegistro

class ListaFormatosDiscrepanciasAdapter (private val mList: ArrayList<FormatoRegistro>,val ctx:Context) :
    RecyclerView.Adapter<ListaFormatosDiscrepanciasAdapter.MyViewHolder>() {

    var onItemClick: ((FormatoRegistro) -> Unit)? = null

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvItemNombre: TextView = view.findViewById(R.id.tvItemNombre)

        val tvModeloAeronave: TextView = view.findViewById(R.id.tvModeloAeronave)
        val tvAeronave: TextView = view.findViewById(R.id.tvAeronave)
        val tvFecha: TextView = view.findViewById(R.id.tvFecha)

        val ivLevantarDiscrepancias: ImageView = view.findViewById(R.id.ivLevantarDiscrepancias)
        val ivEnviarPdf: ImageView = view.findViewById(R.id.ivEnviarPdf)

        val tvNombreFormato: TextView = view.findViewById(R.id.tvNombreFormato)

        init {
            ivLevantarDiscrepancias.setOnClickListener {
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

        holder.ivEnviarPdf.setOnClickListener {
            Toast.makeText(ctx,"PDF generado y enviado",Toast.LENGTH_SHORT).show()

        }


    }

    override fun getItemCount() = mList.size


}