package com.articloud

import android.content.Context

object SessionManager {

    fun saveUser(context: Context, name: String, email: String) {
        val prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean("isLogged", true)
            .putString("name", name)
            .putString("email", email)
            .apply()
    }

    fun isLogged(context: Context): Boolean {
        val prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        return prefs.getBoolean("isLogged", false)
    }

    fun getName(context: Context): String {
        val prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        return prefs.getString("name", "") ?: ""
    }

    fun logout(context: Context) {
            val prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
    fun getEmail(context: Context): String {
        val prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        return prefs.getString("email", "") ?: ""
    }


}