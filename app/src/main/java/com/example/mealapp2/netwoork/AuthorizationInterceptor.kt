package com.example.mealapp2.netwoork

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-RapidAPI-Host", "https://www.themealdb.com/api/json/v1/1/")
            .method(chain.request().method(), chain.request().body())
            .build()
        return chain.proceed(request)
    }
}