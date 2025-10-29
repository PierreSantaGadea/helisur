package com.helisur.helisurapp.ui.mantenimiento


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle

import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

import com.helisur.helisurapp.domain.util.BaseActivity


import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.EnumSet

@AndroidEntryPoint
class VisorPDFActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // binding = ActivityMantenimientoBinding.inflate(layoutInflater)
       // setContentView(binding.root)
      //  prefillPostVueloEditableAndOpen(this)

   //     prefillPostVueloOpen(this,true,true,true)

    //    insertarComboMovibleYMostrar(this)

    }

/*
    fun prefillPostVueloWithImageAndOpen(context: Context) {
        val config = PdfActivityConfiguration.Builder(context).build()

        CoroutineScope(Dispatchers.IO).launch {
            // 1) Copiar assets a archivos editables
            val srcPdf = "formatopostuvuelo.pdf"
            val workFile = File(context.cacheDir, "postvuelo_work.pdf")
            context.assets.open(srcPdf).use { input ->
                FileOutputStream(workFile).use { output -> input.copyTo(output) }
            }
            val workUri = Uri.fromFile(workFile)

            // 2) Abrir documento de trabajo
            val document = PdfDocumentLoader.openDocument(context, workUri)

            // ====== TEXTO (FreeText) ======
            val pageIndex = 0
            val pageSize = document.getPageSize(pageIndex) // puntos PDF (1/72")
            val left = 72f             // 1 inch desde la izquierda
            val topFromTop = 96f       // desde el borde superior "visual"
            val textWidth = 300f
            val textHeight = 24f
            val bottom = pageSize.height - topFromTop - textHeight
            val textBox = RectF(left, bottom, left + textWidth, bottom + textHeight)

            val freeText = FreeTextAnnotation(pageIndex, textBox, "Texto de ejemplo – HELISUR").apply {
                textSize = 12f
                color = Color.BLACK
            }
            document.annotationProvider.addAnnotationToPage(freeText)

            // ====== IMAGEN (StampAnnotation con Bitmap) ======
            // Carga desde assets (logo.png). Si prefieres drawable: BitmapFactory.decodeResource(context.resources, R.drawable.tu_logo)
            val bmp = context.assets.open("logo.png").use { BitmapFactory.decodeStream(it) }

            val imgLeft = 72f
            val imgTopFromTop = 160f
            val imgW = 128f
            val imgH = 64f
            val imgBottom = pageSize.height - imgTopFromTop - imgH
            val imgBox = RectF(imgLeft, imgBottom, imgLeft + imgW, imgBottom + imgH)

            val stamp = StampAnnotation(pageIndex, imgBox, bmp)
            document.annotationProvider.addAnnotationToPage(stamp)

            // 3) Guardar cambios en el documento de trabajo
            document.saveIfModified()

            // 4) Aplanar SOLO el FreeText (queda visible pero NO editable) en un nuevo archivo
            val sealedFile = File(context.cacheDir, "postvuelo_prefilled_sealed.pdf")
          /*  val task = PdfProcessorTask
                .fromDocument(document)
                .changeAnnotationsOfType(AnnotationType.FREETEXT, PdfProcessorTask.AnnotationProcessingMode.FLATTEN)
*/
            val task = PdfProcessorTask.fromDocument(document)
                .changeAnnotationsOfType(AnnotationType.FREETEXT, PdfProcessorTask.AnnotationProcessingMode.FLATTEN)
                .changeAnnotationsOfType(AnnotationType.STAMP, PdfProcessorTask.AnnotationProcessingMode.FLATTEN)

            PdfProcessor.processDocument(task, sealedFile)
            val sealedUri = Uri.fromFile(sealedFile)

            // 5) Abrir SIEMPRE con el builder (lleva los extras correctos)
            withContext(Dispatchers.Main) {
                val intent = PdfActivityIntentBuilder
                    .fromUri(context, sealedUri)
                    .configuration(config)
                    .activityClass(PdfActivity::class.java)
                    .build()
                    .apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }

                if (context is Activity) context.startActivity(intent)
                else {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            }
        }
    }
*/

    /**
     * Copia el PDF de assets -> cache, agrega texto e imagen,
     * deja el FreeText SIN aplanar (editable/movable) y abre el visor.
     */
    /*
    fun prefillPostVueloEditableAndOpen(context: Context) {
        // Config del visor: habilita edición de anotaciones
        val viewerConfig = PdfActivityConfiguration.Builder(context)
            .annotationEditingEnabled(true) // <- clave para poder mover/editar
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            // 1) Copiar el PDF de assets a un archivo editable
            val srcPdf = "formatopostuvuelo.pdf"
            val workFile = File(context.cacheDir, "postvuelo_work.pdf")
            context.assets.open(srcPdf).use { input ->
                FileOutputStream(workFile).use { output -> input.copyTo(output) }
            }
            val workUri = Uri.fromFile(workFile)

            // 2) Abrir documento
            val document = PdfDocumentLoader.openDocument(context, workUri)

            // ====== TEXTO (FreeText) ======
            val pageIndex = 0
            val pageSize = document.getPageSize(pageIndex) // puntos PDF (1/72"), origen abajo-izq
            val left = 72f
            val topFromTop = 96f
            val textWidth = 300f
            val textHeight = 24f
            val bottom = pageSize.height - topFromTop - textHeight
            val textBox = RectF(left, bottom, left + textWidth, bottom + textHeight)

            val freeText = FreeTextAnnotation(pageIndex, textBox, "Texto de ejemplo – HELISUR").apply {
                textSize = 12f
                color = Color.BLACK
            }
            document.annotationProvider.addAnnotationToPage(freeText)

            // ====== IMAGEN (Stamp) opcional, también editable/movable ======
            if (true) { // cambia a false si no quieres imagen
                val bmp = context.assets.open("logo.png").use { BitmapFactory.decodeStream(it) }
                val imgLeft = 72f
                val imgTopFromTop = 160f
                val imgW = 128f
                val imgH = 64f
                val imgBottom = pageSize.height - imgTopFromTop - imgH
                val imgBox = RectF(imgLeft, imgBottom, imgLeft + imgW, imgBottom + imgH)
                val stamp = StampAnnotation(pageIndex, imgBox, bmp)
                document.annotationProvider.addAnnotationToPage(stamp)
            }

            // 3) Guardar cambios en el documento de trabajo
            document.saveIfModified()

            // 4) Abrir SIEMPRE con el intent builder de PSPDFKit
            withContext(Dispatchers.Main) {
                val intent = PdfActivityIntentBuilder
                    .fromUri(context, workUri)
                    .configuration(viewerConfig)
                    .activityClass(PdfActivity::class.java)
                    .build()
                    .apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }

                if (context is Activity) context.startActivity(intent)
                else {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            }
        }
    }
*/







}