package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo

import android.content.Context
import android.util.Log
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
import com.helisur.helisurapp.data.database.HelisurDatabase
import com.helisur.helisurapp.data.repository.FormatosRepository
import com.helisur.helisurapp.domain.model.Reportaje
import com.helisur.helisurapp.domain.model.Tarea
import com.helisur.helisurapp.domain.util.SessionUserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

class ListaTareasAdapter(val ctx: Context, private val mList: ArrayList<Tarea>) :
    RecyclerView.Adapter<ListaTareasAdapter.MyViewHolder>() {


    @Inject
    lateinit var formatosRepository: FormatosRepository

    private val coroutineScope = CoroutineScope(Job())

    var onItemClick: ((Tarea) -> Unit)? = null

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nombreTarea: TextView = view.findViewById(R.id.nombreTarea)
        val contenedorReportajes: LinearLayout = view.findViewById(R.id.contenedorReportajes)
        val viewItem: View = view

        val reportaje_NoAplica: CheckBox = view.findViewById(R.id.chbx_Reportaje_No_Aplica)
        val reportaje_RTV: CheckBox = view.findViewById(R.id.chbx_Reportaje_RTV)
        val reportaje_DanosMenores: CheckBox = view.findViewById(R.id.chbx_Reportaje_DanosMenores)
        val reportaje_MELMDS: CheckBox = view.findViewById(R.id.chbx_Reportaje_MELMDS)
        val reportaje_Motivo: EditText = view.findViewById(R.id.et_Reportaje_Motivo)
        val contenedor_Motivo: RelativeLayout = view.findViewById(R.id.rl_Reportaje_Motivo)

        init {
            itemView.setOnClickListener {

              //  formatosRepository.getReportajesByTarea(mList[adapterPosition].codigoTarea!!)
                onItemClick?.invoke(mList[adapterPosition])
             //   contenedorReportajes.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ListaTareasAdapter.MyViewHolder {
        val vieww = LayoutInflater.from(parent.context).inflate(R.layout.item_tareas, parent, false)
        return MyViewHolder(vieww)
    }

     fun cargaReportajes(idTarea:String)
    {
  //   var listaReportajes:ArrayList<Reportaje> = TareasFragment.getReportajesByTarea(idTarea)

        //cargar reportakes dinamicos
        var nose = ""

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val appItem = mList[position]
        holder.nombreTarea.text = appItem.nombreTarea


        if(appItem.reportaje_NoAplica)
            holder.reportaje_NoAplica.isChecked = true
        else
            holder.reportaje_NoAplica.isChecked = false

        if(appItem.reportaje_RTV)
            holder.reportaje_RTV.isChecked = true
        else
            holder.reportaje_RTV.isChecked = false

        if(appItem.reportaje_DanosMenores)
            holder.reportaje_DanosMenores.isChecked = true
        else
            holder.reportaje_DanosMenores.isChecked = false

        if(appItem.reportaje_MELMDS)
            holder.reportaje_MELMDS.isChecked = true
        else
            holder.reportaje_MELMDS.isChecked = false

        holder.reportaje_Motivo.setText(appItem.reportaje_Motivo)


        holder.viewItem.setOnClickListener {
            if (holder.contenedorReportajes.isVisible) {
                holder.contenedorReportajes.visibility = View.GONE
            } else {
                cargaReportajes(appItem.codigoTarea!!)
             /*   coroutineScope.launch {
                    cargaReportajes(appItem.codigoTarea!!)
                }

              */
                holder.contenedorReportajes.visibility = View.VISIBLE
            }
        }


        holder.reportaje_NoAplica.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_text_box)
                holder.reportaje_Motivo.isEnabled = true
                appItem.reportaje_NoAplica = true
            } else {
                appItem.reportaje_NoAplica = false
                if (!appItem.reportaje_NoAplica && !appItem.reportaje_RTV && !appItem.reportaje_DanosMenores && !appItem.reportaje_MELMDS) {
                    holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_control_disabled)
                    holder.reportaje_Motivo.isEnabled = false
                } else {
                    holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_text_box)
                    holder.reportaje_Motivo.isEnabled = true
                }
            }
        }


        holder.reportaje_RTV.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_text_box)
                holder.reportaje_Motivo.isEnabled = true
                appItem.reportaje_RTV = true
            } else {
                appItem.reportaje_RTV = false
                if (!appItem.reportaje_NoAplica && !appItem.reportaje_RTV && !appItem.reportaje_DanosMenores && !appItem.reportaje_MELMDS) {
                    holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_control_disabled)
                    holder.reportaje_Motivo.isEnabled = false
                } else {
                    holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_text_box)
                    holder.reportaje_Motivo.isEnabled = true
                }
            }
        }


        holder.reportaje_DanosMenores.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_text_box)
                holder.reportaje_Motivo.isEnabled = true
                appItem.reportaje_DanosMenores = true
            } else {
                appItem.reportaje_DanosMenores = false
                if (!appItem.reportaje_NoAplica && !appItem.reportaje_RTV && !appItem.reportaje_DanosMenores && !appItem.reportaje_MELMDS) {
                    holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_control_disabled)
                    holder.reportaje_Motivo.isEnabled = false
                } else {
                    holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_text_box)
                    holder.reportaje_Motivo.isEnabled = true
                }
            }
        }


        holder.reportaje_MELMDS.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_text_box)
                holder.reportaje_Motivo.isEnabled = true
                appItem.reportaje_MELMDS = true
            } else {
                appItem.reportaje_MELMDS = false
                if (!appItem.reportaje_NoAplica && !appItem.reportaje_RTV && !appItem.reportaje_DanosMenores && !appItem.reportaje_MELMDS) {
                    holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_control_disabled)
                    holder.reportaje_Motivo.isEnabled = false
                } else {
                    holder.contenedor_Motivo.setBackgroundResource(R.drawable.shape_text_box)
                    holder.reportaje_Motivo.isEnabled = true
                }
            }
        }


        holder.reportaje_Motivo.doAfterTextChanged {
            appItem.reportaje_Motivo = holder.reportaje_Motivo.text.toString()
        }

    }

    override fun getItemCount() = mList.size

    fun getTareasRevisadas(): ArrayList<Tarea> {
        return mList
    }


    fun listaReportajes(ctx:Context,idTarea:String) {

        var sessionUserManager = SessionUserManager(context = ctx)
      //  val idDevice = Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID)
      //  var usuario = sessionUserManager.getUser()!!
     //   var pass = sessionUserManager.getPass()!!

        var urlApi = "http://localhost:5628/api/FormatoRegistro/GetFormatoAnotaciones"
        val payload = "{'cadena': '"+idTarea+"'}"

        val okHttpClient = OkHttpClient()
        val requestBody = payload.toRequestBody()

        val request = Request.Builder()
            .post(requestBody)
            .url(urlApi)
            .header("Content-Type", "application/json")
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ERROR LOGIN TOKEN",e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                var responseData = response.body!!.string()
                try {
                    var json = JSONObject(responseData)
                    Log.i("JSON RESPONSE: ", json.toString())
                    val responseObject = json.getJSONObject("data")
                    val contArray = responseObject.getJSONObject("obj")
                    val arrayReportajes = contArray.getJSONArray("table")

                    for(i in 0 until arrayReportajes.length())
                    {
                        val itemReportaje = arrayReportajes.getJSONObject(i)

                        val codigoRegistroFormato = itemReportaje.getJSONObject("codigoRegistroFormato")
                        val codigoTarea = itemReportaje.getJSONObject("codigoTarea")
                        val nombreTarea = itemReportaje.getJSONObject("nombreTarea")

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })


    }





}