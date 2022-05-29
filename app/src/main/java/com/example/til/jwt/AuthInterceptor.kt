package com.example.til.jwt

import com.example.til.MainActivity
import com.squareup.okhttp.Interceptor
import com.squareup.okhttp.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var req =
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${MainActivity.prefs.token}" ?: "")
                .build()

        return chain.proceed(req)
    }
}