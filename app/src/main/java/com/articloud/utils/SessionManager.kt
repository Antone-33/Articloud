package com.articloud.utils

import android.content.Context
import android.content.SharedPreferences
import com.articloud.model.Usuarios

/**
 * Almacena el JWT y datos del usuario autenticado.
 * Se usa en TODAS las Activities protegidas.
 */

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("articloud_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOKEN = "jwt_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
    }

    // ── Guardar sesión tras login exitoso ──────────────────────
    fun saveSession(token: String, userId: Int, nombre: String, email: String) {
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putInt(KEY_USER_ID, userId)
            .putString(KEY_USER_NAME, nombre)
            .putString(KEY_USER_EMAIL, email)
            .apply()
    }

    // ── Getters ────────────────────────────────────────────────
    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)
    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, -1)
    fun getUserName(): String = prefs.getString(KEY_USER_NAME, "") ?: ""
    fun getUserEmail(): String = prefs.getString(KEY_USER_EMAIL, "") ?: ""
    fun isLoggedIn(): Boolean = getToken() != null

    // ── Cerrar sesión ──────────────────────────────────────────
    fun clearSession() = prefs.edit().clear().apply()

}
