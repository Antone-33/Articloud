package com.articloud

import android.content.Context

object SessionManager {

    private const val PREFS = "user"

    fun saveUser(context: Context, name: String, email: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putBoolean("isLogged", true)
            .putString("name", name)
            .putString("email", email)
            .apply()
    }

    fun saveProfile(
        context: Context,
        phone: String,
        dni: String,
        address: String,
        city: String,
        bio: String
    ) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putString("phone", phone)
            .putString("dni", dni)
            .putString("address", address)
            .putString("city", city)
            .putString("bio", bio)
            .apply()
    }

    fun isLogged(context: Context): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getBoolean("isLogged", false)

    fun getName(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString("name", "") ?: ""

    fun getEmail(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString("email", "") ?: ""

    fun getPhone(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString("phone", "") ?: ""

    fun getDni(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString("dni", "") ?: ""

    fun getAddress(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString("address", "") ?: ""

    fun getCity(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString("city", "") ?: ""

    fun getBio(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString("bio", "") ?: ""

    fun logout(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().clear().apply()
    }
}