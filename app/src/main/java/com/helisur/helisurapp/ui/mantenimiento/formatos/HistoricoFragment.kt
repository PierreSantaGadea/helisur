package com.helisur.helisurapp.ui.mantenimiento.formatos


import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.widget.CompoundButtonCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.helisur.helisurapp.R
import com.helisur.helisurapp.databinding.FragmentHistoricoBinding
import com.helisur.helisurapp.domain.model.Aeronave
import com.helisur.helisurapp.domain.model.DetalleFormatoRegistro
import com.helisur.helisurapp.domain.model.Empleado
import com.helisur.helisurapp.domain.model.Estacion
import com.helisur.helisurapp.domain.model.Formato
import com.helisur.helisurapp.domain.model.FormatoRegistro
import com.helisur.helisurapp.domain.model.ModeloAeronave
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.login.LoginViewModel
import com.helisur.helisurapp.ui.mantenimiento.AeronavesViewModel
import dagger.hilt.android.AndroidEntryPoint
import pl.polidea.view.ZoomView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import kotlin.math.max
import kotlin.math.min


@AndroidEntryPoint
class HistoricoFragment  : Fragment() {

    var className = "ConcursosAvancesFragment"

    private var _binding: FragmentHistoricoBinding? = null
    private val binding get() = _binding!!

    private val aeronavesViewModel: AeronavesViewModel by viewModels()
    private val formatosViewModel: FormatosViewModel by viewModels()
    private val usuariosViewModel: LoginViewModel by viewModels()

  //  private var aeronavesList: ArrayList<ObtieneAeronavesDataTableCloudResponse>? = null
    private var aeronavesList: ArrayList<ModeloAeronave>? = null
 //   private var formatosList: ArrayList<ObtieneFormatosDataTableCloudResponse>? = null
    private var formatosList: ArrayList<Formato>? = null

    var loading: TransparentProgressDialog? = null

    var listaFormatosRealizados = ArrayList<FormatoRegistro>()

    var formatoSelected:FormatoRegistro? = null
    var listaDetalleFormatoRegistro : ArrayList<DetalleFormatoRegistro>? = null

    var listaEstacionesDb: ArrayList<Estacion>? = null
    var listaModelosAeronave:ArrayList<ModeloAeronave>? = null
    var listaAeronaves:ArrayList<Aeronave>? = null
    var listaEmpleados:ArrayList<Empleado>? = null


    private var idAeronave:String = ""
    private var nombreAeronave:String = ""
    private var idFormato:String = ""
    private var nombreFormato:String = ""

    private lateinit var mScaleGestureDetector: ScaleGestureDetector
    private var mScaleFactor = 1.0f

    private var vista:LinearLayout? = null

    private var filePdf : File? = null


    fun loadBitmapFromView(v: View): Bitmap? {
        if (v.measuredHeight <= 0) {
            v.measure(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
            val b = Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            v.layout(0, 0, v.measuredWidth, v.measuredHeight)
            v.draw(c)
            return b
        }
        else{
            v.measure(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
            val b = Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            v.layout(0, 0, v.measuredWidth, v.measuredHeight)
            v.draw(c)
            return b
        }
    }

    fun generaFormatoPDF(nombreDocumento:String) {

        var pageHeight = 1950
        var pageWidth = 635

        var pdfDocument: PdfDocument = PdfDocument()

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        var paint: Paint = Paint()
        var title: Paint = Paint()

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        var myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 3).create()

        // below line is used for setting
        // start page for our PDF file.
        var myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        // creating a variable for canvas
        // from our page of PDF.
        var canvas: Canvas = myPage.canvas





        //acaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa


        val startX = 20f
        val startY = 50f
        val tableWidth = 555f
        val col1Width = 120f
        val col2Width = 230f
        val col3Width = tableWidth - col1Width - col2Width
        val rowHeight = 60f

        val bitmap: Bitmap? = loadBitmapFromView(binding.llDetalleFormato!!)
      //  val scaledBitmap: Bitmap = Bitmap.createScaledBitmap(bitmap!!, pageWidth, pageHeight, false)
        canvas.drawBitmap(bitmap!!, startX, startY, paint)

        /*

        // Dibujar contorno de la tabla
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        canvas.drawRect(startX, startY, startX + tableWidth, startY + rowHeight * 2, paint)

        // Columnas principales
        val col1End = startX + col1Width
        val col2End = col1End + col2Width
        val col3End = col2End + col3Width

        canvas.drawLine(col1End, startY, col1End, startY + rowHeight * 2, paint) // Línea entre col 1 y 2
        canvas.drawLine(col2End, startY, col2End, startY + rowHeight * 2, paint) // Línea entre col 2 y 3

        // Segunda columna: Divisiones internas
        val col2HalfY = startY + rowHeight
        canvas.drawLine(col1End, col2HalfY, col2End, col2HalfY, paint) // Divide la segunda columna en 2 filas
        canvas.drawLine((col1End + col2End) / 2, col2HalfY, (col1End + col2End) / 2, startY + rowHeight * 2, paint) // Divide en 2 columnas pequeñas

        // Tercera columna: Divisiones internas
        val col3HalfY = startY + rowHeight
        canvas.drawLine(col2End, col3HalfY, col3End, col3HalfY, paint) // Divide la tercera columna en 2 filas

        // Cargar imagen y colocarla en la primera columna
        val bitmap: Bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.logomini)
        val scaledBitmap: Bitmap = Bitmap.createScaledBitmap(bitmap, col1Width.toInt(), (rowHeight * 2).toInt(), false)
        canvas.drawBitmap(scaledBitmap, startX, startY, paint)

        // Texto en cada sección con dos líneas
        paint.style = Paint.Style.FILL
        paint.textSize = 12f
        paint.typeface = Typeface.DEFAULT_BOLD

        // Segunda columna - Primera fila (Titulo general)
        canvas.drawText("Código: FPRGAC-7A", col1End + 10f, startY + 25f, paint)

        // Segunda columna - Sub columnas con dos líneas de texto
        paint.textSize = 10f
        canvas.drawText("Edición", col1End + 10f, col2HalfY + 20f, paint)
        canvas.drawText("Noviembre 2024", col1End + 10f, col2HalfY + 35f, paint)

        canvas.drawText("Revisión", (col1End + col2End) / 2 + 10f, col2HalfY + 20f, paint)
        canvas.drawText("01 (Reedición)", (col1End + col2End) / 2 + 10f, col2HalfY + 35f, paint)

        // Tercera columna - Filas con dos líneas de texto
        paint.textSize = 12f
        canvas.drawText("Página 1", col2End + 10f, startY + 25f, paint)
       // canvas.drawText("Fila 1 - Línea 2", col2End + 10f, startY + 40f, paint)

        canvas.drawText("Fecha Revisión", col2End + 10f, col3HalfY + 20f, paint)
        canvas.drawText("Noviembre 2024", col2End + 10f, col3HalfY + 35f, paint)

         */

        //acaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa



        pdfDocument.finishPage(myPage)

        // below line is used to set the name of
        // our PDF file and its path.
        val root = Environment.getExternalStorageDirectory().toString()
        val fileee: File = File("$root/"+Constants.SAVE_FILE.CARPETA_GENERAL+"/"+Constants.SAVE_FILE.CARPETA_FORMATOS)
        if (!fileee.exists()) {
            fileee.mkdirs()
        }

        val file: File = File(fileee, nombreDocumento+".pdf")

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(FileOutputStream(file))

            // on below line we are displaying a toast message as PDF file generated..
            Toast.makeText(requireContext(), "PDF file generated..", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // below line is used
            // to handle error
            e.printStackTrace()

            // on below line we are displaying a toast message as fail to generate PDF
            Toast.makeText(requireContext(), "Fail to generate PDF file..", Toast.LENGTH_SHORT)
                .show()
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close()

        //    viewPdf("GFG.pdf",Environment.getExternalStorageDirectory().path)

    }


    private var zoomView: ZoomView? = null

    fun isOnline(): Boolean {
        try {
            val p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com")
            val returnVal = p1.waitFor()
            val reachable = (returnVal == 0)
            return reachable
        } catch (e: Exception) {
            //  e.printStackTrace();
        }
        return false
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHistoricoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        clickListener()
        observers()



        return root
    }

    fun initUI() {
        loading = TransparentProgressDialog(requireContext())

   //     vista = binding.llDetalleFormato
   //     mScaleGestureDetector = ScaleGestureDetector(requireContext(), ScaleListener())

        zoomView = ZoomView(requireContext())


        if (binding.llDetalleFormato.getParent() != null) {
            (binding.llDetalleFormato.getParent() as ViewGroup).removeView(binding.llDetalleFormato) // <- fix
        }


        zoomView!!.addView(binding.llDetalleFormato)

        binding.mainContainer.addView(zoomView)

        usuariosViewModel.getEmpleadosListDB()
        aeronavesViewModel.getEstacionesListDB()
        aeronavesViewModel.getModelosAeronavesListDB()
        aeronavesViewModel.getAeronavesListDB()
        formatosViewModel.getFormatosRegistroCompletedListDB()



    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            mScaleFactor *= scaleGestureDetector.scaleFactor
            mScaleFactor = max(0.1f, min(mScaleFactor, 10.0f))
            vista!!.scaleX = mScaleFactor
            vista!!.scaleY = mScaleFactor
            return true
        }
    }



    fun clickListener()
    {

        binding.llBack.setOnClickListener {

            binding.llcontenedorAtras.visibility = View.GONE
            binding.llDetalleFormato.visibility = View.GONE
            binding.llcontenedorLista.visibility = View.VISIBLE

        }


        binding.btnEnviaCorreo.setOnClickListener {

            try {
                sendPdf(formatoSelected!!.id_db!!,formatoSelected!!.codigoFormato)
            }
            catch (e:Exception)
            {
                Log.e(className,e.toString())
                showErrorDialog(e.toString())
            }


        }

    }

    fun saveModeloAeronave(context: Context, idAeronave:String, nombreAeronave:String) {
        val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES.AERONAVE, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.SHARED_PREFERENCES.ID_MODELO_AERONAVE, idAeronave)
        editor.putString(Constants.SHARED_PREFERENCES.NOMBRE_MODELO_AERONAVE, nombreAeronave)
        editor.apply()
    }


    fun saveFormato(context: Context,idFormato:String,nombreFormato:String) {
        val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES.FORMATO, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.SHARED_PREFERENCES.ID_FORMATO, idFormato)
        editor.putString(Constants.SHARED_PREFERENCES.NOMBRE_FORMATO, nombreFormato)
        editor.apply()
    }




    private fun observers()
    {

        aeronavesViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            try {
                if (it) {
                    if (!loading!!.isShowing) {
                        loading!!.show()
                    }
                } else {
                    if (loading!!.isShowing) {
                        loading!!.dismiss()
                    }
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })

        aeronavesViewModel.aeronavesState.observe(viewLifecycleOwner, Observer {
            try {
                if (it.toString().contains(Constants.ERROR.SUCCESS)) {
                } else {
                    if (it.toString().contains(Constants.ERROR.FAILURE)) {
                        Log.e(className, Constants.ERROR.ERROR)
                    }
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })


        formatosViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            try {
                if (it) {
                    if (!loading!!.isShowing) {
                        loading!!.show()
                    }
                } else {
                    if (loading!!.isShowing) {
                        loading!!.dismiss()
                    }
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })



        formatosViewModel.formatosState.observe(viewLifecycleOwner, Observer {
            try {
                if (it.toString().contains(Constants.ERROR.SUCCESS)) {
                } else {
                    if (it.toString().contains(Constants.ERROR.FAILURE)) {
                        var messageError = it.toString().replace("FAILURE","ERROR")
                        //    var messageWithoutFormat = it.toString().replace("FAILURE(message=","")
                        //    var messageFormat = messageWithoutFormat.replace(")","")
                        Log.e(className,messageError )
                        showErrorDialog(messageError)
                    }
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })



        formatosViewModel.responseGetFormatosRegistroCompletedListDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                listaFormatosRealizados = arrayListOf()
                //   var codFormato = getFormato(requireContext())

                var listaFormatosRealizadosWithoutFilter : ArrayList<FormatoRegistro> = ArrayList(it)

                for(item in listaFormatosRealizadosWithoutFilter)
                {
                    //   if(item.codigoFormato.equals(codFormato))
                    //    {
                    listaFormatosRealizados.add(item)
                    //    }
                }

                //  listaFormatosRealizados = ArrayList(it)
                setRecyclerViewFormatosRealizados(listaFormatosRealizados!!)
            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        formatosViewModel.responseGetDetalleFormatosRegistroByFormatoRegistroListDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                listaDetalleFormatoRegistro = ArrayList(it)

                pintaDocumento(formatoSelected!!,listaDetalleFormatoRegistro!!)


            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })



        aeronavesViewModel.responseGetEstacionListDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                listaEstacionesDb = ArrayList(it)

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        aeronavesViewModel.responseGetModeloAeronaveListDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                listaModelosAeronave = ArrayList(it.data)

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        aeronavesViewModel.responseGetAeronaveListDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                listaAeronaves = ArrayList(it)

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        usuariosViewModel.responseGetEmpleadoListDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                listaEmpleados = ArrayList(it)

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })



        formatosViewModel.responseResponseEnviaPdf.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                Toast.makeText(requireContext(), "Correo enviado", Toast.LENGTH_LONG).show()
            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })



    }


    fun setRecyclerViewFormatosRealizados(lista: ArrayList<FormatoRegistro>) {
        val recyclerview = binding.rvFormatosSinDiscrepancias
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ListaFormatosSinDiscrepanciasAdapter(lista)
        recyclerview.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                recyclerview.context,
                R.drawable.divider
            )!!
        )

        recyclerview.addItemDecoration(dividerItemDecoration)

        adapter.onItemClick = { formatoRegistro ->

            formatoSelected = formatoRegistro
            formatosViewModel.getDetalleFormatoRegistroByFormatoRegistroDB(formatoRegistro.id_db!!)

        }
    }


    fun pintaDocumento(formatoRegistro: FormatoRegistro,detalleFormatoRegistro:ArrayList<DetalleFormatoRegistro>)
    {

        binding.tvFormatoRTV.setText(formatoRegistro.numeroRTV)

        var nombreEstacion = ""

        for(item in listaEstacionesDb!!)
        {
            if(item.id_cloud.equals(formatoRegistro.codigoEstacion))
            {
                nombreEstacion = item.nombre!!
            }
        }
        binding.tvFormatoUBICACION.setText(nombreEstacion)


        var idModeloAeronave = ""
        var placaAeronave = ""

        for (item in listaAeronaves!!)
        {
            if(formatoRegistro.codigoPuestoTecnico.equals(item.codigoPuestoTecnico))
            {
                idModeloAeronave = item.id_cloud!!
                placaAeronave = item.placa
            }
        }


        var nombreModelo = ""
        for(item in listaModelosAeronave!!)
        {
            if(item.id_cloud.equals(idModeloAeronave))
            {
                nombreModelo = item.nombre!!

            }

        }


        binding.tvFormatoAERONAVE.setText(nombreModelo+"/"+formatoRegistro.nombreAeronave + "/" + placaAeronave)

        if(formatoRegistro.existenDiscrepancias.equals("1")) {
            binding.ivExistendiscrepanciasSi.setImageResource(R.drawable.ic_check)
            binding.ivExistendiscrepanciasNo.setImageResource(R.drawable.ic_uncheck)

        }
        else {
            binding.ivExistendiscrepanciasSi.setImageResource(R.drawable.ic_uncheck)
            binding.ivExistendiscrepanciasNo.setImageResource(R.drawable.ic_check)
        }


        binding.tvDiscrepanciasNroRTV.setText("b. Las discrepancias estan registradas en el RTV Nro "+formatoRegistro.numeroRTVDiscrepancias)



        if(formatoRegistro.accionesMantenimiento.equals("1")) {
            binding.ivAccionesmantenimientoSi.setImageResource(R.drawable.ic_check)
            binding.ivAccionesmantenimientoNo.setImageResource(R.drawable.ic_uncheck)

        }
        else {
            binding.ivAccionesmantenimientoSi.setImageResource(R.drawable.ic_uncheck)
            binding.ivAccionesmantenimientoNo.setImageResource(R.drawable.ic_check)
        }


        if(formatoRegistro.solicitaEncMotores.equals("1")) {
            binding.ivEncendidomotoresSi.setImageResource(R.drawable.ic_check)
            binding.ivEncendidomotoresNo.setImageResource(R.drawable.ic_uncheck)

        }
        else {
            binding.ivEncendidomotoresSi.setImageResource(R.drawable.ic_uncheck)
            binding.ivEncendidomotoresNo.setImageResource(R.drawable.ic_check)
        }

        val root = Environment.getExternalStorageDirectory().toString()
        val firmaResponsableee : File = File("$root/"+Constants.SAVE_FILE.CARPETA_GENERAL+"/"+Constants.SAVE_FILE.CARPETA_FIRMA,Constants.SAVE_FILE.PREFIJO_FIRMA+Constants.SAVE_FILE.PREFIJO_RESPONSABLE+formatoRegistro.id_db+".png")


        val formatoPdf : File = File("$root/"+Constants.SAVE_FILE.CARPETA_GENERAL+"/"+Constants.SAVE_FILE.CARPETA_FORMATOS,Constants.SAVE_FILE.PREFIJO_FIRMA+Constants.SAVE_FILE.PREFIJO_RESPONSABLE+formatoRegistro.id_db+".png")

        filePdf = formatoPdf




        if (firmaResponsableee.exists()) {
            val myBitmap = BitmapFactory.decodeFile(firmaResponsableee.absolutePath)
            binding.ivFirmaResponsable.setImageBitmap(myBitmap)
        }

        binding.tvFechaResponsable.setText(formatoRegistro.fechaRegistro)



        var nombreRes = ""
        var licenciaRes = ""


        var nombreCopiloto = ""
        var licenciaCopiloto = ""
        var nombrePiloto = ""
        var licenciaPiloto = ""


        for(item in listaEmpleados!!)
        {
            if(item.id_cloud.equals(formatoRegistro.idEmpleadoResponsable))
            {
                nombreRes = item.nombreCompleto!!
                licenciaRes = item.licencia!!
            }

            if(item.id_cloud.equals(formatoRegistro.idEmpleadoCoPiloto))
            {
                nombreCopiloto = item.nombreCompleto!!
                licenciaCopiloto = item.licencia!!
            }

            if(item.id_cloud.equals(formatoRegistro.idEmpleadoPiloto))
            {
                nombrePiloto = item.nombreCompleto!!
                licenciaPiloto = item.licencia!!
            }
        }

        binding.tvNombreResponsable.text = nombreRes.toString()
        binding.tvLicenciaResponsable.text = licenciaRes

        binding.tvNombreCopiloto.text = nombreCopiloto.toString()
        binding.tvLicenciaCopiloto.text = licenciaCopiloto

        binding.tvNombrePiloto.text = nombrePiloto.toString()
        binding.tvLicenciaPiloto.text = licenciaPiloto

        val firmaCopilotooo : File = File("$root/"+Constants.SAVE_FILE.CARPETA_GENERAL+"/"+Constants.SAVE_FILE.CARPETA_FIRMA,Constants.SAVE_FILE.PREFIJO_FIRMA+Constants.SAVE_FILE.PREFIJO_COPILOTO+formatoRegistro.id_db+".png")
        if (firmaCopilotooo.exists()) {
            val myBitmap = BitmapFactory.decodeFile(firmaCopilotooo.absolutePath)
            binding.ivFirmaCopiloto.setImageBitmap(myBitmap)
        }


        val firmaPilotooo : File = File("$root/"+Constants.SAVE_FILE.CARPETA_GENERAL+"/"+Constants.SAVE_FILE.CARPETA_FIRMA,Constants.SAVE_FILE.PREFIJO_FIRMA+Constants.SAVE_FILE.PREFIJO_PILOTO+formatoRegistro.id_db+".png")
        if (firmaPilotooo.exists()) {
            val myBitmap = BitmapFactory.decodeFile(firmaPilotooo.absolutePath)
            binding.ivFirmaPiloto.setImageBitmap(myBitmap)
        }


        if(detalleFormatoRegistro!=null)
        {
            if(detalleFormatoRegistro.size==0)
            {
                binding.tvSinAnotaciones.visibility = View.VISIBLE
            }
            else
            {
                binding.tvSinAnotaciones.visibility = View.GONE
                for(reportajeItem in detalleFormatoRegistro)
                {
                    newCheckBox(reportajeItem.nombreReportaje,reportajeItem.codigoReportaje,binding.llcontenedorTareas!!,reportajeItem.indicadorSN!!,reportajeItem.indicadorBloqueo!!,reportajeItem.nombreTarea!!)
                }

            }

        }



    //    showImage(fileee.path,binding.ivFirmaResponsable)

        /*
                if(formatoRegistro.existenDiscrepancias.equals("1")) {
                    binding.chbxDiscrepanciasSi.isChecked = true
                    binding.chbxDiscrepanciasNo.isChecked = false
                }
                else {
                    binding.chbxDiscrepanciasSi.isChecked = false
                    binding.chbxDiscrepanciasNo.isChecked = true
                }

                binding.tvDiscrepanciasNroRtv.setText("b. Las discrepancias surgidas estan registradas en el RTV Nro "+formatoRegistro.numeroRTVDiscrepancias)



                if(formatoRegistro.accionesMantenimiento.equals("1")) {
                    binding.chbxAccionesmantenimientoSi.isChecked = true
                    binding.chbxAccionesmantenimientoNo.isChecked = false
                }
                else {
                    binding.chbxAccionesmantenimientoSi.isChecked = false
                    binding.chbxAccionesmantenimientoNo.isChecked = true
                }


                if(formatoRegistro.solicitaEncMotores.equals("1")) {
                    binding.chbxEncendidomotoresSi.isChecked = true
                    binding.chbxEncendidomotoresNo.isChecked = false
                }
                else {
                    binding.chbxEncendidomotoresSi.isChecked = false
                    binding.chbxEncendidomotoresNo.isChecked = true
                }
        */
        binding.llcontenedorAtras.visibility = View.VISIBLE
        binding.llDetalleFormato.visibility = View.VISIBLE
        binding.llcontenedorLista.visibility = View.GONE



        var nommbreFile =Constants.SAVE_FILE.PREFIJO_FORMATO+formatoRegistro.codigoFormato+"_"+formatoRegistro.id_db
        generaFormatoPDF(nommbreFile)

    }

    fun newCheckBox(nombre:String,id:String,contenedor:LinearLayout,indicadorSN:String,indicadorBloqueo:String,nombreTarea:String)
    {

        val tituloTarea = TextView(requireContext())

        val nombreReportaje = TextView(requireContext())

      //  tituloTarea.setText("\n"+nombreTarea)
        tituloTarea.setText("- "+nombreTarea)

        nombreReportaje.setText("   "+nombre)

        tituloTarea.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        nombreReportaje.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

        val tabletSize = resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {
            tituloTarea.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.nombretarea_formatos_realizados))
            nombreReportaje.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.nombretarea_formatos_realizados))
            //     tituloTarea.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23f)
        } else {
            tituloTarea.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.nombretarea_cel_pdf))
            nombreReportaje.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.nombrereportaje_cel_pdf))
            //    tituloTarea.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        }
      //  CompoundButtonCompat.setButtonTintList(cb, ColorStateList.valueOf(getResources().getColor(R.color.titulo_pantalla_general)))
        // CompoundButtonCompat.setButtonTintList(tituloTarea, ColorStateList.valueOf(getResources().getColor(R.color.titulo_pantalla_general)))


      //  tituloTarea.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);

        //  val param = cb.layoutParams as ViewGroup.MarginLayoutParams
        //  param.setMargins(0,10,0,0)
        //  cb.layoutParams = param

        contenedor.addView(tituloTarea)
        contenedor.addView(nombreReportaje)

    }


    /*
        fun showImage(nombreImagen:String,imageView:ImageView)
        {
            Glide.with(requireContext())
                .asBitmap()
                .load(nombreImagen)
                .into(imageView)
        }

     */

    fun showErrorDialog(message: String?) {
        val bundle = Bundle()
        bundle.putString("errorMessage", message)
        val df: ErrorMessageDialog = ErrorMessageDialog()
        df.setArguments(bundle)
        df.show(requireFragmentManager(), "")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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


    fun sendPdf(idDb:String,codFormato:String)
    {
        val root = Environment.getExternalStorageDirectory().toString()
        val firmaCopilotooo : File = File("$root/"+Constants.SAVE_FILE.CARPETA_GENERAL+"/"+Constants.SAVE_FILE.CARPETA_FORMATOS,Constants.SAVE_FILE.PREFIJO_FORMATO+codFormato+"_"+idDb+".pdf")


        var uri: Uri = firmaCopilotooo.toUri()
        val fileContent: String = ConvertToString(requireContext(), uri)

        formatosViewModel.enviaPdf(fileContent,"pruebaa")
    }


    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray {
        val bufferSize = 1024
        val byteArrayOutputStream = ByteArrayOutputStream()
        val byteArray = ByteArray(bufferSize)
        var len: Int
        while ((inputStream.read(byteArray).also { len = it }) != -1) {
            byteArrayOutputStream.write(byteArray, 0, len)
        }
        return byteArrayOutputStream.toByteArray()
    }

    fun ConvertToString(context: Context, uri: Uri?): String {
        var encodedValue = ""
        try {
            val `in` = context.contentResolver.openInputStream(uri!!)
            val bytes = getBytes(`in`!!)
            encodedValue = Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return encodedValue
    }
}