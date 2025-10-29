package com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.helisur.helisurapp.R

/**
 * Ejemplo de cómo usar el popup de IA
 */
class PopupIAExample(private val context: Context) {
    
    private var dialog: Dialog? = null
    private var etQuestion: EditText? = null
    private var btnMicrophone: ImageView? = null
    private var btnSend: ImageView? = null
    private var btnClose: ImageView? = null
    private var loaderContainer: LinearLayout? = null
    private var robotLoader: ImageView? = null
    private var tvLoadingText: TextView? = null
    private var progressBar: ProgressBar? = null
    
    /**
     * Muestra el popup de IA
     */
    fun showPopup() {
        dialog = Dialog(context)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.popup_ia)
        dialog?.setCancelable(true)
        
        // Configurar el fondo del diálogo
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        
        // Inicializar vistas
        initializeViews()
        
        // Configurar listeners
        setupListeners()
        
        // Mostrar el popup
        dialog?.show()
    }
    
    /**
     * Inicializa las vistas del popup
     */
    private fun initializeViews() {
        etQuestion = dialog?.findViewById(R.id.etQuestion)
        btnMicrophone = dialog?.findViewById(R.id.btnMicrophone)
        btnSend = dialog?.findViewById(R.id.btnSend)
        btnClose = dialog?.findViewById(R.id.btnClose)
        loaderContainer = dialog?.findViewById(R.id.loaderContainer)
        robotLoader = dialog?.findViewById(R.id.robotLoader)
        tvLoadingText = dialog?.findViewById(R.id.tvLoadingText)
        progressBar = dialog?.findViewById(R.id.progressBar)
    }
    
    /**
     * Configura los listeners de los botones
     */
    private fun setupListeners() {
        // Botón cerrar
        btnClose?.setOnClickListener {
            hidePopup()
        }
        
        // Botón micrófono
        btnMicrophone?.setOnClickListener {
            // Aquí puedes implementar el reconocimiento de voz
            startVoiceRecognition()
        }
        
        // Botón enviar
        btnSend?.setOnClickListener {
            val question = etQuestion?.text?.toString() ?: ""
            if (question.isNotEmpty()) {
                sendQuestion(question)
            }
        }
    }
    
    /**
     * Inicia el reconocimiento de voz
     */
    private fun startVoiceRecognition() {
        // Implementar reconocimiento de voz aquí
        // Por ejemplo, usando SpeechRecognizer
    }
    
    /**
     * Envía la pregunta a la IA
     */
    private fun sendQuestion(question: String) {
        // Mostrar loader
        showLoader("Procesando tu pregunta...")
        
        // Simular llamada a la IA
        // En la implementación real, aquí llamarías a tu servicio de IA
        simulateAIResponse(question)
    }
    
    /**
     * Simula una respuesta de la IA (para ejemplo)
     */
    private fun simulateAIResponse(question: String) {
        // Simular delay de red
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            hideLoader()
            // Aquí mostrarías la respuesta de la IA
            // Por ejemplo, agregando un mensaje al chat
        }, 3000)
    }
    
    /**
     * Muestra el loader futurista
     */
    fun showLoader(message: String = "Procesando...") {
        loaderContainer?.visibility = View.VISIBLE
        tvLoadingText?.text = message
        progressBar?.progress = 0
        
        // Animar el progreso
        animateProgress()
    }
    
    /**
     * Oculta el loader
     */
    fun hideLoader() {
        loaderContainer?.visibility = View.GONE
    }
    
    /**
     * Anima la barra de progreso
     */
    private fun animateProgress() {
        progressBar?.let { pb ->
            android.animation.ObjectAnimator.ofInt(pb, "progress", 0, 100)
                .setDuration(3000)
                .start()
        }
    }
    
    /**
     * Oculta el popup
     */
    fun hidePopup() {
        dialog?.dismiss()
        dialog = null
    }
    
    /**
     * Verifica si el popup está visible
     */
    fun isShowing(): Boolean {
        return dialog?.isShowing ?: false
    }
}

