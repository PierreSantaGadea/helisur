package com.helisur.helisurapp.ui.hojaruta

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.helisur.helisurapp.R
import com.helisur.helisurapp.data.cloud.hojaruta.model.response.ObtieneListaActividadesPorHojaRutaDataTableCloudResponse
import com.helisur.helisurapp.domain.model.Empleado
import com.helisur.helisurapp.domain.model.FormatoRegistro

class ListaActividadesHojasRutaAdapter (private val mList: ArrayList<ObtieneListaActividadesPorHojaRutaDataTableCloudResponse>,
                                        val ctx:Context,
                                        val listaEmpleados: ArrayList<Empleado>) :
    RecyclerView.Adapter<ListaActividadesHojasRutaAdapter.MyViewHolder>() {

    var onItemClick: ((ObtieneListaActividadesPorHojaRutaDataTableCloudResponse) -> Unit)? = null

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
       // val tvItemNombre: TextView = view.findViewById(R.id.tvItemNombre)

        val tvNombreFase: TextView = view.findViewById(R.id.tvNombreFase)
        val tvNombreGrupo: TextView = view.findViewById(R.id.tvNombreGrupo)
        val tvNombreActividad: TextView = view.findViewById(R.id.tvNombreActividad)

        val chxListo: CheckBox = view.findViewById(R.id.chxListo)

        val spiEmpleados: Spinner = view.findViewById(R.id.spiEmpleados)
        val etComentarios: EditText = view.findViewById(R.id.etComentarios)
        val etFecha: EditText = view.findViewById(R.id.etFecha)

        val contenedorItem: CardView = view.findViewById(R.id.contenedorItem)

        val contenedorNombreEmpleado: RelativeLayout = view.findViewById(R.id.contenedorNombreEmpleado)
        val contenedorComentarios: RelativeLayout = view.findViewById(R.id.contenedorComentarios)
        val contenedorFecha: RelativeLayout = view.findViewById(R.id.contenedorFecha)


        init {
            tvNombreActividad.setOnClickListener {
                onItemClick?.invoke(mList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MyViewHolder {
        val vieww =
            LayoutInflater.from(parent.context).inflate(R.layout.item_actividades_hojas_ruta, parent, false)
        return MyViewHolder(vieww)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val appItem = mList[position]
        holder.tvNombreActividad.text = appItem.codigoActividad +" - "+appItem.nombreActividad
        holder.tvNombreFase.text = appItem.nombreFase
        holder.tvNombreGrupo.text = appItem.nombreGrupo

        if(appItem.indicadorHabilitado == "0"){
            holder.contenedorItem.setCardBackgroundColor(ctx.resources.getColor(R.color.item_disabled_hoja_ruta))
            holder.contenedorNombreEmpleado.setBackgroundResource(R.drawable.shape_control_disabled)
            holder.contenedorComentarios.setBackgroundResource(R.drawable.shape_control_disabled)
            holder.contenedorFecha.setBackgroundResource(R.drawable.shape_control_disabled)

            holder.chxListo.isEnabled = false
            holder.spiEmpleados.isEnabled = false
            holder.etComentarios.isEnabled = false
            holder.etFecha.isEnabled = false
        }


        holder.tvNombreGrupo.setOnClickListener {
            Toast.makeText(ctx,"PDF generado y enviado",Toast.LENGTH_SHORT).show()

        }


    }

    override fun getItemCount() = mList.size


}