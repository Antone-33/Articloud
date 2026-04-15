package com.articloud.network

import com.articloud.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor (private val session: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = session.getToken()

        // Si no hay token, deja pasar la petición sin header (login/registro)
        val request = if (token != null) {
            original.newBuilder()
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .build()
        } else original

        return chain.proceed(request)
    }
}