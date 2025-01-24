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
import com.helisur.helisurapp.domain.model.Anotacion
import com.helisur.helisurapp.domain.model.Tarea

class ListaAnotacionesAdapter(private val mList: ArrayList<Anotacion>) :
    RecyclerView.Adapter<ListaAnotacionesAdapter.MyViewHolder>() {

    var onItemClick: ((Anotacion) -> Unit)? = null

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nombreTarea: TextView = view.findViewById(R.id.nombreTarea)
        val contenedorReportajes: LinearLayout = view.findViewById(R.id.contenedorReportajes)
        val viewItem: View = view

        val tv_Reportaje_No_Aplica: TextView = view.findViewById(R.id.tv_Reportaje_No_Aplica)
        val tv_Reportaje_RTV: TextView = view.findViewById(R.id.tv_Reportaje_RTV)
        val tv_Reportaje_DanosMenores: TextView = view.findViewById(R.id.tv_Reportaje_DanosMenores)
        val tv_Reportaje_MELMDS: TextView = view.findViewById(R.id.tv_Reportaje_MELMDS)
        val tv_Reportaje_Motivo: TextView = view.findViewById(R.id.tv_Reportaje_Motivo)


        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(mList[adapterPosition])
             //   contenedorReportajes.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ListaAnotacionesAdapter.MyViewHolder {
        val vieww =
            LayoutInflater.from(parent.context).inflate(R.layout.item_anotaciones, parent, false)
        return MyViewHolder(vieww)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val appItem = mList[position]
        holder.nombreTarea.text = appItem.nombreTarea

        if (appItem.reportaje_NoAplica) {
            holder.tv_Reportaje_No_Aplica.visibility = View.VISIBLE
        } else {
            holder.tv_Reportaje_No_Aplica.visibility = View.GONE
        }

        if (appItem.reportaje_RTV) {
            holder.tv_Reportaje_RTV.visibility = View.VISIBLE
        } else {
            holder.tv_Reportaje_RTV.visibility = View.GONE
        }

        if (appItem.reportaje_DanosMenores) {
            holder.tv_Reportaje_DanosMenores.visibility = View.VISIBLE
        } else {
            holder.tv_Reportaje_DanosMenores.visibility = View.GONE
        }

        if (appItem.reportaje_MELMDS) {
            holder.tv_Reportaje_MELMDS.visibility = View.VISIBLE
        } else {
            holder.tv_Reportaje_MELMDS.visibility = View.GONE
        }

        if(appItem.reportaje_Motivo!=null)
        {
            if(appItem.reportaje_Motivo.equals(""))
            {
                holder.tv_Reportaje_Motivo.visibility = View.GONE
            }
            else
            {
                holder.tv_Reportaje_Motivo.text = "Motivo : "+appItem.reportaje_Motivo.toString()
                holder.tv_Reportaje_Motivo.visibility = View.VISIBLE
            }
        }
        else
        {
            holder.tv_Reportaje_Motivo.visibility = View.GONE
        }




        holder.viewItem.setOnClickListener {
            if (holder.contenedorReportajes.isVisible) {
                holder.contenedorReportajes.visibility = View.GONE
            } else {
                holder.contenedorReportajes.visibility = View.VISIBLE
            }
        }


    }

    override fun getItemCount() = mList.size

    fun getTareasRevisadas(): ArrayList<Anotacion> {
        return mList
    }

}