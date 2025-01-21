package com.helisur.helisurapp.domain.util

import android.content.Context
import android.content.SharedPreferences
import com.helisur.helisurapp.R

class SessionUserManager(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER = "user"
        const val PASS = "pass"
        const val USER_TOKEN = "user_token"
        const val USER_ID = "user_id"
        const val USER_NOMBRES = "user_nombres"
        const val USER_APELLIDO_PATERNO = "user_apellido_paterno"
        const val USER_APELLIDO_MATERNO = "user_apellido_materno"
        const val USER_ROL = "user_rol"
        const val USER_LOGGED = "user_logged"
        const val USER_CURRENT_TAB_POSITION = "user_current_tab_position"
    }

    fun saveUser(user: String) {
        val editor = prefs.edit()
        editor.putString(USER, user)
        editor.apply()
    }

    fun savePass(pass: String) {
        val editor = prefs.edit()
        editor.putString(PASS, pass)
        editor.apply()
    }


    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun saveUserId(id: String) {
        val editor = prefs.edit()
        editor.putString(USER_ID, id)
        editor.apply()
    }

    fun saveUserNombres(nombres: String) {
        val editor = prefs.edit()
        editor.putString(USER_NOMBRES, nombres)
        editor.apply()
    }

    fun saveUserApellidoPaterno(apellidoPaterno: String) {
        val editor = prefs.edit()
        editor.putString(USER_APELLIDO_PATERNO, apellidoPaterno)
        editor.apply()
    }

    fun saveUserApellidoMaterno(apellidoMaterno: String) {
        val editor = prefs.edit()
        editor.putString(USER_APELLIDO_MATERNO, apellidoMaterno)
        editor.apply()
    }

    fun saveUserRol(rol: String) {
        val editor = prefs.edit()
        editor.putString(USER_ROL, rol)
        editor.apply()
    }

    fun saveUserLogged(logged: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(USER_LOGGED, logged)
        editor.apply()
    }

    fun saveUserCurrentTabPosition(tab: Int) {
        val editor = prefs.edit()
        editor.putInt(USER_CURRENT_TAB_POSITION, tab)
        editor.apply()
    }

    fun getUser(): String? {
        return prefs.getString(USER, null)
    }

    fun getPass(): String? {
        return prefs.getString(PASS, null)
    }

    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun getId(): String? {
        return prefs.getString(USER_ID, null)
    }

    fun getNombres(): String? {
        return prefs.getString(USER_NOMBRES, null)
    }

    fun getApellidoPaterno(): String? {
        return prefs.getString(USER_APELLIDO_PATERNO, null)
    }

    fun getApellidoMaterno(): String? {
        return prefs.getString(USER_APELLIDO_MATERNO, null)
    }

    fun getRol(): String? {
        return prefs.getString(USER_ROL, null)
    }

    fun getLogged(): Boolean? {
        return prefs.getBoolean(USER_LOGGED, false)
    }

    fun getUserCurrentTabPosition(): Int? {
        return prefs.getInt(USER_CURRENT_TAB_POSITION, Constants.TABS_VENDEDOR.TAB_INICIAL)
    }
}