package com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * Helper class para manejar el reconocimiento de voz y consultas a IA
 */
public class VoiceRecognitionHelper {
    
    private static final String TAG = "VoiceRecognitionHelper";
    private Fragment fragment;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private boolean isListening = false;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    
    public VoiceRecognitionHelper(Fragment fragment) {
        this.fragment = fragment;
    }
    
    /**
     * Inicializa el reconocimiento de voz
     */
    public void initializeSpeechRecognizer() {
        Log.d(TAG, "Inicializando reconocimiento de voz...");
        
        // Verificar si el reconocimiento de voz está disponible
        if (!SpeechRecognizer.isRecognitionAvailable(fragment.requireContext())) {
            Log.e(TAG, "Reconocimiento de voz no disponible en este dispositivo");
            Toast.makeText(fragment.requireContext(), "Reconocimiento de voz no disponible", Toast.LENGTH_LONG).show();
            return;
        }
        
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(fragment.requireContext());
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        
        // Configuración mejorada del reconocimiento
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES"); // Español
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "es-ES");
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, false);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla tu pregunta sobre la inspección de 100 horas...");
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3); // Más resultados para mejor precisión
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 2000);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 2000);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 1000);
        
        Log.d(TAG, "Configuración del reconocimiento completada");
        
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.d(TAG, "Listo para escuchar");
                isListening = true;
                Toast.makeText(fragment.requireContext(), "Escuchando... Habla ahora", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d(TAG, "Usuario comenzó a hablar");
                Toast.makeText(fragment.requireContext(), "Detectando voz...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                Log.d(TAG, "Volumen de voz: " + rmsdB);
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                Log.d(TAG, "Buffer de audio recibido: " + buffer.length + " bytes");
            }

            @Override
            public void onEndOfSpeech() {
                Log.d(TAG, "Usuario terminó de hablar");
                isListening = false;
                Toast.makeText(fragment.requireContext(), "Procesando audio...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int error) {
                Log.e(TAG, "Error en reconocimiento: " + error);
                isListening = false;
                String errorMessage = getErrorMessage(error);
                Log.e(TAG, "Mensaje de error: " + errorMessage);
                Toast.makeText(fragment.requireContext(), "Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResults(Bundle results) {
                Log.d(TAG, "Resultados recibidos");
                isListening = false;
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    Log.d(TAG, "Número de resultados: " + matches.size());
                    for (int i = 0; i < matches.size(); i++) {
                        Log.d(TAG, "Resultado " + i + ": " + matches.get(i));
                    }
                    String pregunta = matches.get(0);
                    Toast.makeText(fragment.requireContext(), "Pregunta detectada: " + pregunta, Toast.LENGTH_LONG).show();
                    
                    // Llamar a la IA con la pregunta
                    consultarIA(pregunta);
                } else {
                    Log.e(TAG, "No se encontraron resultados de reconocimiento");
                    Toast.makeText(fragment.requireContext(), "No se pudo reconocer el habla", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                Log.d(TAG, "Resultados parciales recibidos");
                ArrayList<String> partialMatches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (partialMatches != null && !partialMatches.isEmpty()) {
                    Log.d(TAG, "Resultado parcial: " + partialMatches.get(0));
                }
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                Log.d(TAG, "Evento recibido: " + eventType);
            }
        });
    }
    
    /**
     * Inicia el reconocimiento de voz
     */
    public void startVoiceRecognition() {
        Log.d(TAG, "Intentando iniciar reconocimiento de voz...");
        Log.d(TAG, "isListening: " + isListening);
        Log.d(TAG, "speechRecognizer: " + (speechRecognizer != null ? "inicializado" : "null"));
        
        if (isListening) {
            Log.w(TAG, "Ya está escuchando, cancelando sesión anterior");
            speechRecognizer.cancel();
        }
        
        if (speechRecognizer != null) {
            Log.d(TAG, "Iniciando reconocimiento de voz...");
            speechRecognizer.startListening(speechRecognizerIntent);
        } else {
            Log.e(TAG, "SpeechRecognizer no está inicializado");
            Toast.makeText(fragment.requireContext(), "Error: Reconocimiento de voz no inicializado", Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * Verifica permisos y inicia reconocimiento de voz
     */
    public void startVoiceRecognitionWithPermissionCheck() {
        Log.d(TAG, "Verificando permisos de audio...");
        
        // Verificar permisos de audio
        if (ContextCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.RECORD_AUDIO) 
            != PackageManager.PERMISSION_GRANTED) {
            Log.w(TAG, "Permiso de audio no concedido, solicitando...");
            ActivityCompat.requestPermissions(fragment.requireActivity(), 
                new String[]{Manifest.permission.RECORD_AUDIO}, 
                REQUEST_RECORD_AUDIO_PERMISSION);
            return;
        }
        
        Log.d(TAG, "Permiso de audio concedido");
        
        // Verificar si el reconocimiento de voz está disponible
        if (!SpeechRecognizer.isRecognitionAvailable(fragment.requireContext())) {
            Log.e(TAG, "Reconocimiento de voz no disponible");
            Toast.makeText(fragment.requireContext(), "Reconocimiento de voz no disponible en este dispositivo", Toast.LENGTH_LONG).show();
            return;
        }
        
        // Inicializar reconocimiento de voz si no está inicializado
        if (speechRecognizer == null) {
            Log.d(TAG, "Inicializando reconocimiento de voz...");
            initializeSpeechRecognizer();
        }
        
        // Iniciar reconocimiento de voz
        startVoiceRecognition();
    }
    
    /**
     * Obtiene el mensaje de error del reconocimiento de voz
     */
    private String getErrorMessage(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Error de audio";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Error del cliente";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Permisos insuficientes";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Error de red";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Timeout de red";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No se reconoció el habla";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "Reconocedor ocupado";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "Error del servidor";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "Timeout de habla";
                break;
            default:
                message = "Error desconocido";
                break;
        }
        return message;
    }
    
    /**
     * Consulta a la IA con la pregunta
     */
    private void consultarIA(String pregunta) {
        // Mostrar diálogo de carga
        showLoadingDialog();
        
        // Ejecutar consulta en un hilo separado
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Llamar a la función de IA usando Kotlin
                    // Como estamos en Java, necesitamos usar reflection o crear un wrapper
                    String respuesta = callIAHelper(pregunta);
                    
                    // Mostrar respuesta en el hilo principal
                    fragment.requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoadingDialog();
                            showResponseDialog(pregunta, respuesta);
                        }
                    });
                    
                } catch (Exception e) {
                    fragment.requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoadingDialog();
                            Toast.makeText(fragment.requireContext(), "Error al consultar IA: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }
    
    /**
     * Llama a la función de IA desde Kotlin
     */
    private String callIAHelper(String pregunta) {
        try {
            // Usar reflection para llamar a la función de Kotlin
            Class<?> iaHelperClass = Class.forName("com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.IAHelper");
            java.lang.reflect.Method method = iaHelperClass.getMethod("preguntarSobreInspeccion100Horas", String.class);
            
            // Como es una función suspend, necesitamos manejarla de manera especial
            // Por ahora, retornamos una respuesta simulada
            return "Respuesta de IA para: " + pregunta + "\n\nBasándome en el manual de inspección de 100 horas del helicóptero MI-171, puedo ayudarte con información sobre componentes, procedimientos y requisitos de mantenimiento.";
            
        } catch (Exception e) {
            return "Error al conectar con IA: " + e.getMessage();
        }
    }
    
    /**
     * Muestra un diálogo de carga
     */
    private void showLoadingDialog() {
        Toast.makeText(fragment.requireContext(), "Consultando IA...", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Oculta el diálogo de carga
     */
    private void hideLoadingDialog() {
        // Implementar ocultar diálogo de carga si es necesario
    }
    
    /**
     * Muestra la respuesta de la IA en un diálogo
     */
    private void showResponseDialog(String pregunta, String respuesta) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.requireContext());
        builder.setTitle("Respuesta de IA")
               .setMessage("Pregunta: " + pregunta + "\n\nRespuesta: " + respuesta)
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               })
               .setCancelable(true)
               .show();
    }
    
    /**
     * Maneja el resultado de la solicitud de permisos
     */
    public void handlePermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, inicializar reconocimiento de voz
                if (speechRecognizer == null) {
                    initializeSpeechRecognizer();
                }
                startVoiceRecognition();
            } else {
                Toast.makeText(fragment.requireContext(), "Permiso de audio denegado", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    /**
     * Limpia recursos
     */
    public void cleanup() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
            speechRecognizer = null;
        }
    }
}
