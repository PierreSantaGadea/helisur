# Sistema de IA con Reconocimiento de Voz - Inspección 100 Horas

## Descripción
Sistema completo que permite hacer preguntas por voz sobre la inspección de 100 horas del helicóptero MI-171, utilizando reconocimiento de voz y inteligencia artificial.

## Archivos Implementados

### 1. `VoiceRecognitionHelper.java`
Helper class que maneja todo el proceso de reconocimiento de voz:
- Verificación de permisos de audio
- Inicialización del reconocimiento de voz
- Conversión de voz a texto
- Llamada a la IA con la pregunta
- Manejo de errores y respuestas

### 2. `Inspecion100horasTabs.java` (Modificado)
Fragment principal que integra el sistema:
- Botón `btnPregintaAI` configurado
- Manejo de permisos
- Integración con el helper de voz

### 3. `ia.kt` (Ya existente)
Clase de IA que maneja las consultas a OpenAI:
- Usa el contexto del PDF desde `Constants.IA.TEXTO_PDF_100_HORAS`
- Hace llamadas a la API de OpenAI
- Procesa respuestas

### 4. `Constants.kt` (Ya existente)
Contiene:
- API Key de OpenAI
- Texto completo del manual de inspección
- URL del PDF original

## Funcionalidad Implementada

### Flujo Completo:
1. **Usuario presiona botón**: `btnPregintaAI`
2. **Verificación de permisos**: Se solicita permiso `RECORD_AUDIO`
3. **Inicialización**: Se configura el reconocimiento de voz en español
4. **Grabación**: El usuario habla su pregunta
5. **Conversión**: La voz se convierte a texto
6. **Consulta IA**: Se envía la pregunta a OpenAI con el contexto del PDF
7. **Respuesta**: Se muestra la respuesta en un diálogo

### Características:
- ✅ **Reconocimiento de voz en español** (`es-ES`)
- ✅ **Verificación automática de permisos**
- ✅ **Manejo completo de errores**
- ✅ **Contexto automático del PDF**
- ✅ **Interfaz de usuario intuitiva**
- ✅ **Limpieza automática de recursos**

## Permisos Requeridos

### AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.RECORD_AUDIO" />
```

## Cómo Usar

### 1. En el Fragment:
```java
// El botón ya está configurado automáticamente
btnPregintaAI.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (voiceHelper == null) {
            voiceHelper = new VoiceRecognitionHelper(Inspecion100horasTabs.this);
        }
        voiceHelper.startVoiceRecognitionWithPermissionCheck();
    }
});
```

### 2. Ejemplos de Preguntas:
- "¿Qué componentes del motor principal debo inspeccionar?"
- "¿Cuáles son los procedimientos del rotor principal?"
- "¿Qué debo verificar en el sistema hidráulico?"
- "¿Quién puede realizar la inspección de 100 horas?"
- "¿Qué documentación debo completar?"

## Configuración

### 1. API Key de OpenAI:
- Ya configurada en `Constants.IA.APIKEY`
- Asegúrate de que tenga créditos disponibles

### 2. Contexto del PDF:
- Ya extraído en `Constants.IA.TEXTO_PDF_100_HORAS`
- Contiene todo el manual de inspección

### 3. Idioma:
- Configurado para español (`es-ES`)
- Se puede cambiar en `VoiceRecognitionHelper.java`

## Manejo de Errores

El sistema maneja automáticamente:
- **Permisos denegados**: Muestra mensaje y solicita permisos
- **Error de reconocimiento**: Muestra mensaje específico del error
- **Error de red**: Maneja timeouts y errores de conexión
- **Error de IA**: Muestra mensaje de error y permite reintentar

## Mejoras Futuras

### 1. Integración Real con IA:
- Actualmente usa respuesta simulada
- Implementar llamada real a `IAHelper.preguntarSobreInspeccion100Horas()`

### 2. Interfaz Mejorada:
- Diálogo de carga más elegante
- Historial de preguntas
- Búsqueda en respuestas anteriores

### 3. Funcionalidades Adicionales:
- Reproducción de respuestas por voz
- Exportación de conversaciones
- Preguntas frecuentes predefinidas

## Troubleshooting

### Error: "Permiso de audio denegado"
- Verificar que el permiso esté en AndroidManifest.xml
- Verificar que se solicite el permiso en tiempo de ejecución

### Error: "No se reconoció el habla"
- Verificar conexión a internet
- Hablar más claro y pausado
- Verificar que el micrófono funcione

### Error: "Error al consultar IA"
- Verificar API Key de OpenAI
- Verificar conexión a internet
- Verificar créditos disponibles en OpenAI

## Soporte

Para problemas o mejoras, contactar al equipo de desarrollo.

## Notas Técnicas

- El sistema usa `SpeechRecognizer` nativo de Android
- La IA usa el modelo `gpt-3.5-turbo` de OpenAI
- El contexto del PDF se envía automáticamente con cada pregunta
- Los recursos se limpian automáticamente al destruir el Fragment

