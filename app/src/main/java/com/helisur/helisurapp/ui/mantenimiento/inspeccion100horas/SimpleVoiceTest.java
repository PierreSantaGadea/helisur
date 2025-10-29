package com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

/**
 * Clase simple para probar el reconocimiento de voz
 */
public class SimpleVoiceTest {
    
    private static final String TAG = "SimpleVoiceTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int REQUEST_SPEECH_RECOGNITION = 201;
    
    /**
     * Método simple para iniciar reconocimiento de voz
     */
    public static void startSimpleVoiceRecognition(Activity activity) {
        Log.d(TAG, "Iniciando reconocimiento de voz simple...");
        
        // Verificar permisos
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) 
            != PackageManager.PERMISSION_GRANTED) {
            Log.w(TAG, "Solicitando permiso de audio...");
            ActivityCompat.requestPermissions(activity, 
                new String[]{Manifest.permission.RECORD_AUDIO}, 
                REQUEST_RECORD_AUDIO_PERMISSION);
            return;
        }
        
        // Verificar si está disponible
        if (!android.speech.SpeechRecognizer.isRecognitionAvailable(activity)) {
            Log.e(TAG, "Reconocimiento de voz no disponible");
            Toast.makeText(activity, "Reconocimiento de voz no disponible", Toast.LENGTH_LONG).show();
            return;
        }
        
        // Crear intent simple
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla tu pregunta...");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        
        try {
            Log.d(TAG, "Iniciando actividad de reconocimiento...");
            activity.startActivityForResult(intent, REQUEST_SPEECH_RECOGNITION);
        } catch (Exception e) {
            Log.e(TAG, "Error al iniciar reconocimiento: " + e.getMessage());
            Toast.makeText(activity, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * Maneja el resultado del reconocimiento de voz
     */
    public static void handleSpeechResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SPEECH_RECOGNITION) {
            Log.d(TAG, "Manejando resultado del reconocimiento...");
            Log.d(TAG, "Result code: " + resultCode);
            
            if (resultCode == Activity.RESULT_OK && data != null) {
                ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (results != null && !results.isEmpty()) {
                    String spokenText = results.get(0);
                    Log.d(TAG, "Texto reconocido: " + spokenText);
                    Toast.makeText(null, "Reconocido: " + spokenText, Toast.LENGTH_LONG).show();
                    
                    // Aquí puedes llamar a la IA
                    // consultarIA(spokenText);
                } else {
                    Log.e(TAG, "No se encontraron resultados");
                    Toast.makeText(null, "No se pudo reconocer el habla", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e(TAG, "Error en reconocimiento o datos nulos");
                Toast.makeText(null, "Error en reconocimiento de voz", Toast.LENGTH_LONG).show();
            }
        }
    }
}

