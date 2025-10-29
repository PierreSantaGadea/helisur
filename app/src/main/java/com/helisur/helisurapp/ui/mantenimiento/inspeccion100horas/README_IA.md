# Sistema de IA para Inspección de 100 Horas - MI-171

## Descripción
Este sistema permite hacer consultas sobre la inspección de 100 horas del helicóptero MI-171 utilizando inteligencia artificial (OpenAI) con el contexto del manual de inspección.

## Archivos Principales

### 1. `ia.kt` - Clase principal IAHelper
Contiene todas las funciones para interactuar con OpenAI:
- `descargarYExtraerTextoPDF()`: Descarga y extrae texto del PDF
- `consultarOpenAI()`: Hace llamadas a la API de OpenAI
- `realizarConsultaIA()`: Función principal para consultas
- `preguntarSobreInspeccion100Horas()`: Función simplificada para consultas

### 2. `Constants.kt` - Constantes del sistema
- `Constants.IA.APIKEY`: API Key de OpenAI
- `Constants.IA.URL_PDF_100_HORAS`: URL del PDF de inspección
- `Constants.IA.TEXTO_PDF_100_HORAS`: Texto extraído del PDF

### 3. `EjemploUsoIA.kt` - Ejemplos de uso
Contiene ejemplos prácticos de cómo usar el sistema.

## Cómo Usar

### Uso Básico
```kotlin
// En un Fragment o Activity
CoroutineScope(Dispatchers.Main).launch {
    val pregunta = "¿Qué componentes del motor principal debo inspeccionar?"
    val respuesta = IAHelper.preguntarSobreInspeccion100Horas(pregunta)
    
    // Mostrar la respuesta en la UI
    textViewRespuesta.text = respuesta
}
```

### Uso con Callback
```kotlin
EjemploUsoIA.hacerConsultaPersonalizada("¿Cuáles son los procedimientos de inspección?") { respuesta ->
    // Actualizar UI con la respuesta
    runOnUiThread {
        textViewRespuesta.text = respuesta
    }
}
```

### Preguntas Frecuentes
```kotlin
val preguntasFrecuentes = EjemploUsoIA.obtenerPreguntasFrecuentes()
// Mostrar lista de preguntas al usuario
```

## Funcionalidades

### 1. Consultas Especializadas
El sistema está configurado para responder preguntas específicas sobre:
- Componentes a inspeccionar
- Procedimientos de inspección
- Requisitos de personal
- Documentación requerida
- Manejo de discrepancias

### 2. Contexto Automático
Cada consulta incluye automáticamente el contexto completo del manual de inspección de 100 horas.

### 3. Respuestas Precisas
El sistema está configurado para:
- Responder solo basándose en el contexto del PDF
- Indicar claramente si no puede responder una pregunta
- Proporcionar información técnica precisa

## Configuración

### API Key de OpenAI
La API Key está configurada en `Constants.IA.APIKEY`. Asegúrate de que sea válida y tenga créditos disponibles.

### Modelo de IA
El sistema usa `gpt-3.5-turbo` por defecto. Puedes cambiarlo en la función `consultarOpenAI()`.

### Parámetros de la Consulta
- `max_tokens`: 1000 (ajustable según necesidades)
- `temperature`: 0.7 (balance entre creatividad y precisión)

## Manejo de Errores

El sistema maneja automáticamente:
- Errores de conexión a OpenAI
- Errores de descarga del PDF
- Errores de extracción de texto
- Timeouts de red

## Mejoras Futuras

### 1. Extracción Real de PDF
Actualmente usa texto de ejemplo. Para producción, implementar:
- Librería iText o PDFBox para extracción real
- Procesamiento de imágenes en el PDF
- Manejo de tablas y formatos complejos

### 2. Cache de Respuestas
- Guardar respuestas frecuentes localmente
- Reducir llamadas a la API
- Mejorar velocidad de respuesta

### 3. Interfaz de Usuario
- Chat interface para conversaciones
- Historial de consultas
- Búsqueda en respuestas anteriores

## Consideraciones de Seguridad

1. **API Key**: Mantener segura la API Key de OpenAI
2. **Datos Sensibles**: No incluir información confidencial en las consultas
3. **Validación**: Validar respuestas antes de mostrarlas al usuario
4. **Rate Limiting**: Implementar límites de consultas por usuario

## Troubleshooting

### Error: "No se encontró el contexto del PDF"
- Verificar que `Constants.IA.TEXTO_PDF_100_HORAS` no esté vacío
- Revisar la extracción del texto del PDF

### Error: "Error al consultar OpenAI"
- Verificar API Key válida
- Verificar conexión a internet
- Verificar créditos disponibles en OpenAI

### Respuestas Genéricas
- Ajustar el prompt en `consultarOpenAI()`
- Verificar que el contexto del PDF sea relevante
- Ajustar parámetros de temperatura y max_tokens

## Soporte

Para problemas o mejoras, contactar al equipo de desarrollo.

