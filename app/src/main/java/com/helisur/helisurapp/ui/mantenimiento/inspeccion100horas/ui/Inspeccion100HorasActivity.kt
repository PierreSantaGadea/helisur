package com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.navigation.ui.AppBarConfiguration
import com.helisur.helisurapp.R
import com.helisur.helisurapp.databinding.ActivityPrevueloBinding
import com.helisur.helisurapp.domain.util.BaseActivity
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.TextoPDF
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.conscrypt.Conscrypt
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.security.Security
import java.util.Locale
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class Inspeccion100HorasActivity  : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityPrevueloBinding

    private var textToSpeech: TextToSpeech? = null
    private var isSpeaking = false
    private var currentMicrophoneButton: ImageView? = null
    
    // CoroutineScope para manejar corrutinas
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

 
    var tvMessage: TextView? = null
    var btnMicrophone: ImageView? = null
    var loaderContainer: LinearLayout? = null
    var etQuestion: EditText? = null
    var btnSend: ImageView? = null
    var btnClose: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrevueloBinding.inflate(layoutInflater)
        Security.insertProviderAt(Conscrypt.newProvider(), 1)
        setContentView(binding.root)
        // Inicializar TextToSpeech
        initializeTextToSpeech()
        loadTabHomeFragment()
        binding.fabButtonIA.setOnClickListener {
            showDialog()
        }
    }

    /**
     * Inicializa TextToSpeech
     */
    private fun initializeTextToSpeech() {
        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale("es", "ES"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Idioma no soportado")
                } else {
                    Log.d("TTS", "TextToSpeech inicializado correctamente")
                }
            } else {
                Log.e("TTS", "Error al inicializar TextToSpeech")
            }
        }
        
        // Configurar listener para detectar cuando termina la reproducción
        textToSpeech?.setOnUtteranceProgressListener(object : android.speech.tts.UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                Log.d("TTS", "Iniciando reproducción")
            }
            
            override fun onDone(utteranceId: String?) {
                Log.d("TTS", "Reproducción terminada")
                runOnUiThread {
                    isSpeaking = false
                    // Restaurar el icono del micrófono
                    currentMicrophoneButton?.let { button ->
                        setMicrophoneIcon(button, false)
                    }
                }
            }
            
            override fun onError(utteranceId: String?) {
                Log.e("TTS", "Error en reproducción")
                runOnUiThread {
                    isSpeaking = false
                    // Restaurar el icono del micrófono en caso de error
                    currentMicrophoneButton?.let { button ->
                        setMicrophoneIcon(button, false)
                    }
                }
            }
        })
    }

    fun initTalk() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
     //   intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ru-RU")
        startActivityForResult(intent, 100)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val voiceText = result?.get(0)
            voiceText?.let {
                Log.d("VoiceRecognition", "Texto reconocido: $it")
                // Para el reconocimiento de voz, necesitamos obtener la referencia del loader del diálogo actual
                // Por ahora, pasamos null y manejamos el loader en el método sendToChatGPT
                tvMessage!!.text = "Procesando tu pregunta..."
          //      sendToChatGPT(it, tvMessage, btnMicrophone, loaderContainer, null, null, null)

                val prefs = getSharedPreferences("openai_prefs", Context.MODE_PRIVATE)
                val previousId = prefs.getString("last_response_id", null)
               // val previousId = ""

                // Ahora crear vector store y adjuntar el archivo usando corrutina
            /*    coroutineScope.launch {
                    try {
                        tvMessage?.text = "Creando vector store..."
                        val vectorStoreId = createVectorStoreAndAttachFile(
                            context = this@Inspeccion100HorasActivity,
                            apiKey = Constants.IA.APIKEY,
                            fileId = "file-Jypaj4cRkppZsdMXNGLPRM",
                            vectorStoreName = "inspeccion_100_horas_mi171_${System.currentTimeMillis()}",
                            pollForReady = true,
                            pollIntervalMs = 3000,
                            pollTimeoutMs = 120_000
                        )
                        
                        if (vectorStoreId != null) {
                            Log.d("VectorStore", "Vector store creado exitosamente: $vectorStoreId")
                            tvMessage?.text = "Vector store listo. ID: $vectorStoreId"
                        } else {
                            Log.e("VectorStore", "Error al crear vector store")
                            tvMessage?.text = "Error al crear vector store"
                        }
                    } catch (e: Exception) {
                        Log.e("VectorStore", "Excepción al crear vector store: ${e.message}")
                        tvMessage?.text = "Error: ${e.message}"
                    }
                }
            */
                // Primero asegurar que el PDF esté subido
           /*     ensurePDFUploaded { fileId, error ->
                    if (error != null) {
                        Log.e("PDF_Upload", "Error con PDF: $error")
                        tvMessage?.text = "Error al preparar el documento: $error"
                        return@ensurePDFUploaded
                    }
                    
                    Log.d("PDF_Upload", "PDF listo, fileID: $fileId")
                    

                }*/
                sendToResponsesApi(
                    context = this,
                    preguntaVoz = it,
                    previousResponseId = previousId,
                    tvMessage = tvMessage,
                    btnMicrophone = btnMicrophone,
                    loaderContainer = loaderContainer,
                    etQuestion = null,
                    btnSend = null,
                    btnClose = null
                )

                /*
                ensurePDFUploaded { fileId, error ->
                    if (error != null) {
                        Log.e("PDF_Upload", "Error con PDF: $error")
                        tvMessage?.text = "Error al preparar el documento: $error"
                        return@ensurePDFUploaded
                    }
                    
                    Log.d("PDF_Upload", "PDF listo, fileID: $fileId")

                    // Ahora que tenemos el fileID, llamar a sendToResponsesApi
                /*    sendToResponsesApi(
                        context = this,
                        preguntaVoz = it,
                        previousResponseId = previousId,
                        tvMessage = tvMessage,
                        btnMicrophone = btnMicrophone,
                        loaderContainer = loaderContainer,
                        etQuestion = null,
                        btnSend = null,
                        btnClose = null
                    )*/
                }
                */

            }
        } else if (requestCode == 100) {
            Log.e("VoiceRecognition", "Error en reconocimiento de voz: resultCode = $resultCode")
        }
    }

    fun sendToChatGPT(preguntaVoz: String, tvMessage: TextView? = null, btnMicrophone: ImageView? = null, loaderContainer: LinearLayout? = null, etQuestion: EditText? = null, btnSend: ImageView? = null, btnClose: ImageView? = null) {
        // Mostrar loader al inicio y bloquear controles
        showLoader(loaderContainer, "Consultando a la IA...")
        blockControls(etQuestion, btnMicrophone, btnSend, btnClose)
        
        val apiKey = Constants.IA.APIKEY // considerar usar un backend proxy en producción

        // 🔹 Cliente OkHttp con timeouts de 60 segundos
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val mediaType = "application/json; charset=utf-8".toMediaType()

        // Contexto
        val contextoPregunta = buildString {
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_1)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_2)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_3)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_4)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_5)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_13)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_88)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_89)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_90)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_91)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_92)
            append("\n\n")
        }

      /*  val contextoRuso = buildString {
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_3)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_4)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_5)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_15)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_16)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_17)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_18)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_19)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_20)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_21)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_22)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_23)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_24)
            append("\n\n")


        }*/

        // Mensajes con roles (system + user)
        val systemMessage = """
        Eres un asistente experto en mantenimiento de helicópteros MI-171.
        Responde basándote únicamente en el contexto proporcionado sobre la inspección de 100 horas.
        Al final indica en qué página del contexto encontraste la información, el número de página lo encuentras en este formato: '----PAGINA x ----'.
        Si no está en el contexto, di claramente que no puedes responder con la info dada.
        Mantén respuestas técnicas y concisas. El contexto puede estar en ruso pero siempre respondeme en español.
    """.trimIndent()

        val userMessage = """
        CONTEXTO:
        $contextoPregunta

        PREGUNTA: $preguntaVoz
    """.trimIndent()

        // Construir JSON
        val payload = JSONObject().apply {
            put("model", "gpt-5")
            // put("max_completion_tokens", 1000)
            // put("temperature", 0.1)
            val messagesArray = JSONArray()
            messagesArray.put(JSONObject().put("role", "system").put("content", systemMessage))
            messagesArray.put(JSONObject().put("role", "user").put("content", userMessage))
            put("messages", messagesArray)
        }

        val requestBody = payload.toString().toRequestBody(mediaType)
        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .post(requestBody)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Accept", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("OpenAI", "Error: ${e.message}")
                runOnUiThread { 
                    hideLoader(loaderContainer)
                    unblockControls(null, btnMicrophone, null, null)
                    tvMessage?.text = "Error al conectar con la IA: ${e.message}"
                    showErrorDialog(e.message) 
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val bodyStr = it.body?.string()
                    if (!it.isSuccessful) {
                        Log.e("API_ERROR", "HTTP ${it.code}: $bodyStr")
                        runOnUiThread { 
                            hideLoader(loaderContainer)
                            unblockControls(null, btnMicrophone, null, null)
                            tvMessage?.text = "Error HTTP ${it.code}: $bodyStr"
                            showErrorDialog("Error HTTP ${it.code}") 
                        }
                        return
                    }
                    if (bodyStr.isNullOrEmpty()) {
                        Log.e("API_ERROR", "Cuerpo vacío")
                        runOnUiThread { 
                            hideLoader(loaderContainer)
                            unblockControls(null, btnMicrophone, null, null)
                            tvMessage?.text = "Error: Respuesta vacía del servidor"
                        }
                        return
                    }
                    try {
                        val jsonObject = JSONObject(bodyStr)
                        if (jsonObject.has("choices")) {
                            val choices = jsonObject.getJSONArray("choices")
                            if (choices.length() > 0) {
                                val messageObj = choices.getJSONObject(0).optJSONObject("message")
                                val content = messageObj?.optString("content") ?: ""
                                runOnUiThread {
                                    val respuestaIA = content.trim()
                                    
                                    // Ocultar el loader y desbloquear controles
                                    hideLoader(loaderContainer)
                                    unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)
                                    
                                    // Actualizar el TextView del popup si está disponible
                                    tvMessage?.text = respuestaIA
                                    
                                    // Reproducir la respuesta por voz
                                    speakResponse(respuestaIA, btnMicrophone)
                                }
                            } else {
                                Log.e("API_ERROR", "No hay choices en la respuesta")
                                runOnUiThread { 
                                    hideLoader(loaderContainer)
                                    unblockControls(null, btnMicrophone, null, null)
                                    tvMessage?.text = "Error: No se encontraron respuestas válidas"
                                }
                            }
                        } else if (jsonObject.has("error")) {
                            val error = jsonObject.getJSONObject("error").getString("message")
                            Log.e("API_ERROR", error)
                            runOnUiThread { 
                                hideLoader(loaderContainer)
                                unblockControls(null, btnMicrophone, null, null)
                                tvMessage?.text = "Error de la IA: $error"
                                showErrorDialog(error) 
                            }
                        } else {
                            Log.e("API_ERROR", "Respuesta inesperada: $jsonObject")
                            runOnUiThread { 
                                hideLoader(loaderContainer)
                                unblockControls(null, btnMicrophone, null, null)
                                tvMessage?.text = "Error: Respuesta inesperada del servidor"
                            }
                        }
                    } catch (e: JSONException) {
                        Log.e("API_ERROR", "JSON error: ${e.message}")
                        runOnUiThread { 
                            hideLoader(loaderContainer)
                            unblockControls(null, btnMicrophone, null, null)
                            tvMessage?.text = "Error al procesar la respuesta: ${e.message}"
                        }
                    }
                }
            }
        })
    }



    fun disableBackButton() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // No hagas nada aquí para deshabilitar el botón "Atrás"
            }
        })
    }

    @SuppressLint("SuspiciousIndentation")
    fun loadTabHomeFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val homeFragment: Inspecion100horasTabs =
            Inspecion100horasTabs()
        fragmentTransaction.replace(binding.containerView.id, homeFragment)
        fragmentTransaction.commit()
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.popup_ia)
        dialog.getWindow()!!.getAttributes().windowAnimations = R.style.DialogAnimation

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.attributes.alpha = 1f
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

         tvMessage = dialog.findViewById(R.id.tvMessage) as TextView
         etQuestion = dialog.findViewById(R.id.etQuestion) as EditText
         loaderContainer = dialog.findViewById(R.id.loaderContainer) as LinearLayout

         btnClose = dialog.findViewById(R.id.btnClose) as ImageView
        btnClose!!.setOnClickListener {
            dialog.dismiss()
        }

         btnMicrophone = dialog.findViewById(R.id.btnMicrophone) as ImageView
        btnMicrophone!!.setOnClickListener {
            if (isSpeaking) {
                // Si está hablando, pausar
                stopSpeaking()
                setMicrophoneIcon(btnMicrophone!!, false)
            } else {
                // Si no está hablando, iniciar reconocimiento de voz
                initTalk()
            }
        }

         btnSend = dialog.findViewById(R.id.btnSend) as ImageView
        btnSend!!.setOnClickListener {
            val preguntaVoz = etQuestion!!.text.toString()
            if (preguntaVoz.isNotEmpty()) {
                // Mostrar mensaje de carga
                tvMessage!!.text = "Procesando tu pregunta..."
                sendToChatGPT(preguntaVoz, tvMessage, btnMicrophone, loaderContainer, etQuestion, btnSend, btnClose)
            }
        }


        dialog.show()
    }


    /**
     * Reproduce la respuesta por voz y cambia el icono a pausa
     */
    private fun speakResponse(text: String, btnMicrophone: ImageView?) {
        val utteranceId = "response_${System.currentTimeMillis()}"
        val params = Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        
        textToSpeech?.speak(text, TextToSpeech.QUEUE_ADD, params, utteranceId)
        isSpeaking = true
        
        // Cambiar el icono del micrófono a pausa
        btnMicrophone?.let { button ->
            setMicrophoneIcon(button, true)
        }
        
        // Guardar referencia del botón para poder restaurarlo después
        currentMicrophoneButton = btnMicrophone
    }
    
    /**
     * Detiene la reproducción de voz
     */
    private fun stopSpeaking() {
        textToSpeech?.stop()
        isSpeaking = false
    }
    
    /**
     * Cambia el icono del micrófono entre micrófono y pausa
     */
    private fun setMicrophoneIcon(button: ImageView, isPause: Boolean) {
        if (isPause) {
            button.setImageResource(R.drawable.ic_pause_futuristic)
            button.background = getDrawable(R.drawable.button_pause_bg)
        } else {
            button.setImageResource(R.drawable.ic_microphone_futuristic)
            button.background = getDrawable(R.drawable.button_microphone_bg)
        }
    }
    
    /**
     * Muestra el loader futurista con animación infinita
     */
    private fun showLoader(loaderContainer: LinearLayout?, message: String = "Procesando...") {
        loaderContainer?.let { container ->
            runOnUiThread {
                container.visibility = android.view.View.VISIBLE
                val tvLoadingText = container.findViewById<TextView>(R.id.tvLoadingText)
                tvLoadingText?.text = message
                
                // Crear animación infinita para la barra de progreso
                val progressBar = container.findViewById<ProgressBar>(R.id.progressBar)
                progressBar?.let { pb ->
                    startInfiniteProgressAnimation(pb)
                }
            }
        }
    }
    
    /**
     * Inicia la animación infinita de la barra de progreso
     */
    private fun startInfiniteProgressAnimation(progressBar: ProgressBar) {
        val animator = android.animation.ObjectAnimator.ofInt(progressBar, "progress", 0, 100)
        animator.duration = 2000 // 2 segundos para completar
        animator.repeatCount = android.animation.ObjectAnimator.INFINITE
        animator.repeatMode = android.animation.ObjectAnimator.RESTART
        animator.interpolator = android.view.animation.LinearInterpolator()
        animator.start()
    }
    
    /**
     * Oculta el loader futurista
     */
    private fun hideLoader(loaderContainer: LinearLayout?) {
        loaderContainer?.let { container ->
            runOnUiThread {
                container.visibility = android.view.View.GONE
            }
        }
    }
    
    /**
     * Bloquea todos los controles del popup
     */
    private fun blockControls(etQuestion: EditText?, btnMicrophone: ImageView?, btnSend: ImageView?, btnClose: ImageView?) {
        runOnUiThread {
            etQuestion?.isEnabled = false
            etQuestion?.alpha = 0.5f
            
            btnMicrophone?.isEnabled = false
            btnMicrophone?.alpha = 0.5f
            
            btnSend?.isEnabled = false
            btnSend?.alpha = 0.5f
            
            btnClose?.isEnabled = false
            btnClose?.alpha = 0.5f
        }
    }
    
    /**
     * Desbloquea todos los controles del popup
     */
    private fun unblockControls(etQuestion: EditText?, btnMicrophone: ImageView?, btnSend: ImageView?, btnClose: ImageView?) {
        runOnUiThread {
            etQuestion?.isEnabled = true
            etQuestion?.alpha = 1.0f
            
            btnMicrophone?.isEnabled = true
            btnMicrophone?.alpha = 1.0f
            
            btnSend?.isEnabled = true
            btnSend?.alpha = 1.0f
            
            btnClose?.isEnabled = true
            btnClose?.alpha = 1.0f
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        
        // Limpiar TextToSpeech
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
        isSpeaking = false
    }


    // IMPORTS necesarios
 /*   import okhttp3.*
    import okhttp3.MediaType.Companion.toMediaType
    import okhttp3.RequestBody.Companion.toRequestBody
    import org.json.JSONArray
    import org.json.JSONException
    import org.json.JSONObject
    import android.content.Context
    import android.util.Log
    import android.widget.EditText
    import android.widget.ImageView
    import android.widget.LinearLayout
    import android.widget.TextView
    import java.io.IOException
    import java.util.concurrent.TimeUnit*/

    /**
     * sendToResponsesApi - versión adaptada a Responses API (/v1/responses)
     *
     * @param context - para SharedPreferences (guardamos el last_response_id por usuario/sesión)
     * @param preguntaVoz - la pregunta del usuario
     * @param previousResponseId - opcional: si lo pasas, la API intentará continuar esa conversación
     * (si no lo pasas, se inicia una "nueva" conversación en la Responses API)
     * Los demás parámetros son los mismos UI callbacks/controles que usabas.
     */

/*
    fun sendToResponsesApi(
        context: Context,
        preguntaVoz: String,
        previousResponseId: String? = null,
        tvMessage: TextView? = null,
        btnMicrophone: ImageView? = null,
        loaderContainer: LinearLayout? = null,
        etQuestion: EditText? = null,
        btnSend: ImageView? = null,
        btnClose: ImageView? = null
    ) {
        // Mostrar loader y bloquear controles (tus funciones existentes)
        showLoader(loaderContainer, "Consultando a la IA...")
        blockControls(etQuestion, btnMicrophone, btnSend, btnClose)

        val apiKey = Constants.IA.APIKEY // en producción considera usar proxy backend

        // OkHttp con timeouts
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.MINUTES)
            .readTimeout(60, TimeUnit.MINUTES)
            .writeTimeout(60, TimeUnit.MINUTES)
            .build()

        val mediaType = "application/json; charset=utf-8".toMediaType()

        val contextoAenviar = buildString {
            append("NOMBRE DOCUMENTO : INSPECCION 100 HORAS"); append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_1)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_2)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_3)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_4)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_5)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_13)
            append("\n\n")

            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_27)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_28)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_29)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_30)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_35)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_79)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_87)
            append("\n\n")



            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_88)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_89)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_90)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_91)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_92)
            append("\n\n")

            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_93)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_94)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_95)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_96)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_97)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_98)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_99)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_100)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_101)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_102)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_103)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_104)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_105)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_106)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_107)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_108)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_109)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_110)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_111)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_112)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_113)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_116)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_118)
            append("\n\n")
            append(TextoPDF.INSPECCION_100_HORAS_PAGINAS.PAGINA_119)
            append("\n\n")
        }


        val systemMessage = """
        Eres un asistente experto en mantenimiento de helicópteros.
        Aprende este documento detalladamente.
        El nombre de documento lo encuentra en este formato 'NOMBRE DOCUMENTO :' 
        El número de página lo encuentras en este formato: '----PAGINA x ----'.
        Recuerda el nombre del documento y sus paginas ya que cuando luego te pregunte algo , tendras que darme como referencia el nombre del documento y la pagina.
  
    """.trimIndent()

        val userMessage = """
        DOCUMENTO:
        $contextoAenviar    
    """.trimIndent()

        // --- Construir payload de Responses API ---
        val payload = JSONObject()
        try {
            // Modelo (ajusta a tu modelo preferido disponible)
            payload.put("model", "gpt-5") // o "gpt-5" si tu cuenta tiene acceso; adapta según disponibilidad

            // "input" puede ser string o un array de mensajes con roles (system, user, etc.)
            val inputArray = JSONArray()
            inputArray.put(JSONObject().put("role", "system").put("content", systemMessage))
            inputArray.put(JSONObject().put("role", "user").put("content", userMessage))
            payload.put("input", inputArray)

            // Si tenemos previousResponseId, lo añadimos para encadenar la conversación
            if (!previousResponseId.isNullOrBlank()) {
                payload.put("previous_response_id", previousResponseId)
            }

            // Opcionales: puedes ajustar temperatura, top_p, etc. si lo deseas
            // payload.put("temperature", 0.1)
            // payload.put("max_output_tokens", 1000)

        } catch (e: JSONException) {
            Log.e("ResponsesAPI", "Error construyendo JSON: ${e.message}")
            runOnUiThread {
                hideLoader(loaderContainer)
                unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)
                tvMessage?.text = "Error interno al construir la petición"
            }
            return
        }

        val requestBody = payload.toString().toRequestBody(mediaType)
        val request = Request.Builder()
            .url("https://api.openai.com/v1/responses")
            .post(requestBody)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Accept", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("OpenAI", "Error: ${e.message}")
                runOnUiThread {
                    hideLoader(loaderContainer)
                    unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)
                    tvMessage?.text = "Error al conectar con la IA: ${e.message}"
                    showErrorDialog(e.message)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val bodyStr = it.body?.string()
                    if (!it.isSuccessful) {
                        Log.e("API_ERROR", "HTTP ${it.code}: $bodyStr")
                        runOnUiThread {
                            hideLoader(loaderContainer)
                            unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)
                            tvMessage?.text = "Error HTTP ${it.code}: $bodyStr"
                            showErrorDialog("Error HTTP ${it.code}")
                        }
                        return
                    }

                    if (bodyStr.isNullOrEmpty()) {
                        Log.e("API_ERROR", "Cuerpo vacío")
                        runOnUiThread {
                            hideLoader(loaderContainer)
                            unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)
                            tvMessage?.text = "Error: Respuesta vacía del servidor"
                        }
                        return
                    }

                    try {
                        val jsonObject = JSONObject(bodyStr)

                        // Extraer el response.id (para encadenar conversaciones en llamadas futuras)
                        val responseId = jsonObject.optString("id", null)
                        if (!responseId.isNullOrBlank()) {
                            // Ejemplo de guardado sencillo en SharedPreferences
                            val prefs = getSharedPreferences("openai_prefs", Context.MODE_PRIVATE)
                            prefs.edit().putString("last_response_id", responseId).apply()
                            // También puedes retornar/usar este id como prefieras
                        }

                        // 1) Intentar obtener output_text si existe (SDK-friendly)
                        var respuestaTextual: String? = null
                        if (jsonObject.has("output_text")) {
                            respuestaTextual = jsonObject.optString("output_text")
                        } else if (jsonObject.has("output")) {
                            // 2) Si hay "output" (array), concatenar textos encontrados
                            val outputArr = jsonObject.optJSONArray("output")
                            if (outputArr != null) {
                                val sb = StringBuilder()
                                for (i in 0 until outputArr.length()) {
                                    val item = outputArr.optJSONObject(i) ?: continue
                                    // item puede tener "content" que a su vez es array u objeto
                                    if (item.has("content")) {
                                        val content = item.get("content")
                                        when (content) {
                                            is JSONArray -> {
                                                for (j in 0 until content.length()) {
                                                    val cItem = content.optJSONObject(j) ?: continue
                                                    // varios tipos: "type":"output_text" / "text" / "markdown" ...
                                                    if (cItem.has("text")) {
                                                        sb.append(cItem.optString("text"))
                                                    } else if (cItem.has("content") && cItem.get("content") is JSONObject) {
                                                        val inner = cItem.getJSONObject("content")
                                                        if (inner.has("text")) sb.append(inner.optString("text"))
                                                    } else if (cItem.has("type") && cItem.optString("type") == "output_text") {
                                                        // fallback
                                                        sb.append(cItem.optString("text", ""))
                                                    }
                                                    sb.append("\n")
                                                }
                                            }
                                            is JSONObject -> {
                                                if (content.has("text")) sb.append(content.optString("text"))
                                            }
                                            is String -> {
                                                sb.append(content)
                                            }
                                        }
                                    } else {
                                        // fallback: si el item tiene "text"
                                        if (item.has("text")) sb.append(item.optString("text"))
                                    }
                                }
                                respuestaTextual = sb.toString().trim()
                            }
                        } else if (jsonObject.has("choices")) {
                            // Por compatibilidad: algunos endpoints regresan choices -> message.text
                            val choices = jsonObject.optJSONArray("choices")
                            if (choices != null && choices.length() > 0) {
                                val first = choices.optJSONObject(0)
                                val message = first?.optJSONObject("message")
                                val content = message?.optString("content") ?: first?.optString("text")
                                respuestaTextual = content
                            }
                        }

                        // Si no pudimos construir ninguna respuesta textual, tomar todo el JSON como fallback
                        if (respuestaTextual.isNullOrBlank()) {
                            respuestaTextual = jsonObject.toString(2)
                        }

                        // Actualizar UI en hilo principal
                        runOnUiThread {
                            hideLoader(loaderContainer)
                            unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)

                            val respuestaIA = respuestaTextual!!.trim()
                            tvMessage?.text = respuestaIA
                            speakResponse(respuestaIA, btnMicrophone)
                        }
                    } catch (e: JSONException) {
                        Log.e("API_ERROR", "JSON error: ${e.message}")
                        runOnUiThread {
                            hideLoader(loaderContainer)
                            unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)
                            tvMessage?.text = "Error al procesar la respuesta: ${e.message}"
                        }
                    }
                }
            }
        })
    }
*/

/*
        fun sendToResponsesApi(
        context: Context,
        preguntaVoz: String,
        previousResponseId: String? = null,
        tvMessage: TextView? = null,
        btnMicrophone: ImageView? = null,
        loaderContainer: LinearLayout? = null,
        etQuestion: EditText? = null,
        btnSend: ImageView? = null,
        btnClose: ImageView? = null
    ) {
        // Mostrar loader y bloquear controles (tus funciones existentes)
        showLoader(loaderContainer, "Consultando a la IA...")
        blockControls(etQuestion, btnMicrophone, btnSend, btnClose)

        val apiKey = Constants.IA.APIKEY // en producción considera usar proxy backend

        // OkHttp con timeouts
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.MINUTES)
            .readTimeout(60, TimeUnit.MINUTES)
            .writeTimeout(60, TimeUnit.MINUTES)
            .build()

        val mediaType = "application/json; charset=utf-8".toMediaType()

//manual de mantenumiento mi171 ruso

        val contextoAenviar = buildString {
            append("NOMBRE DOCUMENTO : MANUAL DE MANTENIMIENTO MI 171 - RUSO"); append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_3)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_4)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_5)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_15)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_16)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_17)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_18)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_19)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_20)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_21)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_22)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_23)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_24)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_35)
            append("\n\n")

            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_39)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_53)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_54)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_55)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_56)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_63)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_64)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_65)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_69)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_70)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_71)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_72)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_73)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_85)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_86)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_99)
            append("\n\n")
            append(TextoPDF.DOCUMENTO_RUSO.PAGINA_100)
            append("\n\n")



        }

        val systemMessage = """
        Eres un asistente experto en mantenimiento de helicópteros.
        Aprende este DOCUMENTO te lo dare en partes y siempre el nombre estara primero seguido de un listado de paginas.
        El nombre de documento lo encuentra en este formato 'NOMBRE DOCUMENTO :'
        El número de página lo encuentras en este formato: '----PAGINA x ----'.
        Recuerda el nombre del documento y sus paginas ya que cuando luego te pregunte algo , tendras que darme como referencia el nombre del documento y la pagina
    """.trimIndent()

        val userMessage = """
        DOCUMENTO:
        $contextoAenviar
    """.trimIndent()

        // --- Construir payload de Responses API ---
        val payload = JSONObject()
        try {
            // Modelo (ajusta a tu modelo preferido disponible)
            payload.put("model", "gpt-5") // o "gpt-5" si tu cuenta tiene acceso; adapta según disponibilidad

            // "input" puede ser string o un array de mensajes con roles (system, user, etc.)
            val inputArray = JSONArray()
            inputArray.put(JSONObject().put("role", "system").put("content", systemMessage))
            inputArray.put(JSONObject().put("role", "user").put("content", userMessage))
            payload.put("input", inputArray)

            // Si tenemos previousResponseId, lo añadimos para encadenar la conversación
            if (!previousResponseId.isNullOrBlank()) {
                payload.put("previous_response_id", previousResponseId)
            }

            // Opcionales: puedes ajustar temperatura, top_p, etc. si lo deseas
            // payload.put("temperature", 0.1)
            // payload.put("max_output_tokens", 1000)

        } catch (e: JSONException) {
            Log.e("ResponsesAPI", "Error construyendo JSON: ${e.message}")
            runOnUiThread {
                hideLoader(loaderContainer)
                unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)
                tvMessage?.text = "Error interno al construir la petición"
            }
            return
        }

        val requestBody = payload.toString().toRequestBody(mediaType)
        val request = Request.Builder()
            .url("https://api.openai.com/v1/responses")
            .post(requestBody)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Accept", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("OpenAI", "Error: ${e.message}")
                runOnUiThread {
                    hideLoader(loaderContainer)
                    unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)
                    tvMessage?.text = "Error al conectar con la IA: ${e.message}"
                    showErrorDialog(e.message)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val bodyStr = it.body?.string()
                    if (!it.isSuccessful) {
                        Log.e("API_ERROR", "HTTP ${it.code}: $bodyStr")
                        runOnUiThread {
                            hideLoader(loaderContainer)
                            unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)
                            tvMessage?.text = "Error HTTP ${it.code}: $bodyStr"
                            showErrorDialog("Error HTTP ${it.code}")
                        }
                        return
                    }

                    if (bodyStr.isNullOrEmpty()) {
                        Log.e("API_ERROR", "Cuerpo vacío")
                        runOnUiThread {
                            hideLoader(loaderContainer)
                            unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)
                            tvMessage?.text = "Error: Respuesta vacía del servidor"
                        }
                        return
                    }

                    try {
                        val jsonObject = JSONObject(bodyStr)

                        // Extraer el response.id (para encadenar conversaciones en llamadas futuras)
                        val responseId = jsonObject.optString("id", null)
                        if (!responseId.isNullOrBlank()) {
                            // Ejemplo de guardado sencillo en SharedPreferences
                            val prefs = getSharedPreferences("openai_prefs", Context.MODE_PRIVATE)
                            prefs.edit().putString("last_response_id", responseId).apply()
                            // También puedes retornar/usar este id como prefieras
                        }

                        // 1) Intentar obtener output_text si existe (SDK-friendly)
                        var respuestaTextual: String? = null
                        if (jsonObject.has("output_text")) {
                            respuestaTextual = jsonObject.optString("output_text")
                        } else if (jsonObject.has("output")) {
                            // 2) Si hay "output" (array), concatenar textos encontrados
                            val outputArr = jsonObject.optJSONArray("output")
                            if (outputArr != null) {
                                val sb = StringBuilder()
                                for (i in 0 until outputArr.length()) {
                                    val item = outputArr.optJSONObject(i) ?: continue
                                    // item puede tener "content" que a su vez es array u objeto
                                    if (item.has("content")) {
                                        val content = item.get("content")
                                        when (content) {
                                            is JSONArray -> {
                                                for (j in 0 until content.length()) {
                                                    val cItem = content.optJSONObject(j) ?: continue
                                                    // varios tipos: "type":"output_text" / "text" / "markdown" ...
                                                    if (cItem.has("text")) {
                                                        sb.append(cItem.optString("text"))
                                                    } else if (cItem.has("content") && cItem.get("content") is JSONObject) {
                                                        val inner = cItem.getJSONObject("content")
                                                        if (inner.has("text")) sb.append(inner.optString("text"))
                                                    } else if (cItem.has("type") && cItem.optString("type") == "output_text") {
                                                        // fallback
                                                        sb.append(cItem.optString("text", ""))
                                                    }
                                                    sb.append("\n")
                                                }
                                            }
                                            is JSONObject -> {
                                                if (content.has("text")) sb.append(content.optString("text"))
                                            }
                                            is String -> {
                                                sb.append(content)
                                            }
                                        }
                                    } else {
                                        // fallback: si el item tiene "text"
                                        if (item.has("text")) sb.append(item.optString("text"))
                                    }
                                }
                                respuestaTextual = sb.toString().trim()
                            }
                        } else if (jsonObject.has("choices")) {
                            // Por compatibilidad: algunos endpoints regresan choices -> message.text
                            val choices = jsonObject.optJSONArray("choices")
                            if (choices != null && choices.length() > 0) {
                                val first = choices.optJSONObject(0)
                                val message = first?.optJSONObject("message")
                                val content = message?.optString("content") ?: first?.optString("text")
                                respuestaTextual = content
                            }
                        }

                        // Si no pudimos construir ninguna respuesta textual, tomar todo el JSON como fallback
                        if (respuestaTextual.isNullOrBlank()) {
                            respuestaTextual = jsonObject.toString(2)
                        }

                        // Actualizar UI en hilo principal
                        runOnUiThread {
                            hideLoader(loaderContainer)
                            unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)

                            val respuestaIA = respuestaTextual!!.trim()
                            tvMessage?.text = respuestaIA
                            speakResponse(respuestaIA, btnMicrophone)
                        }
                    } catch (e: JSONException) {
                        Log.e("API_ERROR", "JSON error: ${e.message}")
                        runOnUiThread {
                            hideLoader(loaderContainer)
                            unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)
                            tvMessage?.text = "Error al procesar la respuesta: ${e.message}"
                        }
                    }
                }
            }
        })
    }
*/





    fun sendToResponsesApi(
        context: Context,
        preguntaVoz: String,
        previousResponseId: String? = null,
        tvMessage: TextView? = null,
        btnMicrophone: ImageView? = null,
        loaderContainer: LinearLayout? = null,
        etQuestion: EditText? = null,
        btnSend: ImageView? = null,
        btnClose: ImageView? = null
    ) {
        // Mostrar loader y bloquear controles (tus funciones existentes)
        showLoader(loaderContainer, "Consultando a la IA...")
        blockControls(etQuestion, btnMicrophone, btnSend, btnClose)

        val apiKey = Constants.IA.APIKEY // en producción considera usar proxy backend

        // OkHttp con timeouts
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.MINUTES)
            .readTimeout(60, TimeUnit.MINUTES)
            .writeTimeout(60, TimeUnit.MINUTES)
            .build()

        val mediaType = "application/json; charset=utf-8".toMediaType()

        val systemMessage = """
        Eres un asistente experto en mantenimiento de helicópteros.
        Responde basándote en los documentos que te proporcione anteriormente.
        Analiza que documento usar y usalo para responder.
        Al final indica de que documento encontraste la información. El nombre de documento lo encuentra en este formato "NOMBRE DOCUMENTO :
        Al final indica en qué página del documento encontraste la información, el número de página o páginas lo encuentras en este formato: '----PAGINA x ----'.
        Si no está en el documento, di claramente que no puedes responder con la info dada.
        Mantén respuestas técnicas y concisas. El documento puede estar en ruso pero siempre respondeme en español.
    """.trimIndent()

        val userMessage = """


        PREGUNTA: $preguntaVoz
    """.trimIndent()


        // --- Construir payload de Responses API ---
        val payload = JSONObject()
        try {
            // Modelo (ajusta a tu modelo preferido disponible)
            payload.put("model", "gpt-5") // o "gpt-5" si tu cuenta tiene acceso; adapta según disponibilidad

            // "input" puede ser string o un array de mensajes con roles (system, user, etc.)
            val inputArray = JSONArray()
            inputArray.put(JSONObject().put("role", "system").put("content", systemMessage))
            inputArray.put(JSONObject().put("role", "user").put("content", userMessage))
            payload.put("input", inputArray)

            // Si tenemos previousResponseId, lo añadimos para encadenar la conversación
            if (!previousResponseId.isNullOrBlank()) {
                payload.put("previous_response_id", previousResponseId)
            }

            // Opcionales: puedes ajustar temperatura, top_p, etc. si lo deseas
            // payload.put("temperature", 0.1)
            // payload.put("max_output_tokens", 1000)

        } catch (e: JSONException) {
            Log.e("ResponsesAPI", "Error construyendo JSON: ${e.message}")
            runOnUiThread {
                hideLoader(loaderContainer)
                unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)
                tvMessage?.text = "Error interno al construir la petición"
            }
            return
        }

        val requestBody = payload.toString().toRequestBody(mediaType)
        val request = Request.Builder()
            .url("https://api.openai.com/v1/responses")
            .post(requestBody)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Accept", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("OpenAI", "Error: ${e.message}")
                runOnUiThread {
                    hideLoader(loaderContainer)
                    unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)
                    tvMessage?.text = "Error al conectar con la IA: ${e.message}"
                    showErrorDialog(e.message)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val bodyStr = it.body?.string()
                    if (!it.isSuccessful) {
                        Log.e("API_ERROR", "HTTP ${it.code}: $bodyStr")
                        runOnUiThread {
                            hideLoader(loaderContainer)
                            unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)
                            tvMessage?.text = "Error HTTP ${it.code}: $bodyStr"
                            showErrorDialog("Error HTTP ${it.code}")
                        }
                        return
                    }

                    if (bodyStr.isNullOrEmpty()) {
                        Log.e("API_ERROR", "Cuerpo vacío")
                        runOnUiThread {
                            hideLoader(loaderContainer)
                            unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)
                            tvMessage?.text = "Error: Respuesta vacía del servidor"
                        }
                        return
                    }

                    try {
                        val jsonObject = JSONObject(bodyStr)

                        // Extraer el response.id (para encadenar conversaciones en llamadas futuras)
                        val responseId = jsonObject.optString("id", null)
                        if (!responseId.isNullOrBlank()) {
                            // Ejemplo de guardado sencillo en SharedPreferences
                            val prefs = context.getSharedPreferences("openai_prefs", Context.MODE_PRIVATE)
                            prefs.edit().putString("last_response_id", responseId).apply()
                            // También puedes retornar/usar este id como prefieras
                        }

                        // 1) Intentar obtener output_text si existe (SDK-friendly)
                        var respuestaTextual: String? = null
                        if (jsonObject.has("output_text")) {
                            respuestaTextual = jsonObject.optString("output_text")
                        } else if (jsonObject.has("output")) {
                            // 2) Si hay "output" (array), concatenar textos encontrados
                            val outputArr = jsonObject.optJSONArray("output")
                            if (outputArr != null) {
                                val sb = StringBuilder()
                                for (i in 0 until outputArr.length()) {
                                    val item = outputArr.optJSONObject(i) ?: continue
                                    // item puede tener "content" que a su vez es array u objeto
                                    if (item.has("content")) {
                                        val content = item.get("content")
                                        when (content) {
                                            is JSONArray -> {
                                                for (j in 0 until content.length()) {
                                                    val cItem = content.optJSONObject(j) ?: continue
                                                    // varios tipos: "type":"output_text" / "text" / "markdown" ...
                                                    if (cItem.has("text")) {
                                                        sb.append(cItem.optString("text"))
                                                    } else if (cItem.has("content") && cItem.get("content") is JSONObject) {
                                                        val inner = cItem.getJSONObject("content")
                                                        if (inner.has("text")) sb.append(inner.optString("text"))
                                                    } else if (cItem.has("type") && cItem.optString("type") == "output_text") {
                                                        // fallback
                                                        sb.append(cItem.optString("text", ""))
                                                    }
                                                    sb.append("\n")
                                                }
                                            }
                                            is JSONObject -> {
                                                if (content.has("text")) sb.append(content.optString("text"))
                                            }
                                            is String -> {
                                                sb.append(content)
                                            }
                                        }
                                    } else {
                                        // fallback: si el item tiene "text"
                                        if (item.has("text")) sb.append(item.optString("text"))
                                    }
                                }
                                respuestaTextual = sb.toString().trim()
                            }
                        } else if (jsonObject.has("choices")) {
                            // Por compatibilidad: algunos endpoints regresan choices -> message.text
                            val choices = jsonObject.optJSONArray("choices")
                            if (choices != null && choices.length() > 0) {
                                val first = choices.optJSONObject(0)
                                val message = first?.optJSONObject("message")
                                val content = message?.optString("content") ?: first?.optString("text")
                                respuestaTextual = content
                            }
                        }

                        // Si no pudimos construir ninguna respuesta textual, tomar todo el JSON como fallback
                        if (respuestaTextual.isNullOrBlank()) {
                            respuestaTextual = jsonObject.toString(2)
                        }

                        // Actualizar UI en hilo principal
                        runOnUiThread {
                            hideLoader(loaderContainer)
                            unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)

                            val respuestaIA = respuestaTextual!!.trim()
                            tvMessage?.text = respuestaIA
                            speakResponse(respuestaIA, btnMicrophone)
                        }
                    } catch (e: JSONException) {
                        Log.e("API_ERROR", "JSON error: ${e.message}")
                        runOnUiThread {
                            hideLoader(loaderContainer)
                            unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)
                            tvMessage?.text = "Error al procesar la respuesta: ${e.message}"
                        }
                    }
                }
            }
        })
    }



    var urlPDF_Inspeccion100Horas = "https://firebasestorage.googleapis.com/v0/b/autoservicio-87532.appspot.com/o/00.-%20Insp.%20100%20horas%20(Mi-171)%20rev.%208%20COMPLETO%20CON%20PND%20%2B%20PM-318.pdf?alt=media&token=18b36c7f-f79a-45ae-ab74-d8201a88c7f6"

    var prefsOpenAI = "openai_prefs"
    var documento_inspeccion_100_horas = "documento_inspeccion_100_horas"
    var vectorID_inspeccion_100_horas = "vectorID_inspeccion_100_horas"

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .build()

    fun downloadFileBytes(url: String, callback: (ByteArray?, String?) -> Unit) {
        val request = Request.Builder().url(url).get().build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, "Error al descargar el archivo: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        callback(null, "Error HTTP al descargar: ${it.code}")
                        return
                    }
                    val bytes = it.body?.bytes()
                    if (bytes == null || bytes.isEmpty()) {
                        callback(null, "Archivo descargado vacío")
                        return
                    }
                    callback(bytes, null)
                }
            }
        })
    }



    fun uploadFileToOpenAI(
        context: Context,
        apiKey: String,
        fileName: String,
        fileBytes: ByteArray,
        purpose: String = "assistants",
        callback: (String?, String?) -> Unit
    ) {
        val mediaTypeOctet = "application/octet-stream".toMediaType()

        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("file", fileName, fileBytes.toRequestBody(mediaTypeOctet))
            .addFormDataPart("purpose", purpose)
            .build()

        val request = Request.Builder()
            .url("https://api.openai.com/v1/files")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody)
            .build()

        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, "Error al subir archivo a OpenAI: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val bodyStr = it.body?.string()
                    if (!it.isSuccessful) {
                        callback(null, "Upload HTTP ${it.code}: $bodyStr")
                        return
                    }
                    try {
                        val json = JSONObject(bodyStr ?: "{}")
                        val fileId = json.optString("id", null)
                        if (fileId.isNullOrBlank()) {
                            callback(null, "No se obtuvo file id: $json")
                            return
                        }
                        // Guardar en SharedPreferences para reusar
                        val prefs = context.getSharedPreferences(prefsOpenAI, Context.MODE_PRIVATE)
                        prefs.edit().putString(documento_inspeccion_100_horas, fileId).apply()

                        callback(fileId, null)
                    } catch (e: Exception) {
                        callback(null, "Error parseando respuesta upload: ${e.message}")
                    }
                }
            }
        })
    }

    /**
     * Función integrada que descarga el PDF desde la URL y lo sube a OpenAI
     * Guarda automáticamente el fileID en SharedPreferences
     */
    fun downloadAndUploadPDFToOpenAI(
        callback: (String?, String?) -> Unit
    ) {
        Log.d("PDF_Upload", "Iniciando descarga y upload del PDF...")
        
        // Primero descargar el archivo
        downloadFileBytes(urlPDF_Inspeccion100Horas) { fileBytes, downloadError ->
            if (downloadError != null) {
                Log.e("PDF_Upload", "Error descargando PDF: $downloadError")
                callback(null, downloadError)
                return@downloadFileBytes
            }
            
            if (fileBytes == null) {
                Log.e("PDF_Upload", "Archivo descargado es null")
                callback(null, "Archivo descargado es null")
                return@downloadFileBytes
            }
            
            Log.d("PDF_Upload", "PDF descargado exitosamente, tamaño: ${fileBytes.size} bytes")
            
            // Ahora subir a OpenAI
            val fileName = documento_inspeccion_100_horas+".pdf"
            val apiKey = Constants.IA.APIKEY
            
            uploadFileToOpenAI(
                context = this,
                apiKey = apiKey,
                fileName = fileName,
                fileBytes = fileBytes,
                purpose = "assistants"
            ) { fileId, uploadError ->
                if (uploadError != null) {
                    Log.e("PDF_Upload", "Error subiendo a OpenAI: $uploadError")
                    callback(null, uploadError)
                } else {
                    Log.d("PDF_Upload", "PDF subido exitosamente, fileID: $fileId")
                    callback(fileId, null)
                }
            }
        }
    }

    /**
     * Obtiene el fileID del PDF desde SharedPreferences
     * @return fileID si existe, null si no está guardado
     */
    fun getPDFFileID(): String? {
        val prefs = getSharedPreferences(prefsOpenAI, Context.MODE_PRIVATE)
        return prefs.getString(documento_inspeccion_100_horas, null)
    }

    /**
     * Verifica si el PDF ya está subido a OpenAI
     * @return true si el fileID existe en SharedPreferences
     */
    fun isPDFAlreadyUploaded(): Boolean {
        return getPDFFileID() != null
    }

    /**
     * Función completa que maneja el upload del PDF:
     * - Si ya está subido, devuelve el fileID existente
     * - Si no está subido, lo descarga y sube automáticamente
     */
    fun ensurePDFUploaded(callback: (String?, String?) -> Unit) {
        val existingFileId = getPDFFileID()
        if (existingFileId != null) {
            Log.d("PDF_Upload", "PDF ya está subido, usando fileID existente: $existingFileId")
            callback(existingFileId, null)
            return
        }
        
        Log.d("PDF_Upload", "PDF no está subido, iniciando proceso de upload...")
        downloadAndUploadPDFToOpenAI(callback)
    }


    var vectorID = "vs_68ed619d46a4819198a834e96c0c0faa"
/*
   fun sendToResponsesApi(
       context: Context,
       preguntaVoz: String,
       previousResponseId: String? = null,
       tvMessage: TextView? = null,
       btnMicrophone: ImageView? = null,
       loaderContainer: LinearLayout? = null,
       etQuestion: EditText? = null,
       btnSend: ImageView? = null,
       btnClose: ImageView? = null
   ) {
       // Mostrar loader y bloquear controles (tus funciones existentes)
       showLoader(loaderContainer, "Consultando a la IA...")
       blockControls(etQuestion, btnMicrophone, btnSend, btnClose)

       val apiKey = Constants.IA.APIKEY // en producción considera usar proxy backend

       // OkHttp con timeouts
       val client = OkHttpClient.Builder()
           .connectTimeout(360, TimeUnit.SECONDS)
           .readTimeout(360, TimeUnit.SECONDS)
           .writeTimeout(360, TimeUnit.SECONDS)
           .build()

       val mediaType = "application/json; charset=utf-8".toMediaType()

       val systemMessage = """
       Eres un asistente experto en mantenimiento de helicópteros.
       Responde basándote en los documentos que te proporcione anteriormente.
       Analiza que documento usar y usalo para responder.
       Si no está en el documento, di claramente que no puedes responder con la info dada.
       Mantén respuestas técnicas y concisas. El documento puede estar en ruso pero siempre respondeme en español.
       Despues de cada respuesta da como referencia el documento y la pagina en donde enocntraste la o las respuestas.
   """.trimIndent()

       val userMessage = """


       PREGUNTA: $preguntaVoz
   """.trimIndent()


       // --- Construir payload de Responses API ---
       val payload = JSONObject()
       try {
           // Modelo (ajusta a tu modelo preferido disponible)
           payload.put("model", "gpt-5") // o "gpt-5" si tu cuenta tiene acceso; adapta según disponibilidad

           // "input" puede ser string o un array de mensajes con roles (system, user, etc.)
           val inputArray = JSONArray()
           inputArray.put(JSONObject().put("role", "system").put("content", systemMessage))
           inputArray.put(JSONObject().put("role", "user").put("content", userMessage))
           payload.put("input", inputArray)

      //-------------

           val vectorStoreIds = JSONArray().apply { put(vectorID) } // <- TU vector_store id aquí

           val fileSearchTool = JSONObject()
           fileSearchTool.put("type", "file_search")
           fileSearchTool.put("vector_store_ids", vectorStoreIds)
            // Opcional: puedes añadir parámetros de filtrado o limit
            // fileSearchTool.put("limit", 5)

           val toolsArr = JSONArray().apply {
               put(fileSearchTool)
           }

           payload.put("tools", toolsArr)

           //-------------------------

           // Si tenemos previousResponseId, lo añadimos para encadenar la conversación
           if (!previousResponseId.isNullOrBlank()) {
               payload.put("previous_response_id", previousResponseId)
           }

       } catch (e: JSONException) {
           Log.e("ResponsesAPI", "Error construyendo JSON: ${e.message}")
           runOnUiThread {
               hideLoader(loaderContainer)
               unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)
               tvMessage?.text = "Error interno al construir la petición"
           }
           return
       }

       val requestBody = payload.toString().toRequestBody(mediaType)
       val request = Request.Builder()
           .url("https://api.openai.com/v1/responses")
           .post(requestBody)
           .addHeader("Authorization", "Bearer $apiKey")
           .addHeader("Accept", "application/json")
           .build()

       client.newCall(request).enqueue(object : Callback {
           override fun onFailure(call: Call, e: IOException) {
               Log.e("OpenAI", "Error: ${e.message}")
               runOnUiThread {
                   hideLoader(loaderContainer)
                   unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)
                   tvMessage?.text = "Error al conectar con la IA: ${e.message}"
                   showErrorDialog(e.message)
               }
           }

           override fun onResponse(call: Call, response: Response) {
               response.use {
                   val bodyStr = it.body?.string()
                   if (!it.isSuccessful) {
                       Log.e("API_ERROR", "HTTP ${it.code}: $bodyStr")
                       runOnUiThread {
                           hideLoader(loaderContainer)
                           unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)
                           tvMessage?.text = "Error HTTP ${it.code}: $bodyStr"
                           showErrorDialog("Error HTTP ${it.code}")
                       }
                       return
                   }

                   if (bodyStr.isNullOrEmpty()) {
                       Log.e("API_ERROR", "Cuerpo vacío")
                       runOnUiThread {
                           hideLoader(loaderContainer)
                           unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)
                           tvMessage?.text = "Error: Respuesta vacía del servidor"
                       }
                       return
                   }

                   try {
                       val jsonObject = JSONObject(bodyStr)

                       // Extraer el response.id (para encadenar conversaciones en llamadas futuras)
                       val responseId = jsonObject.optString("id", null)
                       if (!responseId.isNullOrBlank()) {
                           // Ejemplo de guardado sencillo en SharedPreferences
                           val prefs = context.getSharedPreferences("openai_prefs", Context.MODE_PRIVATE)
                           prefs.edit().putString("last_response_id", responseId).apply()
                           // También puedes retornar/usar este id como prefieras
                       }

                       // 1) Intentar obtener output_text si existe (SDK-friendly)
                       var respuestaTextual: String? = null
                       if (jsonObject.has("output_text")) {
                           respuestaTextual = jsonObject.optString("output_text")
                       } else if (jsonObject.has("output")) {
                           // 2) Si hay "output" (array), concatenar textos encontrados
                           val outputArr = jsonObject.optJSONArray("output")
                           if (outputArr != null) {
                               val sb = StringBuilder()
                               for (i in 0 until outputArr.length()) {
                                   val item = outputArr.optJSONObject(i) ?: continue
                                   // item puede tener "content" que a su vez es array u objeto
                                   if (item.has("content")) {
                                       val content = item.get("content")
                                       when (content) {
                                           is JSONArray -> {
                                               for (j in 0 until content.length()) {
                                                   val cItem = content.optJSONObject(j) ?: continue
                                                   // varios tipos: "type":"output_text" / "text" / "markdown" ...
                                                   if (cItem.has("text")) {
                                                       sb.append(cItem.optString("text"))
                                                   } else if (cItem.has("content") && cItem.get("content") is JSONObject) {
                                                       val inner = cItem.getJSONObject("content")
                                                       if (inner.has("text")) sb.append(inner.optString("text"))
                                                   } else if (cItem.has("type") && cItem.optString("type") == "output_text") {
                                                       // fallback
                                                       sb.append(cItem.optString("text", ""))
                                                   }
                                                   sb.append("\n")
                                               }
                                           }
                                           is JSONObject -> {
                                               if (content.has("text")) sb.append(content.optString("text"))
                                           }
                                           is String -> {
                                               sb.append(content)
                                           }
                                       }
                                   } else {
                                       // fallback: si el item tiene "text"
                                       if (item.has("text")) sb.append(item.optString("text"))
                                   }
                               }
                               respuestaTextual = sb.toString().trim()
                           }
                       } else if (jsonObject.has("choices")) {
                           // Por compatibilidad: algunos endpoints regresan choices -> message.text
                           val choices = jsonObject.optJSONArray("choices")
                           if (choices != null && choices.length() > 0) {
                               val first = choices.optJSONObject(0)
                               val message = first?.optJSONObject("message")
                               val content = message?.optString("content") ?: first?.optString("text")
                               respuestaTextual = content
                           }
                       }

                       // Si no pudimos construir ninguna respuesta textual, tomar todo el JSON como fallback
                       if (respuestaTextual.isNullOrBlank()) {
                           respuestaTextual = jsonObject.toString(2)
                       }

                       // Actualizar UI en hilo principal
                       runOnUiThread {
                           hideLoader(loaderContainer)
                           unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)

                           val respuestaIA = respuestaTextual!!.trim()
                           tvMessage?.text = respuestaIA
                           speakResponse(respuestaIA, btnMicrophone)
                       }
                   } catch (e: JSONException) {
                       Log.e("API_ERROR", "JSON error: ${e.message}")
                       runOnUiThread {
                           hideLoader(loaderContainer)
                           unblockControls(etQuestion, btnMicrophone, btnSend, btnClose)
                           tvMessage?.text = "Error al procesar la respuesta: ${e.message}"
                       }
                   }
               }
           }
       })
   }
*/

   // private const val TAG = "VectorStore"

    suspend fun createVectorStoreAndAttachFile(
        context: Context,
        apiKey: String,
        fileId: String,
        vectorStoreName: String = "vs_from_android_${System.currentTimeMillis()}",
        pollForReady: Boolean = true,
        pollIntervalMs: Long = 3000,
        pollTimeoutMs: Long = 120_000
    ): String? = withContext(Dispatchers.IO) {
        try {
            // 1) Crear vector store
            val createJson = JSONObject().apply {
                put("name", vectorStoreName)
            }
            val createReq = Request.Builder()
                .url("https://api.openai.com/v1/vector_stores")
                .post(createJson.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()))
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("Accept", "application/json")
                .build()

            httpClient.newCall(createReq).execute().use { resp ->
                val body = resp.body?.string()
                if (!resp.isSuccessful) {
                    Log.e("TAG", "create vector store failed HTTP ${resp.code}: $body")
                    return@withContext null
                }
                if (body.isNullOrBlank()) {
                    Log.e("TAG", "create vector store returned empty body")
                    return@withContext null
                }

                val json = JSONObject(body)
                val vsId = json.optString("id", null)
                if (vsId.isNullOrBlank()) {
                    Log.e("TAG", "no id in create vector store response: $body")
                    return@withContext null
                }

                Log.i("TAG", "Vector store created: $vsId")

                // 2) Asociar el file al vector store (JSON)
                val attachJson = JSONObject().apply {
                    put("file_id", fileId)
                }
                
                val attachReq = Request.Builder()
                    .url("https://api.openai.com/v1/vector_stores/$vsId/files")
                    .post(attachJson.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()))
                    .addHeader("Authorization", "Bearer $apiKey")
                    .addHeader("Accept", "application/json")
                    .build()

                httpClient.newCall(attachReq).execute().use { attachResp ->
                    val attachBody = attachResp.body?.string()
                    if (!attachResp.isSuccessful) {
                        Log.e("TAG", "attach file failed HTTP ${attachResp.code}: $attachBody")
                        // Podemos retornar vsId aún si attach falló, pero normalmente queremos null
                        return@withContext null
                    }
                    Log.i("TAG", "File attached to vector store: $attachBody")
                }

                // 3) Opcional: poll para saber cuándo el vector store está "ready"
                if (pollForReady) {
                    val start = System.currentTimeMillis()
                    while (true) {
                        // GET /v1/vector_stores/{vsId}
                        val statusReq = Request.Builder()
                            .url("https://api.openai.com/v1/vector_stores/$vsId")
                            .get()
                            .addHeader("Authorization", "Bearer $apiKey")
                            .addHeader("Accept", "application/json")
                            .build()

                        httpClient.newCall(statusReq).execute().use { statusResp ->
                            val statusBody = statusResp.body?.string()
                            if (!statusResp.isSuccessful) {
                                Log.w("TAG", "status check HTTP ${statusResp.code}: $statusBody")
                                // esperar e intentar de nuevo
                            } else if (!statusBody.isNullOrBlank()) {
                                try {
                                    val statusJson = JSONObject(statusBody)
                                    // Muchos endpoints devuelven "status" o "state", intentar ambos
                                    val status = when {
                                        statusJson.has("status") -> statusJson.optString("status")
                                        statusJson.has("state") -> statusJson.optString("state")
                                        else -> null
                                    }

                                    Log.i("TAG", "vector store status: $status")

                                    // Si el endpoint devuelve 'ready' (o 'succeeded') consideramos listo
                                    if (status != null && (status.equals("ready", ignoreCase = true) || status.equals("succeeded", ignoreCase = true) || status.equals("available", ignoreCase = true)  || status.equals("completed", ignoreCase = true))) {
                                        // Guardar en SharedPreferences
                                        val prefs = context.getSharedPreferences(prefsOpenAI, Context.MODE_PRIVATE)
                                        prefs.edit().putString(vectorID_inspeccion_100_horas, vsId).apply()
                                        Log.i("TAG", "Vector store listo. Guardado en prefs: $vsId")
                                        return@withContext vsId
                                    }
                                } catch (je: Exception) {
                                    Log.w("TAG", "no pudimos parsear status JSON: $je")
                                }
                            }

                            // Timeout?
                            if (System.currentTimeMillis() - start > pollTimeoutMs) {
                                Log.w("TAG", "poll timeout alcanzado, retornando vsId sin asegurar ready")
                                // Guardar de todas formas
                                val prefs = context.getSharedPreferences(prefsOpenAI, Context.MODE_PRIVATE)
                                prefs.edit().putString(vectorID_inspeccion_100_horas, vsId).apply()
                                return@withContext vsId
                            }
                        }

                        delay(pollIntervalMs)
                    } // end while
                } else {
                    // Si no hacemos polling, guardamos y retornamos el id inmediatamente
                    val prefs = context.getSharedPreferences(prefsOpenAI, Context.MODE_PRIVATE)
                    prefs.edit().putString(vectorID_inspeccion_100_horas, vsId).apply()
                    return@withContext vsId
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", "Exception creating vector store: ${e.message}", e)
            return@withContext null
        }
    }?.toString()



}