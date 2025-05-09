package com.helisur.helisurapp.ui.hojaruta

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.helisur.helisurapp.R
import com.helisur.helisurapp.data.cloud.hojaruta.model.response.ObtieneListaActividadesPorHojaRutaDataTableCloudResponse
import com.helisur.helisurapp.databinding.FragmentActividadesHojaRutaBinding
import com.helisur.helisurapp.domain.model.Empleado
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.SessionUserManager
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.login.LoginViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
import java.net.HttpURLConnection
import java.net.URL


@AndroidEntryPoint
class NuevaHojaRutaActividadesFragment : Fragment() {

    var className = "NuevaHojaRutaActividadesFragment"
    private lateinit var binding: FragmentActividadesHojaRutaBinding
    var loading: TransparentProgressDialog? = null

    var listaActividadesHojaRuta = ArrayList<ObtieneListaActividadesPorHojaRutaDataTableCloudResponse>()
    var nroHojaRuta:String? = ""
    private val hojasRutaViewModel: HojaRutaViewModel by viewModels()
    private val usuarioViewModel: LoginViewModel by viewModels()
    private var empleadoListDB: ArrayList<Empleado>? = null

    var actividadHojaRutaSelected = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentActividadesHojaRutaBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        observers()
        clickListener()
        return root
    }

    fun initUI() {
        loading = TransparentProgressDialog(requireContext())

        var url = "https://firebasestorage.googleapis.com/v0/b/autoservicio-87532.appspot.com/o/acitvidades_imagen.png?alt=media&token=eb8e2676-6aa0-4262-bf66-93bfc0664a0b"

        Picasso.get().load(url).into(binding.ivActividades)
/*
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            val url = URL(url)
            val imageData = url.readBytes()
            binding.ivActividades.setImageBitmap(BitmapFactory.decodeByteArray(imageData, 0, imageData.size))
       //     binding.ivActividades.setImageBitmap(getBitmapFromURL(url.toString()))
        }

 */

        var prefs: SharedPreferences = requireContext().getSharedPreferences("HOJARUTA", Context.MODE_PRIVATE)
        usuarioViewModel.getEmpleadosListDB()
        nroHojaRuta = prefs.getString("nroHojaRuta", "")

    }




    fun clickListener()
    {
        binding.tvAtras.setOnClickListener {

            TabsHojaRuta.viewPager.setCurrentItem(0)
        }
    }

    private fun observers() {


        hojasRutaViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                if (!loading!!.isShowing) {
                    loading!!.show()
                }
            } else {
                if (loading!!.isShowing) {
                    loading!!.dismiss()
                }
            }
        })


        hojasRutaViewModel.hojaRutaState.observe(viewLifecycleOwner, Observer {
            if (it.toString().contains(Constants.ERROR.SUCCESS)) {
            } else {
                if (it.toString().contains(Constants.ERROR.FAILURE)) {
                    var message = it.toString().replace("FAILURE(Error=","")
                    var messagFinal = message.replace(")","")
                    showErrorDialog(messagFinal)
                    Log.e(className, it.toString())
                }
            }
        })


        hojasRutaViewModel.responseObtieneListaActividadesPorHojaRuta.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                listaActividadesHojaRuta = ArrayList(it.data!!.table!!)
                setRecyclerViewHojasRutaActividades(listaActividadesHojaRuta!!)
            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        usuarioViewModel.responseGetEmpleadoListDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                empleadoListDB = ArrayList(it)
                hojasRutaViewModel.obtieneListaActividadesPorHojaRuta(nroHojaRuta!!)
            } else {
                Log.e(className, Constants.ERROR.ERROR)
            }
        })


    }


    fun setRecyclerViewHojasRutaActividades(lista: ArrayList<ObtieneListaActividadesPorHojaRutaDataTableCloudResponse>) {
        val recyclerview = binding.rvActividades
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ListaActividadesHojasRutaAdapter(lista,requireContext(),empleadoListDB!!,requireActivity())
        recyclerview.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                recyclerview.context,
                R.drawable.divider
            )!!
        )

      //  recyclerview.addItemDecoration(dividerItemDecoration)

        adapter.onItemClick = { actividadHojaRuta ->
            actividadHojaRutaSelected = actividadHojaRuta.codigoActividad
            guardaCumplimiento(requireContext(),actividadHojaRuta.id!!,actividadHojaRuta.codigoActividad!!,actividadHojaRuta.comentario!!,actividadHojaRuta.codigoResponsable!!,"1")
        }

        if(actividadHojaRutaSelected == "")
        {

        }
        else
        {
            for (i in 0 until lista.size) {
                if(lista[i].codigoActividad == actividadHojaRutaSelected){
                    binding.rvActividades.scrollToPosition(i)
                }
            }
        }
    }



    fun guardaCumplimiento(ctx: Context,codigoCheckList:String,codigoActividad:String,comentario: String,codigoResponsable:String,indicadorCumplimiento:String) {

        var sessionUserManager = SessionUserManager(context = ctx)
        val tokenn = sessionUserManager.getToken()

        var urlApi = "http://38.199.4.100:81/serviceintranetHLS/api/CheckListCabeceraDetalle/CheckListCumplimiento"
        val payload =
            "{'codigoCheckList': '" + codigoCheckList + "','codigoActividad': '" + codigoActividad + "','comentario':'"+comentario+"','codigoResponsable':'"+codigoResponsable+"','indicadorCumplimiento':'"+indicadorCumplimiento+"'}"

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
                    usuarioViewModel.getEmpleadosListDB()
                    println(json)
                   Log.i("RESPONSE: ", json.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })

    }


    fun showErrorDialog(message: String?) {
        val bundle = Bundle()
        bundle.putString("errorMessage", message)
        val df: ErrorMessageDialog = ErrorMessageDialog()
        df.setArguments(bundle)
        df.show(requireFragmentManager(), "")
    }


    override fun onDestroyView() {
        super.onDestroyView()
     //   _binding = null
    }


    override fun onResume() {
        super.onResume()
        var prueba = ""
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            //  val sessionManager = SessionUserManager(requireContext())
            //  cocursosViewModel.listaPeriodos(sessionManager!!.getToken()!!)
        } else {
        }
    }
}