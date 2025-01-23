package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.helisur.helisurapp.R
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasDataTableCloudResponse

class ListaSistemasAdapter (
                            val ctx:Context,
                            private val mListSistemas: ArrayList<ObtieneSistemasDataTableCloudResponse>,
                            private val mListTareas: ArrayList<ObtieneTareasDataTableCloudResponse>?
    ) :
    RecyclerView.Adapter<ListaSistemasAdapter.MyViewHolder>() {

    var onItemClick: ((ObtieneSistemasDataTableCloudResponse) -> Unit)? = null

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nombreSistema: TextView = view.findViewById(R.id.nombreSistema)
        val rvTareas: RecyclerView = view.findViewById(R.id.rvTareas)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(mListSistemas[adapterPosition])
             //   setRecyclerViewTareas()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ListaSistemasAdapter.MyViewHolder {
        val vieww = LayoutInflater.from(parent.context).inflate(R.layout.item_sistemas, parent, false)
        return MyViewHolder(vieww)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val appItem = mListSistemas[position]
        holder.nombreSistema.text = appItem.nombrePosicion
/*
        holder.view.setOnClickListener {

            holder.rvTareas.visibility = View.VISIBLE
        }

 */

        if(mListTareas != null)
        {

            if(mListTareas.size>0)
            {
                setRecyclerViewTareas(mListTareas,holder.rvTareas)
                holder.rvTareas.visibility = View.VISIBLE
            }
            else
            {

            }

        }




    }

    override fun getItemCount() = mListSistemas.size




    fun setRecyclerViewTareas(lista: ArrayList<ObtieneTareasDataTableCloudResponse>,rvTareas:RecyclerView) {
        val recyclerview = rvTareas
        recyclerview.layoutManager = LinearLayoutManager(ctx)
        val adapter = ListaTareasAdapter(lista)
        recyclerview.adapter = adapter


    }









}