package com.helisur.helisurapp.ui.hojaruta

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.provider.SyncStateContract.Helpers
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.helisur.helisurapp.R
import com.helisur.helisurapp.data.cloud.hojaruta.model.response.ObtieneListaActividadesPorHojaRutaDataTableCloudResponse
import com.helisur.helisurapp.data.repository.AeronavesRepository
import com.helisur.helisurapp.data.repository.HojaRutaRepository
import com.helisur.helisurapp.domain.model.Empleado
import com.helisur.helisurapp.domain.model.ResponsableHojaRuta
import com.helisur.helisurapp.domain.util.SessionUserManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject


class ListaActividadesHojasRutaAdapter (private val mList: ArrayList<ObtieneListaActividadesPorHojaRutaDataTableCloudResponse>,
                                        val ctx:Context,
                                        val listaEmpleados: ArrayList<Empleado>,
                                        val actividad:Activity) :
    RecyclerView.Adapter<ListaActividadesHojasRutaAdapter.MyViewHolder>() {

    var onItemClick: ((ObtieneListaActividadesPorHojaRutaDataTableCloudResponse) -> Unit)? = null
    val myCalendar: Calendar = Calendar.getInstance()


    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
       // val tvItemNombre: TextView = view.findViewById(R.id.tvItemNombre)

        val tvNombreFase: TextView = view.findViewById(R.id.tvNombreFase)
        val tvNombreGrupo: TextView = view.findViewById(R.id.tvNombreGrupo)
        val tvNombreActividad: TextView = view.findViewById(R.id.tvNombreActividad)

        val chxListo: CheckBox = view.findViewById(R.id.chxListo)

        val spiEmpleados: Spinner = view.findViewById(R.id.spiEmpleados)
        val etComentarios: EditText = view.findViewById(R.id.etComentarios)
        val etFecha: TextView = view.findViewById(R.id.etFecha)

        val contenedorItem: CardView = view.findViewById(R.id.contenedorItem)

        val contenedorNombreEmpleado: RelativeLayout = view.findViewById(R.id.contenedorNombreEmpleado)
        val contenedorComentarios: RelativeLayout = view.findViewById(R.id.contenedorComentarios)
        val contenedorFecha: LinearLayout = view.findViewById(R.id.contenedorFecha)

        val tvFechaTitulo: TextView = view.findViewById(R.id.tvFechaTitulo)


        init {
            chxListo.setOnClickListener {
                if(etComentarios.text.toString() == "")
                {
                    Toast.makeText(ctx, "Ingrese comentarios", Toast.LENGTH_SHORT).show()
                    chxListo.isChecked = false
                }
                else
                {
                    if(spiEmpleados.selectedItemPosition == 0)
                    {
                        Toast.makeText(ctx, "Elija un responsable", Toast.LENGTH_SHORT).show()
                        chxListo.isChecked = false
                    }
                    else
                    {
                        onItemClick?.invoke(mList[adapterPosition])
                    }
                }

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
        if(appItem.fechaSeleccion.equals(""))
        {
            holder.etFecha.text = ""
            holder.tvFechaTitulo.visibility = View.GONE
            holder.contenedorFecha.visibility = View.GONE
        }
        else
        {
            holder.etFecha.text = appItem.fechaSeleccion
            holder.tvFechaTitulo.visibility = View.VISIBLE
            holder.contenedorFecha.visibility = View.VISIBLE
        }

        holder.etComentarios.setText(appItem.comentario)

        var codResponsable = appItem.codigoResponsable

        if(appItem.indicadorHabilitado == "0"){
            holder.contenedorItem.setCardBackgroundColor(ctx.resources.getColor(R.color.item_disabled_hoja_ruta))
            holder.contenedorNombreEmpleado.setBackgroundResource(R.drawable.shape_control_disabled)
            holder.contenedorComentarios.setBackgroundResource(R.drawable.shape_control_disabled)
         //   holder.contenedorFecha.setBackgroundResource(R.drawable.shape_control_disabled)

            holder.chxListo.isEnabled = false
            holder.spiEmpleados.isEnabled = false
            holder.etComentarios.isEnabled = false
            holder.etFecha.isEnabled = false

        }
        else
        {

            if(appItem.indicadorHabilitado == "1"){

                holder.contenedorItem.setCardBackgroundColor(ctx.resources.getColor(R.color.color_fondo_card_inicio_conductor))
                holder.contenedorNombreEmpleado.setBackgroundResource(R.drawable.shape_text_box)
                holder.contenedorComentarios.setBackgroundResource(R.drawable.shape_text_box)
                //   holder.contenedorFecha.setBackgroundResource(R.drawable.shape_control_disabled)

                holder.chxListo.isEnabled = true
                holder.spiEmpleados.isEnabled = true
                holder.etComentarios.isEnabled = true
                holder.etFecha.isEnabled = true

            }
            else
            {
                holder.contenedorItem.setCardBackgroundColor(ctx.resources.getColor(R.color.item_disabled_hoja_ruta))
                holder.contenedorNombreEmpleado.setBackgroundResource(R.drawable.shape_control_disabled)
                holder.contenedorComentarios.setBackgroundResource(R.drawable.shape_control_disabled)
                //   holder.contenedorFecha.setBackgroundResource(R.drawable.shape_control_disabled)

                holder.chxListo.isEnabled = false
                holder.spiEmpleados.isEnabled = false
                holder.etComentarios.isEnabled = false
                holder.etFecha.isEnabled = false
            }


        }

        if(appItem.indicadorCumplimiento == "1")
        {
            holder.chxListo.isChecked = true
        }

        holder.contenedorFecha.setOnClickListener {
          //  loadDatePicker(holder.etFecha,position)
        }


        holder.etComentarios.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                appItem.comentario = holder.etComentarios.text.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


        actividad.runOnUiThread(Runnable {
            listaResponsables(ctx,appItem.id,appItem.codigoActividad,holder.spiEmpleados,position,codResponsable,appItem.indicadorHabilitado)
        })


    }

    private fun loadDatePicker(etFecha:TextView,posicion: Int) {
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabelFechaOptima(etFecha,posicion)
        }

        DatePickerDialog(
            ctx,
            date,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }


    private fun updateLabelFechaOptima(etFecha:TextView,posicion: Int) {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        etFecha.setText(sdf.format(myCalendar.time))
        mList[posicion].fechaSeleccion = sdf.format(myCalendar.time)
    }


    override fun getItemCount() = mList.size


    fun listaResponsables(ctx: Context,cadena:String,cadena2:String,spinnerr:Spinner,posicion:Int,codResponsable:String?,indicadorHabilitado:String) {

        var sessionUserManager = SessionUserManager(context = ctx)
        val tokenn = sessionUserManager.getToken()

        var urlApi = "http://38.199.4.100:81/serviceintranetHLS/api/CheckListCabeceraDetalle/getResponsable"
        val payload =
            "{'cadena': '" + cadena + "','cadena2': '" + cadena2 + "'}"

        val okHttpClient = OkHttpClient()
        val requestBody = payload.toRequestBody()

        val request = Request.Builder().post(requestBody).url(urlApi)
            .header("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $tokenn")
            .build()


        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ERROR", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                var responseData = response.body!!.string()
                try {
                    var json = JSONObject(responseData)
                    val responseObject = json.getJSONObject("data")
                    val listaResponsables = responseObject.get("table")

                    val listdata = ArrayList<ResponsableHojaRuta>()
                    val jArray = listaResponsables as JSONArray?
                    if (jArray != null) {
                        for (i in 0 until jArray.length()) {
                            var objetoResponsable = jArray.get(i).toString()

                            var json = JSONObject(objetoResponsable)
                            val iddd = json.get("id")
                            val codigoResponsable = json.get("codigoResponsable")
                            val empleado = json.get("empleado")
                            val indicadorTitular = json.get("indicadorTitular")
                            val usuarioRegistro = json.get("usuarioRegistro")
                            val fechaRegistro = json.get("fechaRegistro")
                            val usuarioModificacion = json.get("usuarioModificacion")
                            val fechaModificacion = json.get("fechaModificacion")

                            listdata.add(ResponsableHojaRuta(iddd.toString(),codigoResponsable.toString(),empleado.toString(),indicadorTitular.toString(),usuarioRegistro.toString(),fechaRegistro.toString(),usuarioModificacion.toString(),fechaModificacion.toString()))
                        }

                        actividad.runOnUiThread(Runnable {
                            setSpinnerResponsable(spinnerr,listdata,posicion,codResponsable,indicadorHabilitado)
                        })

                    }

                    println(json)
                    Log.i("RESPONSE: ", json.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })


    }



    fun setSpinnerResponsable(spinerResponsable:Spinner,lista:ArrayList<ResponsableHojaRuta>,posicion:Int,codResponsable:String?,indicadorHabilitado:String
    ) {
        var spinnerTipo = spinerResponsable
        val spinnerArray: MutableList<String> = ArrayList()
        spinnerArray.add("Seleccione responsable")
        for (item in lista) {
            spinnerArray.add(item.empleado!!)
        }

        var adapter:ArrayAdapter<String>?  = null
        if(indicadorHabilitado == "0")
        {
            adapter = ArrayAdapter(actividad, R.layout.spinner_item_disabled, spinnerArray)
            adapter.setDropDownViewResource(R.layout.spinner_item_disabled)
        }
        else
        {
            if(indicadorHabilitado == "1")
            {
                adapter = ArrayAdapter(actividad, R.layout.spinner_item, spinnerArray)
                adapter.setDropDownViewResource(R.layout.spinner_item)
            }
            else
            {
                adapter = ArrayAdapter(actividad, R.layout.spinner_item_disabled, spinnerArray)
                adapter.setDropDownViewResource(R.layout.spinner_item_disabled)
            }

        }

        spinnerTipo.adapter = adapter
        spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long,
            ) {
                if (position == 0) {
                    mList[posicion].codigoResponsable = ""
                } else {
                    mList[posicion].codigoResponsable = lista[position - 1].codigoResponsable!!
                }
            }
        }

        if(codResponsable!= null)
        {
            if(codResponsable != ""){

                for (i in 0 until lista.size) {
                    if(lista[i].codigoResponsable == codResponsable){
                        spinnerTipo.setSelection(i+1)
                    }
                }
            }
        }

    }


}