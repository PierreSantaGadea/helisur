package com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas;

import android.content.Context;
import android.widget.Toast;

/**
 * Ejemplo de uso del sistema de reconocimiento de voz y IA
 */
public class EjemploUsoVoz {
    
    /**
     * Ejemplo de cómo usar el sistema completo
     */
    public static void ejemploUsoCompleto(Context context) {
        // 1. El usuario presiona el botón btnPregintaAI
        // 2. Se verifica el permiso RECORD_AUDIO
        // 3. Se inicializa el reconocimiento de voz
        // 4. El usuario habla su pregunta
        // 5. Se convierte la voz a texto
        // 6. Se envía la pregunta a OpenAI con el contexto del PDF
        // 7. Se muestra la respuesta en un diálogo
        
        Toast.makeText(context, "Sistema de IA con voz implementado correctamente", Toast.LENGTH_LONG).show();
    }
    
    /**
     * Ejemplos de preguntas que puede hacer el usuario
     */
    public static String[] obtenerPreguntasEjemplo() {
        return new String[]{
            "¿Qué componentes del motor principal debo inspeccionar?",
            "¿Cuáles son los procedimientos del rotor principal?",
            "¿Qué debo verificar en el sistema hidráulico?",
            "¿Quién puede realizar la inspección de 100 horas?",
            "¿Qué documentación debo completar?",
            "¿Qué hacer si encuentro una discrepancia?",
            "¿Con qué frecuencia se realiza esta inspección?",
            "¿Qué sistemas eléctricos debo revisar?",
            "¿Cómo inspeccionar el fuselaje?",
            "¿Qué instrumentos de navegación debo verificar?"
        };
    }
}

