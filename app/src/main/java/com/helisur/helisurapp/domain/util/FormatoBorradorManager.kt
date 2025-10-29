package com.helisur.helisurapp.domain.util

import android.content.Context
import android.content.SharedPreferences
import com.helisur.helisurapp.R
import com.helisur.helisurapp.domain.model.TareaFormato
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FormatoBorradorManager(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences("Borrador_formato", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        const val HAS_BORRADOR = "has_borrador"
        const val ID_AERONAVE = "id_aeronave"
        const val RTV = "rtv"
        const val ID_UBICACION = "id_ubicacion"
        const val EXISTEN_DISCREPANCIAS = "existen_discrepancias"
        const val RTV_DISCREPANCIAS = "rtv_discrepancias"
        const val ACCIONES_MANTENIMIENTO = "acciones_mantenimiento"
        const val SOLICITA_ENCENDIDO_PREVIO = "solicita_encendido_previo"
        const val TAREA_FORMATO = "tarea_formato"
        const val LISTA_TAREA_FORMATO = "lista_tarea_formato"

        const val TIPO_FORMATO = "tipo_formato"

        const val DESDE_BORRADOR = "desde_borrador"

    }

    fun saveTipoFormato(tipoFormato: String) {
        val editor = prefs.edit()
        editor.putString(TIPO_FORMATO, tipoFormato)
        editor.apply()
    }

    fun saveTareaFormato(tarea: TareaFormato) {
        val json = gson.toJson(tarea)
        prefs.edit().putString(TAREA_FORMATO, json).apply()
    }

    fun getTareaFormato(): TareaFormato? {
        val json = prefs.getString(TAREA_FORMATO, null)
        return json?.let {
            gson.fromJson(it, TareaFormato::class.java)
        }
    }

    fun saveListaTareaFormato(lista: List<TareaFormato>) {
        val json = gson.toJson(lista)
        prefs.edit().putString(LISTA_TAREA_FORMATO, json).apply()
    }

    fun getListaTareaFormato(): List<TareaFormato> {
        val json = prefs.getString(LISTA_TAREA_FORMATO, null)
        return if (json != null) {
            val type = object : TypeToken<List<TareaFormato>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun saveDesdeBorrador(desdeBorrador: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(DESDE_BORRADOR, desdeBorrador)
        editor.apply()
    }

    fun saveHasBorrador(tieneBorrador: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(HAS_BORRADOR, tieneBorrador)
        editor.apply()
    }

    fun saveIdAeronave(user: String) {
        val editor = prefs.edit()
        editor.putString(ID_AERONAVE, user)
        editor.apply()
    }

    fun saveRtv(pass: String) {
        val editor = prefs.edit()
        editor.putString(RTV, pass)
        editor.apply()
    }


    fun saveIdUbicacion(token: String) {
        val editor = prefs.edit()
        editor.putString(ID_UBICACION, token)
        editor.apply()
    }

    fun saveExistenDiscrepancias(tieneDiscrepancias: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(EXISTEN_DISCREPANCIAS, tieneDiscrepancias)
        editor.apply()
    }

    fun saveRtvDiscrepancias(nombres: String) {
        val editor = prefs.edit()
        editor.putString(RTV_DISCREPANCIAS, nombres)
        editor.apply()
    }

    fun saveAccionesMantenimiento(tieneAccionesMantenimiento: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(ACCIONES_MANTENIMIENTO, tieneAccionesMantenimiento)
        editor.apply()
    }

    fun saveSolicitaEncendidoPrevio(tieneSolicitaEncendidoPrevio: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(SOLICITA_ENCENDIDO_PREVIO, tieneSolicitaEncendidoPrevio)
        editor.apply()
    }


    fun getDesdeBorrador(): Boolean? {
        return prefs.getBoolean(DESDE_BORRADOR, false)
    }

    fun getTipoFormato(): String? {
        return prefs.getString(TIPO_FORMATO, "")
    }

    fun getHasBorrador(): Boolean? {
        return prefs.getBoolean(HAS_BORRADOR, false)
    }

    fun getIdAeronave(): String? {
        return prefs.getString(ID_AERONAVE, "")
    }

    fun getRtv(): String? {
        return prefs.getString(RTV, "")
    }

    fun getIdUbicacion(): String? {
        return prefs.getString(ID_UBICACION, "")
    }

    fun getExistenDiscrepancias(): Boolean? {
        return prefs.getBoolean(EXISTEN_DISCREPANCIAS, false)
    }

    fun getRtvDiscrepancias(): String? {
        return prefs.getString(RTV_DISCREPANCIAS, "")
    }

    fun getAccionesMantenimiento(): Boolean? {
        return prefs.getBoolean(ACCIONES_MANTENIMIENTO, false)
    }

    fun getEncendidoPrevioMotores(): Boolean? {
        return prefs.getBoolean(SOLICITA_ENCENDIDO_PREVIO, false)
    }


}