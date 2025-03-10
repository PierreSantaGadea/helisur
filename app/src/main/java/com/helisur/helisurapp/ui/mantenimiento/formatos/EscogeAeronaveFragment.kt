package com.helisur.helisurapp.ui.mantenimiento.formatos

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.helisur.helisurapp.R
import com.helisur.helisurapp.databinding.FragmentEscogeAeronaveBinding
import com.helisur.helisurapp.domain.model.Formato
import com.helisur.helisurapp.domain.model.ModeloAeronave
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.ErrorMessageDialog
import com.helisur.helisurapp.domain.util.TransparentProgressDialog
import com.helisur.helisurapp.ui.mantenimiento.AeronavesViewModel
import com.helisur.helisurapp.ui.mantenimiento.formatos.postvuelo.PostVueloActivity
import com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo.PreVueloActivity
import com.helisur.helisurapp.ui.mantenimiento.formatos.spinners.SpinenrItemAeronave
import com.helisur.helisurapp.ui.mantenimiento.formatos.spinners.SpinenrItemFormato
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream


@AndroidEntryPoint
class EscogeAeronaveFragment  : Fragment() {

    var className = "ConcursosAvancesFragment"

    private var _binding: FragmentEscogeAeronaveBinding? = null
    private val binding get() = _binding!!

    private val aeronavesViewModel: AeronavesViewModel by viewModels()
    private val formatosViewModel: FormatosViewModel by viewModels()

  //  private var aeronavesList: ArrayList<ObtieneAeronavesDataTableCloudResponse>? = null
    private var aeronavesList: ArrayList<ModeloAeronave>? = null
 //   private var formatosList: ArrayList<ObtieneFormatosDataTableCloudResponse>? = null
    private var formatosList: ArrayList<Formato>? = null

    var loading: TransparentProgressDialog? = null
    private var idAeronave:String = ""
    private var nombreAeronave:String = ""
    private var idFormato:String = ""
    private var nombreFormato:String = ""


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
            return null
        }
    }

    fun genraa() {

        var pageHeight = 942
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

        val bitmap: Bitmap? = loadBitmapFromView(binding.llpruebaaaa!!)
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEscogeAeronaveBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initUI()
        clickListener()
        observers()
        genraa()
        return root
    }

    fun initUI() {
        loading = TransparentProgressDialog(requireContext())
        binding.rlAeronave!!.setBackgroundResource(R.drawable.shape_control_disabled)
        binding.rlFormato!!.setBackgroundResource(R.drawable.shape_control_disabled)
      //  aeronavesViewModel.getModeloAeronaveListCloud()
       // formatosViewModel.obtieneFormatos()
        formatosViewModel.getFormatosListDB()
        aeronavesViewModel.getModelosAeronavesListDB()
    }


    fun clickListener()
    {
        binding.btnCotinuar.setOnClickListener {

            if(idAeronave.equals(""))
            {
                showErrorDialog("Escoja aeronave")
            }
            else
            {
                if(idFormato.equals(""))
                {
                    showErrorDialog("Escoja formato")
                }
                else
                {

                   if(idFormato.equals("00001"))
                   {
                       saveModeloAeronave(requireContext(),idAeronave,nombreAeronave)
                       saveFormato(requireContext(),idFormato,nombreFormato)
                       val intent = Intent (getActivity(), PreVueloActivity::class.java)
                       getActivity()?.startActivity(intent)
                   }
                    else
                   {
                       saveModeloAeronave(requireContext(),idAeronave,nombreAeronave)
                       saveFormato(requireContext(),idFormato,nombreFormato)
                       val intent = Intent (getActivity(), PostVueloActivity::class.java)
                       getActivity()?.startActivity(intent)
                   }

                }
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


    fun setSpinnerAeronave(
    ) {
        var spinnerTipo = binding.spiAeronave
        val spinnerArray: MutableList<String> = ArrayList()
        val spinnerArrayImages: MutableList<Int> = ArrayList()
        spinnerArray.add("Seleccione modelo aeronave")
        spinnerArrayImages.add(R.drawable.empty)
    //    spinnerArray.add("Seleccione modelo")
    //    spinnerArrayImages.add(R.drawable.ic_down)

           for(item in aeronavesList!!)
           {
               var itemName = item.nombre!!
               spinnerArray.add(itemName)

               if(itemName.contains("MI"))
               {
                   spinnerArrayImages.add(R.drawable.img_mi8)
               }
               else
               {
                   if(itemName.contains("BK"))
                   {
                       spinnerArrayImages.add(R.drawable.img_bk117)
                   }
                   else
                   {
                       if(itemName.contains("BELL"))
                       {
                           spinnerArrayImages.add(R.drawable.img_bell412)
                       }
                       else
                       {
                       //    spinnerArrayImages.add(R.drawable.img_bell412)
                       }
                   }
               }
           }

        val adapter =
            SpinenrItemAeronave(
                requireContext(), 0,
                spinnerArray.toTypedArray(), spinnerArrayImages.toTypedArray()
            )
    //    val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, spinnerArray)
     //   adapter.setDropDownViewResource(R.layout.spinner_item)
        spinnerTipo.adapter = adapter

        spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {

                if (position == 0) {
                    idAeronave = ""
                    nombreAeronave = ""
                } else {

                    idAeronave = aeronavesList!![position-1].id_cloud!!
                    nombreAeronave = aeronavesList!![position-1].nombre!!
                    setSpinnerFormato(idAeronave)
                }


            }
        }
    }



    fun setSpinnerFormato(idModeloAeronave:String
    ) {
        var spinnerTipo = binding.spiFormato
        val spinnerArray: MutableList<String> = ArrayList()
        val spinnerArrayImages: MutableList<Int> = ArrayList()
        spinnerArray.add("Seleccione formato")
        spinnerArrayImages.add(R.drawable.empty)

        if(formatosList!=null)
        {
            if(formatosList!!.size>0)
            {
                for(item in formatosList!!)
                {
                    if(item.codigoModeloAeronave.equals(idModeloAeronave))
                    {
                        spinnerArray.add(item.nombreFormato!!)
                        spinnerArrayImages.add(R.drawable.ic_form)
                    }
                }
            }
        }


        if(spinnerArray.size>1)
        {
            binding.rlFormato!!.setBackgroundResource(R.drawable.shape_text_box)

         //   val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, spinnerArray)
         //   adapter.setDropDownViewResource(R.layout.spinner_item)
            val adapter =
                SpinenrItemFormato(
                    requireContext(), 0,
                    spinnerArray.toTypedArray(), spinnerArrayImages.toTypedArray()
                )

            spinnerTipo.adapter = adapter

            spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    if (position == 0) {
                        idFormato = ""
                    } else {
                        idFormato = formatosList!![position-1].id_cloud!!
                        nombreFormato = formatosList!![position-1].nombreFormato!!
                    }
                }
            }
        }
        else
        {
            binding.rlFormato!!.setBackgroundResource(R.drawable.shape_control_disabled)
        }


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

        aeronavesViewModel.responseGetModeloAeronaveListCloud.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                 //   aeronavesList = ArrayList(it.data!!.table)

                    /*
                    var modeloAeronaveEntityList: ArrayList<ModeloAeronave> = arrayListOf()
                    for(item in aeronavesList!!)
                    {
                        modeloAeronaveEntityList.add(item.toDomain())
                    }
                    aeronavesViewModel.insertModeloAeronaveListDB(modeloAeronaveEntityList)

                     */


                    binding.rlAeronave!!.setBackgroundResource(R.drawable.shape_text_box)
                    setSpinnerAeronave()
                } else {
                    Log.e(className, Constants.ERROR.ERROR)
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })


        aeronavesViewModel.responseGetModeloAeronaveListDB.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    aeronavesList = ArrayList(it.data!!)

                    /*
                    var modeloAeronaveEntityList: ArrayList<ModeloAeronave> = arrayListOf()
                    for(item in aeronavesList!!)
                    {
                        modeloAeronaveEntityList.add(item.toDomain())
                    }
                    aeronavesViewModel.insertModeloAeronaveListDB(modeloAeronaveEntityList)

                     */


                    binding.rlAeronave!!.setBackgroundResource(R.drawable.shape_text_box)
                    setSpinnerAeronave()
                } else {
                    Log.e(className, Constants.ERROR.ERROR)
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



        formatosViewModel.responseObtieneFormatos.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
               //     formatosList = ArrayList(it.data!!.table)
                    binding.rlAeronave!!.setBackgroundResource(R.drawable.shape_text_box)
                   // setSpinnerAeronave()
                } else {
                    Log.e(className, Constants.ERROR.ERROR)
                }
            } catch (e: Exception) {
                Log.e(className, Constants.ERROR.ERROR_EN_CODIGO + e.toString())
                e.printStackTrace();
                showErrorDialog(e.toString())
            }
        })


        formatosViewModel.responseGetFormatoListDB.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    formatosList = ArrayList(it)
                    binding.rlAeronave!!.setBackgroundResource(R.drawable.shape_text_box)
                    // setSpinnerAeronave()
                } else {
                    Log.e(className, Constants.ERROR.ERROR)
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
}