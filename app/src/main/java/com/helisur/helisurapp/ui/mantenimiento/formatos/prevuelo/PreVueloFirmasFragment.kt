package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo


import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.gcacace.signaturepad.views.SignaturePad
import com.helisur.helisurapp.R
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaFormatoCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaTareaCloudParameter
import com.helisur.helisurapp.databinding.FragmentFirmasBinding
import com.helisur.helisurapp.domain.model.Aeronave
import com.helisur.helisurapp.domain.model.DetalleFormatoRegistro
import com.helisur.helisurapp.domain.model.Empleado
import com.helisur.helisurapp.domain.model.Estacion
import com.helisur.helisurapp.domain.model.Formato
import com.helisur.helisurapp.domain.model.FormatoRegistro
import com.helisur.helisurapp.domain.model.ModeloAeronave
import com.helisur.helisurapp.domain.model.Reportaje
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.SessionUserManager
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.login.LoginViewModel
import com.helisur.helisurapp.ui.mantenimiento.AeronavesViewModel
import com.helisur.helisurapp.ui.mantenimiento.MainActivityMantenimiento
import com.helisur.helisurapp.ui.mantenimiento.formatos.FormatosViewModel
import com.helisur.helisurapp.ui.mantenimiento.formatos.spinners.SpinenrItemEmpleado
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.UUID


@AndroidEntryPoint
class PreVueloFirmasFragment : Fragment() {

    var className = "PreVueloFirmasFragment"
    private lateinit var binding: FragmentFirmasBinding
    var loading: TransparentProgressDialog? = null

    private val loginViewModel: LoginViewModel by viewModels()
    private val formatosViewModel: FormatosViewModel by viewModels()
    private val aeronavesViewModel: AeronavesViewModel by viewModels()

    var empleadosListAll: ArrayList<Empleado>? = null

    var piloto_copiloto = ""

    var dialogg:Dialog? = null

   // var copilotosList: ArrayList<ObtieneEmpleadosDataTableCloudResponse>? = null
   // var pilotosList: ArrayList<ObtieneEmpleadosDataTableCloudResponse>? = null


    var copilotosList: ArrayList<Empleado>? = null
    var pilotosList: ArrayList<Empleado>? = null

    var idCopiloto = ""
    var urlFirmaCopiloto = ""
    var idPiloto = ""
    var urlFirmaPiloto = ""

    var firmaPiloto : Bitmap? = null
    var firmaCopiloto : Bitmap? = null

    var idDB_nuevoFormato = ""
    var codFormato_nuevoFormato = ""

    var listaEstacionesDb: ArrayList<Estacion>? = null
    var listaModelosAeronave:ArrayList<ModeloAeronave>? = null
    var listaAeronaves:ArrayList<Aeronave>? = null
    var listaEmpleados:ArrayList<Empleado>? = null
    var listaReportajes:ArrayList<Reportaje>? = null
    var listaFormatos:ArrayList<Formato>? = null
    private var filePdf : File? = null

    var formatoAenviar:FormatoRegistro? = null
    var detalleFormatoAenviar:ArrayList<DetalleFormatoRegistro>? = null

    var idCloudNuevoFormato :String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirmasBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
     //   setCheckBox()
        clickListener()
         observers()
        signatureEvents()
        return root
    }

    fun initUI() {
        loading = TransparentProgressDialog(requireContext())
       // loginViewModel.obtieneEmpleados("00020")
        loginViewModel.getEmpleadosListDB()

        aeronavesViewModel.getEstacionesListDB()
        aeronavesViewModel.getModelosAeronavesListDB()
        aeronavesViewModel.getAeronavesListDB()
        formatosViewModel.getReportajesListDB()
        formatosViewModel.getFormatosListDB()

        if(isOnline())
        {
            binding.chbxEnviarcorreo!!.isEnabled = true
        }
        else
        {
            binding.chbxEnviarcorreo!!.isEnabled = false
        }

  //      bmp = BitmapFactory.decodeResource(resources, R.drawable.ic_form)
  //      scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false)

     //   firmapiloto = binding.signaturePadPiloto

       // var bitt = loadBitmapFromViewww(binding.btnBorrarFirmaPiloto!!)
      //  binding.signaturePadPiloto!!.signatureBitmap = bitt
      //  binding.signaturePadCopiloto!!.signatureBitmap = bitt


    }



    private fun getBitmapFromString(text: String, fontSizeSP: Float, context: Context): Bitmap {
        val fontSizePX: Int = convertDiptoPix(context, fontSizeSP)
        val pad = (fontSizePX / 9)
        val paint = Paint()

        paint.isAntiAlias = true
        paint.color = Color.WHITE
        paint.textSize = fontSizePX.toFloat()

        val textWidth = (paint.measureText(text) + pad * 2).toInt()
        val height = (fontSizePX / 0.75).toInt()
        val bitmap = Bitmap.createBitmap(textWidth, height, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(bitmap)
        val xOriginal = pad.toFloat()
        canvas.drawText(text, xOriginal, fontSizePX.toFloat(), paint)

        return bitmap
    }

    fun convertDiptoPix(context: Context, dip: Float): Int {
        val value = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            context.resources.displayMetrics
        ).toInt()
        return value
    }

    fun showErrorDialog(message: String?) {
        val bundle = Bundle()
        bundle.putString("errorMessage", message)
        val df: ErrorMessageDialog = ErrorMessageDialog()
        df.setArguments(bundle)
        df.show(requireFragmentManager(), "")
    }

    var responsableFirmoPiloto = false
    var responsableFirmoCoPiloto = false
    fun signatureEvents()
    {

        binding.signaturePadPiloto!!.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                //Event triggered when the pad is touched
                responsableFirmoPiloto = true
            }

            override fun onSigned() {
                //Event triggered when the pad is signed
            }

            override fun onClear() {
                //Event triggered when the pad is cleared
            }
        })

        binding.signaturePadCopiloto!!.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                //Event triggered when the pad is touched
                responsableFirmoCoPiloto = true
            }

            override fun onSigned() {
                //Event triggered when the pad is signed
            }

            override fun onClear() {
                //Event triggered when the pad is cleared
            }
        })
    }



    fun clickListener() {

        binding.tvAtras.setOnClickListener {

     //       genraa()
     //       pruebaPDF()
            PreVueloTabsFragment.viewPager.setCurrentItem(Constants.TABS_PRE_VUELO.ENTREGA_OPERACIONES)
        }


        binding.btnGuardarFirmaCopiloto!!.setOnClickListener{



            if(idCopiloto.equals(""))
            {
                showErrorDialog("Seleccione un copiloto")
            }
            else
            {
                if(responsableFirmoCoPiloto)
                {
                    piloto_copiloto = "COPILOTO"
                    showDialogLogin()
                }
                else
                {
                    showErrorDialog("Firme por favor")
                }
            }



        }


        binding.btnBorrarFirmaCopiloto!!.setOnClickListener{
            showDialogBorrarFirmaCoPiloto()
        }


        binding.btnGuardarFirmaPiloto!!.setOnClickListener{


            if(idPiloto.equals(""))
            {
                showErrorDialog("Seleccione un piloto")
            }
            else
            {
                if(responsableFirmoPiloto)
                {
                    piloto_copiloto = "PILOTO"
                    showDialogLogin()
                }
                else
                {
                    showErrorDialog("Firme por favor")
                }
            }


        }


        binding.btnBorrarFirmaPiloto!!.setOnClickListener{
            showDialogBorrarFirmaPiloto()
        }


        binding.btnGuardarTodo!!.setOnClickListener{
            if(validaciones())
            {
                saveFormatoLocalDabaBase()
            }

            //formatosViewModel.grabaFormato(TabsPreVuelo.formatoParameter)
        }

    }


    fun saveFormatoLocalDabaBase()
    {
        var parameter: GuardaFormatoCloudParameter = PreVueloTabsFragment.formatoParameter
        var nombreAeronave:String = getNombreAeronave(requireContext())!!
        val uniqueID: String = UUID.randomUUID().toString()

        idDB_nuevoFormato = uniqueID
        codFormato_nuevoFormato = PreVueloTabsFragment.formatoParameter.codigoFormato

        var completado:Boolean = false

        if(parameter.listaTareas!=null) {
            if(ArrayList(parameter.listaTareas).size == 0) {
                completado = true
            }
            else {
                completado = false
            }
        }
        else {
            completado = true
        }

        var fechaHoy: String = ""
        var fechaHoyCloud:String = ""
        val gc: GregorianCalendar = GregorianCalendar()
        val pattern = "yyyy-MM-dd HH:mm:ss"
        val pattern2 = "yyyyMMdd HH:mm:ss"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val simpleDateFormat2 = SimpleDateFormat(pattern2)
        simpleDateFormat.calendar = gc
        simpleDateFormat2.calendar = gc
        fechaHoy = simpleDateFormat.format(gc.time)
        fechaHoyCloud = simpleDateFormat2.format(gc.time)

        PreVueloTabsFragment.formatoParameter.fechaHoraFinRegistro = fechaHoyCloud
        PreVueloTabsFragment.formatoParameter.usuarioRegistro = SessionUserManager(requireContext()).getId()!!

        saveBitmapOnLocalStorage(Constants.SAVE_FILE.PREFIJO_FIRMA+Constants.SAVE_FILE.PREFIJO_RESPONSABLE+uniqueID,
            PreVueloTabsFragment.firmaResponsable)
        saveBitmapOnLocalStorage(Constants.SAVE_FILE.PREFIJO_FIRMA+Constants.SAVE_FILE.PREFIJO_PILOTO+uniqueID,firmaPiloto!!)
        saveBitmapOnLocalStorage(Constants.SAVE_FILE.PREFIJO_FIRMA+Constants.SAVE_FILE.PREFIJO_COPILOTO+uniqueID,firmaCopiloto!!)



        var formatoRegistro: FormatoRegistro = FormatoRegistro(uniqueID,"",parameter.codigoFormato,nombreAeronave,parameter.codigoPuestoTecnico,parameter.numeroRTV,
            parameter.codigoEstacion,parameter.existenDiscrepancias,parameter.numeroRTVDiscrepancias,parameter.accionesMantenimiento,
            parameter.solicitaEncMotores,parameter.idEmpleadoResponsable,parameter.urlFirmaResponsable,parameter.idEmpleadoPiloto,
            parameter.urlFirmaPiloto,parameter.idEmpleadoCoPiloto,parameter.urlFirmaCoPiloto,parameter.fechaHoraInicioRegistro,
            parameter.fechaHoraFinRegistro,parameter.usuarioRegistro,fechaHoy,"",completado)


        var listaDetalleDB:ArrayList<DetalleFormatoRegistro> = ArrayList()
        if(parameter.listaTareas!=null)
        {
            var listaDetalle:ArrayList<GuardaTareaCloudParameter> = ArrayList(PreVueloTabsFragment.formatoParameter.listaTareas)

            for(item in listaDetalle)
            {

                var nombreReportaje = ""
                for(itemRepo in listaReportajes!!)
                {
                    if(item.codigoReportaje.equals(itemRepo.id_cloud))
                    {
                        nombreReportaje = itemRepo.nombreReportaje
                    }
                }

                val uniqueIDDetalle: String = UUID.randomUUID().toString()
                var detalle:DetalleFormatoRegistro = DetalleFormatoRegistro(uniqueIDDetalle,"",uniqueID,item.codigoRegistroFormato,item.codigoTarea,item.nombreTarea,item.codigoReportaje,
                    nombreReportaje,item.indicadorSN,"",fechaHoy,"")

                listaDetalleDB.add(detalle)
            }
        }

        formatoAenviar = formatoRegistro
        detalleFormatoAenviar = listaDetalleDB


        formatosViewModel.insertFormatoRegistroDB(formatoRegistro)
        formatosViewModel.insertDetalleFormatoRegistroDB(listaDetalleDB)

    }

    fun String.titlecaseFirstChar() = replaceFirstChar(Char::titlecase)


    fun pintaDocumento(formatoRegistro: FormatoRegistro,detalleFormatoRegistro:ArrayList<DetalleFormatoRegistro>)
    {

        val cal: Calendar = Calendar.getInstance()
        val month_date: SimpleDateFormat = SimpleDateFormat("MMMM")
        val year_date: SimpleDateFormat = SimpleDateFormat("yyyy")

        val month_name: String = month_date.format(cal.getTime()).titlecaseFirstChar()
        val year_name: String = year_date.format(cal.getTime()).titlecaseFirstChar()

        binding.tvMesEdicion!!.setText(month_name + " "+ year_name)
        binding.tvMesRevision!!.setText(month_name + " "+ year_name)


        binding.tvFormatoRTV!!.setText(formatoRegistro.numeroRTV)

        var nombreEstacion = ""

        for(item in listaEstacionesDb!!)
        {
            if(item.id_cloud.equals(formatoRegistro.codigoEstacion))
            {
                nombreEstacion = item.nombre!!
            }
        }
        binding.tvFormatoUBICACION!!.setText(nombreEstacion)


        var idModeloAeronave = ""
        var placaAeronave = ""

        for (item in listaAeronaves!!)
        {
            if(formatoRegistro.codigoPuestoTecnico.equals(item.codigoModeloPuesto))
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

        var nombreFormato = ""

        for(item in listaFormatos!!)
        {
            if(item.id_cloud.equals(formatoRegistro.codigoFormato))
            {
                nombreFormato = item.nombreFormato!!

            }

        }

        binding.tvTituloFormatoPdf!!.setText("FORMATO DE " +nombreFormato.toUpperCase()+ " PARA AERONAVE "+nombreModelo)

        binding.tvFormatoAERONAVE!!.setText(nombreModelo+"/"+formatoRegistro.nombreAeronave + "/" + placaAeronave)

        if(formatoRegistro.existenDiscrepancias.equals("1")) {
            binding.ivExistendiscrepanciasSi!!.setImageResource(R.drawable.ic_check)
            binding.ivExistendiscrepanciasNo!!.setImageResource(R.drawable.ic_uncheck)

        }
        else {
            binding.ivExistendiscrepanciasSi!!.setImageResource(R.drawable.ic_uncheck)
            binding.ivExistendiscrepanciasNo!!.setImageResource(R.drawable.ic_check)
        }


        binding.tvDiscrepanciasNroRTV!!.setText("b. Las discrepancias estan registradas en el RTV Nro "+formatoRegistro.numeroRTVDiscrepancias)



        if(formatoRegistro.accionesMantenimiento.equals("1")) {
            binding.ivAccionesmantenimientoSi!!.setImageResource(R.drawable.ic_check)
            binding.ivAccionesmantenimientoNo!!.setImageResource(R.drawable.ic_uncheck)

        }
        else {
            binding.ivAccionesmantenimientoSi!!.setImageResource(R.drawable.ic_uncheck)
            binding.ivAccionesmantenimientoNo!!.setImageResource(R.drawable.ic_check)
        }


        if(formatoRegistro.solicitaEncMotores.equals("1")) {
            binding.ivEncendidomotoresSi!!.setImageResource(R.drawable.ic_check)
            binding.ivEncendidomotoresNo!!.setImageResource(R.drawable.ic_uncheck)

        }
        else {
            binding.ivEncendidomotoresSi!!.setImageResource(R.drawable.ic_uncheck)
            binding.ivEncendidomotoresNo!!.setImageResource(R.drawable.ic_check)
        }

        val root = Environment.getExternalStorageDirectory().toString()
        val firmaResponsableee : File = File("$root/"+Constants.SAVE_FILE.CARPETA_GENERAL+"/"+Constants.SAVE_FILE.CARPETA_FIRMA,Constants.SAVE_FILE.PREFIJO_FIRMA+Constants.SAVE_FILE.PREFIJO_RESPONSABLE+formatoRegistro.id_db+".png")


        val formatoPdf : File = File("$root/"+Constants.SAVE_FILE.CARPETA_GENERAL+"/"+Constants.SAVE_FILE.CARPETA_FORMATOS,Constants.SAVE_FILE.PREFIJO_FIRMA+Constants.SAVE_FILE.PREFIJO_RESPONSABLE+formatoRegistro.id_db+".png")

        filePdf = formatoPdf




        if (firmaResponsableee.exists()) {
            val myBitmap = BitmapFactory.decodeFile(firmaResponsableee.absolutePath)
            binding.ivFirmaResponsable!!.setImageBitmap(myBitmap)
        }

        binding.tvFechaResponsable!!.setText(formatoRegistro.fechaRegistro)



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

        binding.tvNombreResponsable!!.text = nombreRes.toString()
        binding.tvLicenciaResponsable!!.text = licenciaRes

        binding.tvNombreCopiloto!!.text = nombreCopiloto.toString()
        binding.tvLicenciaCopiloto!!.text = licenciaCopiloto

        binding.tvNombrePiloto!!.text = nombrePiloto.toString()
        binding.tvLicenciaPiloto!!.text = licenciaPiloto

        val firmaCopilotooo : File = File("$root/"+Constants.SAVE_FILE.CARPETA_GENERAL+"/"+Constants.SAVE_FILE.CARPETA_FIRMA,Constants.SAVE_FILE.PREFIJO_FIRMA+Constants.SAVE_FILE.PREFIJO_COPILOTO+formatoRegistro.id_db+".png")
        if (firmaCopilotooo.exists()) {
            val myBitmap = BitmapFactory.decodeFile(firmaCopilotooo.absolutePath)
            binding.ivFirmaCopiloto!!.setImageBitmap(myBitmap)
        }


        val firmaPilotooo : File = File("$root/"+Constants.SAVE_FILE.CARPETA_GENERAL+"/"+Constants.SAVE_FILE.CARPETA_FIRMA,Constants.SAVE_FILE.PREFIJO_FIRMA+Constants.SAVE_FILE.PREFIJO_PILOTO+formatoRegistro.id_db+".png")
        if (firmaPilotooo.exists()) {
            val myBitmap = BitmapFactory.decodeFile(firmaPilotooo.absolutePath)
            binding.ivFirmaPiloto!!.setImageBitmap(myBitmap)
        }


        if(detalleFormatoRegistro!=null)
        {
            if(detalleFormatoRegistro.size==0)
            {
                binding.tvSinAnotaciones!!.visibility = View.VISIBLE
            }
            else
            {
                binding.tvSinAnotaciones!!.visibility = View.GONE
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

        var nommbreFile =Constants.SAVE_FILE.PREFIJO_FORMATO+formatoRegistro.codigoFormato+"_"+formatoRegistro.id_db
        generaFormatoPDF(nommbreFile)

    }


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
            sendPdf(idDB_nuevoFormato,codFormato_nuevoFormato)
        } catch (e: Exception) {
            // below line is used
            // to handle error
            e.printStackTrace()
            Toast.makeText(requireContext(), "Fail to generate PDF file..", Toast.LENGTH_SHORT).show()

            // on below line we are displaying a toast message as fail to generate PDF

        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close()

        //    viewPdf("GFG.pdf",Environment.getExternalStorageDirectory().path)

    }

    fun newCheckBox(nombre:String, id:String, contenedor: LinearLayout, indicadorSN:String, indicadorBloqueo:String, nombreTarea:String)
    {

        val tituloTarea = TextView(requireContext())

        val nombreReportaje = TextView(requireContext())

        //  tituloTarea.setText("\n"+nombreTarea)
        tituloTarea.setText("- "+nombreTarea)

        nombreReportaje.setText("   "+nombre)

        tituloTarea.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        nombreReportaje.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey))

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


    fun saveBitmapOnLocalStorage(nombreDocumento:String,bitmap: Bitmap) {

        val root = Environment.getExternalStorageDirectory().toString()
        val fileee: File = File("$root/"+Constants.SAVE_FILE.CARPETA_GENERAL+"/"+Constants.SAVE_FILE.CARPETA_FIRMA)
        if (!fileee.exists()) {
            fileee.mkdirs()
        }

        val file: File = File(fileee, nombreDocumento+".png")

        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
        out.flush()
        out.close()

    }

    fun getNombreAeronave(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES.AERONAVE, MODE_PRIVATE)
        val text = sharedPreferences.getString(Constants.SHARED_PREFERENCES.NOMBRE_AERONAVE, "")
        return text
    }

    fun validaciones():Boolean
    {
        var isOk = true


        return  isOk
    }



    fun sendPdf(idDb:String,codFormato:String)
    {
        val root = Environment.getExternalStorageDirectory().toString()
        val firmaCopilotooo : File = File("$root/"+Constants.SAVE_FILE.CARPETA_GENERAL+"/"+Constants.SAVE_FILE.CARPETA_FORMATOS,Constants.SAVE_FILE.PREFIJO_FORMATO+codFormato+"_"+idDb+".pdf")

        var uri:Uri = firmaCopilotooo.toUri()
        val fileContent: String = ConvertToString(requireContext(), uri)


        formatosViewModel.enviaPdf(fileContent,idCloudNuevoFormato+".pdf")
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

    fun observers()
    {

        loginViewModel.responseObtieneTokenCloud.observe(viewLifecycleOwner, Observer {
            try {

                if(piloto_copiloto.equals("PILOTO"))
                {
                    binding.signaturePadPiloto!!.isEnabled = false
                }
                else
                {
                    binding.signaturePadCopiloto!!.isEnabled = false
                }

                dialogg!!.dismiss()



            } catch (e: Exception) {
                //  Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })



        loginViewModel.responseObtieneEmpleados.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                //    copilotosList = ArrayList(it)
                //    pilotosList = ArrayList(it)
                    setSpinnerCopilotos()
                    setSpinnerPilotos()
                } else {
                    Log.e(className, Constants.ERROR.ERROR)
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })


        loginViewModel.responseGetEmpleadoListDB.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    listaEmpleados = ArrayList(it)
                    copilotosList = arrayListOf()
                    pilotosList = arrayListOf()
                    var empleadosListaTotal: ArrayList<Empleado>? = ArrayList(it)
                    empleadosListAll=ArrayList(it)

                    for(item in empleadosListaTotal!!)
                    {
                        if(item.codigoArea.equals("00020"))
                        {
                            copilotosList!!.add(item)
                            pilotosList!!.add(item)
                        }

                    }
                    setSpinnerCopilotos()
                    setSpinnerPilotos()
                } else {
                    Log.e(className, Constants.ERROR.ERROR)
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
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

        formatosViewModel.responseGetFormatoListDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                listaFormatos = ArrayList(it)

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

        formatosViewModel.responseGetReportajeListDB.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                listaReportajes = ArrayList(it)

            } else {
                Log.e(className, Constants.ERROR.ERROR)

            }
        })


        formatosViewModel.formatosState.observe(viewLifecycleOwner, Observer {
            try {
                if (it.toString().contains(Constants.ERROR.SUCCESS)) {
                } else {
                    if (it.toString().contains(Constants.ERROR.FAILURE)) {
                        Log.e(className,it.toString())
                        var messageError = it.toString().replace("FAILURE(Error=","")
                        showErrorDialog(messageError.replace(")",""))
                    }
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })


        formatosViewModel.responseGrabaFormato.observe(viewLifecycleOwner, Observer {
            try {

                idCloudNuevoFormato = it.message

                formatosViewModel.updateIdCloudFormatoRefgistro(idDB_nuevoFormato,idCloudNuevoFormato)


                if(binding.chbxEnviarcorreo!!.isChecked)
                {
                    pintaDocumento(formatoAenviar!!,detalleFormatoAenviar!!)
                 //
                }
                else
                {
                    //grabacion correcta
                    requireActivity().finish()

                    val intent = Intent (getActivity(), MainActivityMantenimiento::class.java)
                    requireActivity().startActivity(intent)
                }




            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })

        formatosViewModel.responseResponseEnviaPdf.observe(viewLifecycleOwner, Observer {
            try {

                    //grabacion correcta
                    requireActivity().finish()

                    val intent = Intent (getActivity(), MainActivityMantenimiento::class.java)
                    requireActivity().startActivity(intent)


            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })


        formatosViewModel.responsInsertFormatoRegistroDB.observe(viewLifecycleOwner, Observer {
            try {

                if(isOnline())
                {
                    formatosViewModel.grabaFormato(PreVueloTabsFragment.formatoParameter)
                }
                else
                {
                    //grabacion correcta
                    requireActivity().finish()

                    val intent = Intent (getActivity(), MainActivityMantenimiento::class.java)
                    requireActivity().startActivity(intent)
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





    }

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



    fun setSpinnerCopilotos(
    ) {
        var spinnerTipo = binding.spiCopilotos
        val spinnerArray: MutableList<String> = ArrayList()
        val spinnerArrayImages: MutableList<Int> = ArrayList()
        spinnerArray.add("Seleccione copiloto")
        spinnerArrayImages.add(R.drawable.empty)
        for(item in copilotosList!!)
        {
            spinnerArray.add(item.nombreCompleto!!)
            spinnerArrayImages.add(R.drawable.ic_user)
        }

        val adapter =
            SpinenrItemEmpleado(
                requireContext(), 0,
                spinnerArray.toTypedArray(), spinnerArrayImages.toTypedArray()
            )

        //   val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, spinnerArray)
     //   adapter.setDropDownViewResource(R.layout.spinner_item)
        spinnerTipo!!.adapter = adapter

        spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                if (position == 0) {
                    idCopiloto = ""
                    binding.etLicenciaCopiloto!!.setText("")
                } else {
                    idCopiloto  =  copilotosList!![position-1].id_cloud!!
                    PreVueloTabsFragment.formatoParameter.idEmpleadoCoPiloto = idCopiloto
                    PreVueloTabsFragment.formatoParameter.urlFirmaCoPiloto = urlFirmaCopiloto
                    binding.etLicenciaCopiloto!!.setText(copilotosList!![position-1].licencia)


                }
            }
        }
    }



    fun setSpinnerPilotos(
    ) {
        var spinnerTipo = binding.spiPilotos
        val spinnerArray: MutableList<String> = ArrayList()
        val spinnerArrayImages: MutableList<Int> = ArrayList()
        spinnerArray.add("Seleccione piloto")
        spinnerArrayImages.add(R.drawable.empty)
        for(item in pilotosList!!)
        {
            spinnerArray.add(item.nombreCompleto!!)
            spinnerArrayImages.add(R.drawable.ic_user)
        }

     //   val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, spinnerArray)
      //  adapter.setDropDownViewResource(R.layout.spinner_item)
        val adapter =
            SpinenrItemEmpleado(
                requireContext(), 0,
                spinnerArray.toTypedArray(), spinnerArrayImages.toTypedArray()
            )
        spinnerTipo!!.adapter = adapter

        spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                if (position == 0) {
                    idPiloto = ""
                    binding.etLicenciaPiloto!!.setText("")
                } else {
                    idPiloto  =  pilotosList!![position-1].id_cloud!!
                    PreVueloTabsFragment.formatoParameter.idEmpleadoPiloto = idPiloto
                    PreVueloTabsFragment.formatoParameter.urlFirmaPiloto = urlFirmaPiloto

                    binding.etLicenciaPiloto!!.setText(pilotosList!![position-1].licencia)

                }
            }
        }
    }

    var firmapiloto:SignaturePad? = null

    companion object {

        fun destruye(){

        }
    }
/*
    override fun onDestroyView() {
        super.onDestroyView()
/*
        try {
            var bise= ""
        }
        catch (e:Exception)
        {
            var bise= ""
        }

 */

        /*
        // Example: If you're creating a Bitmap from 'myView'
        binding.signaturePadPiloto!!.post {
            // This code will run after the View has been measured and laid out
            if (binding.signaturePadPiloto!!.width > 0 && binding.signaturePadPiloto!!.height > 0) {
                val bitmap: Bitmap? = loadBitmapFromView(binding.signaturePadPiloto!!)
                // ... do something with the bitmap ...
            }
        }

        binding.signaturePadCopiloto!!.post {
            // This code will run after the View has been measured and laid out
            if (binding.signaturePadCopiloto!!.width > 0 && binding.signaturePadCopiloto!!.height > 0) {
                val bitmap: Bitmap? = loadBitmapFromView(binding.signaturePadCopiloto!!)
                // ... do something with the bitmap ...
            }
        }

         */



    }


 */

    /*
    private fun loadBitmapFromView(view: View): Bitmap? {
        // Check if the view is valid and has dimensions
        if (view.width <= 0 || view.height <= 0) {
            return null
        }
        // ... your code to create a Bitmap from the View ...
        return null
    }

     */



    override fun onResume() {
        super.onResume()
        var prueba = ""
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {

            PreVueloTabsFragment.formatoParameter.usuarioRegistro = PreVueloTabsFragment.idUsuario

        } else {
        }
    }



    private fun showDialogLogin() {
        val dialog = Dialog(requireContext())
        dialogg = dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_credentials)
        dialog.getWindow()!!.getAttributes().windowAnimations = R.style.DialogAnimation

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.attributes.alpha = 1f
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val etUsuario = dialog.findViewById(R.id.etUsuario) as EditText
        val etPass = dialog.findViewById(R.id.etPass) as EditText

        val yesBtn = dialog.findViewById(R.id.btnSi) as RelativeLayout
        yesBtn.setOnClickListener {

            var user = etUsuario.text.toString().trim()
            var pass = etPass.text.toString().trim()

            var userExist = false

            for(empleado in empleadosListAll!!)
            {
                var empleadoWithoutDomain = empleado.email!!.replace("@helisur.com.pe","")
                if(empleado.email!!.contains(user))
                {
                    if(pass.equals(empleado.numeroDocumento))
                    {
                        if(piloto_copiloto.equals("PILOTO"))
                        {
                            if(empleado.id_cloud.equals(idPiloto))
                            {
                                userExist = true
                                binding.signaturePadPiloto!!.isEnabled = false
                                binding.llFirmaValidadaPiloto!!.visibility = View.VISIBLE

                                firmaPiloto = binding.signaturePadPiloto!!.transparentSignatureBitmap
                            }
                        }
                        else
                        {
                            if(empleado.id_cloud.equals(idCopiloto))
                            {
                                userExist = true
                                binding.signaturePadCopiloto!!.isEnabled = false
                                binding.llFirmaValidadaCopiloto!!.visibility = View.VISIBLE

                                firmaCopiloto = binding.signaturePadCopiloto!!.transparentSignatureBitmap
                            }
                        }
                    }
                }
            }
            if(userExist)
            {
                dialog.dismiss()
            }
            else
            {
                showErrorDialog("Usuario inválido")

                if(piloto_copiloto.equals("PILOTO"))
                {
                    binding.llFirmaValidadaPiloto!!.visibility = View.GONE
                }
                else
                {
                    binding.llFirmaValidadaCopiloto!!.visibility = View.GONE
                }

            }

        }

        val noBtn = dialog.findViewById(R.id.btnNo) as RelativeLayout
        noBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


    private fun showDialogBorrarFirmaPiloto() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_borrarfirma)
        dialog.getWindow()!!.getAttributes().windowAnimations = R.style.DialogAnimation

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.attributes.alpha = 1f
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }


        val yesBtn = dialog.findViewById(R.id.btnSi) as RelativeLayout
        yesBtn.setOnClickListener {

            binding.signaturePadPiloto!!.isEnabled = true
            binding.signaturePadPiloto!!.clear()
            binding.llFirmaValidadaPiloto!!.visibility = View.GONE
            responsableFirmoPiloto = false
            dialog.dismiss()

        }

        val noBtn = dialog.findViewById(R.id.btnNo) as RelativeLayout
        noBtn.setOnClickListener {

            dialog.dismiss()

        }
        dialog.show()
    }



    private fun showDialogBorrarFirmaCoPiloto() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_borrarfirma)
        dialog.getWindow()!!.getAttributes().windowAnimations = R.style.DialogAnimation

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.attributes.alpha = 1f
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }


        val yesBtn = dialog.findViewById(R.id.btnSi) as RelativeLayout
        yesBtn.setOnClickListener {

            binding.signaturePadCopiloto!!.isEnabled = true
            binding.signaturePadCopiloto!!.clear()
            binding.llFirmaValidadaCopiloto!!.visibility = View.GONE
            responsableFirmoCoPiloto = false
            dialog.dismiss()

        }

        val noBtn = dialog.findViewById(R.id.btnNo) as RelativeLayout
        noBtn.setOnClickListener {

            dialog.dismiss()

        }
        dialog.show()
    }




    var pageHeight = 1950
    var pageWidth = 635

    // creating a bitmap variable
    // for storing our images
 //   lateinit var bmp: Bitmap
  //  lateinit var scaledbmp: Bitmap

    // on below line we are creating a
    // constant code for runtime permissions.
    var PERMISSION_CODE = 101


    fun checkPermissions(): Boolean {
        // on below line we are creating a variable for both of our permissions.

        // on below line we are creating a variable for
        // writing to external storage permission
        var writeStoragePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            WRITE_EXTERNAL_STORAGE
        )

        // on below line we are creating a variable
        // for reading external storage permission
        var readStoragePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            READ_EXTERNAL_STORAGE
        )

        // on below line we are returning true if both the
        // permissions are granted and returning false
        // if permissions are not granted.
        return writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission() {

        // on below line we are requesting read and write to
        // storage permission for our application.
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), PERMISSION_CODE
        )
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // on below line we are checking if the
        // request code is equal to permission code.
        if (requestCode == PERMISSION_CODE) {

            // on below line we are checking if result size is > 0
            if (grantResults.size > 0) {

                // on below line we are checking
                // if both the permissions are granted.
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]
                    == PackageManager.PERMISSION_GRANTED) {

                    var noses = ""
                    // if permissions are granted we are displaying a toast message.
                  //  Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()

                } else {
                    var noses = ""
                    // if permissions are not granted we are
                    // displaying a toast message as permission denied.
                  //  Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show()
                    //finish()
                }
            }
        }
    }




    fun genraa()
    {

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
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

        // below line is used for setting
        // start page for our PDF file.
        var myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        // creating a variable for canvas
        // from our page of PDF.
        var canvas: Canvas = myPage.canvas


        val bitmap: Bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.logo_helisur_color)
        val scaledBitmap: Bitmap = Bitmap.createScaledBitmap(bitmap, 120, 50, false)
        canvas.drawBitmap(scaledBitmap, 20f, 20f, paint)

        // Dibujar líneas para la tabla
        paint.strokeWidth = 2f
        paint.style = Paint.Style.STROKE
        canvas.drawRect(150f, 20f, 570f, 80f, paint) // Cuadro principal

        // Líneas internas
        canvas.drawLine(300f, 20f, 300f, 80f, paint) // Separación primera columna
        canvas.drawLine(450f, 20f, 450f, 80f, paint) // Separación segunda columna

        // Texto dentro de la tabla
        paint.style = Paint.Style.FILL
        paint.textSize = 12f
        paint.typeface = Typeface.DEFAULT_BOLD

        canvas.drawText("Código: FPRGAC-7A", 160f, 40f, paint)
        canvas.drawText("Página 1", 460f, 40f, paint)

        paint.textSize = 10f
        paint.typeface = Typeface.DEFAULT
        canvas.drawText("Edición:", 160f, 60f, paint)
        canvas.drawText("Noviembre 2024", 210f, 60f, paint)
        canvas.drawText("Revisión:", 310f, 60f, paint)
        canvas.drawText("01 (Reedición)", 370f, 60f, paint)
        canvas.drawText("Fecha de Revisión:", 460f, 60f, paint)
        canvas.drawText("Noviembre 2024", 560f, 60f, paint)

        // Dibujar la barra negra con el título
        paint.color = android.graphics.Color.BLACK
        canvas.drawRect(20f, 90f, 570f, 110f, paint)

        paint.color = android.graphics.Color.WHITE
        paint.textSize = 14f
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText("FORMATO DE PRE-VUELO PARA AERONAVE MBB-BK117 B-2", 30f, 105f, paint)




        pdfDocument.finishPage(myPage)

        // below line is used to set the name of
        // our PDF file and its path.
        val file: File = File(Environment.getExternalStorageDirectory(), "GFG.pdf")

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


    fun generatePDF() {
        // creating an object variable
        // for our PDF document.
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
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

        // below line is used for setting
        // start page for our PDF file.
        var myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        // creating a variable for canvas
        // from our page of PDF.
        var canvas: Canvas = myPage.canvas

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
       // canvas.drawBitmap(scaledbmp, 56F, 40F, paint)

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.textSize = 15F

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(requireContext(), R.color.purple_200))

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText("A portal for IT professionals.", 209F, 100F, title)
        canvas.drawText("Geeks for Geeks", 209F, 80F, title)
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
        title.setColor(ContextCompat.getColor(requireContext(), R.color.purple_200))
        title.textSize = 15F

        // below line is used for setting
        // our text to center of PDF.
        title.textAlign = Paint.Align.CENTER
        canvas.drawText("This is sample document which we have created.", 396F, 560F, title)

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage)

        // below line is used to set the name of
        // our PDF file and its path.
        val file: File = File(Environment.getExternalStorageDirectory(), "GFG.pdf")

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


    private fun viewPdf(file: String, directory: String) {
        val pdfFile = File(
            Environment.getExternalStorageDirectory().toString() + "/" + directory + "/" + file
        )
        val path = Uri.fromFile(pdfFile)

        // Setting the intent for pdf reader
        val pdfIntent = Intent(Intent.ACTION_VIEW)
        pdfIntent.setDataAndType(path, "application/pdf")
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        try {
            startActivity(pdfIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireActivity(),
                "No application has been found to open PDF files.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}