package com.example.mealapp2.netwoork

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MealClient {
    private fun getOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .addNetworkInterceptor(getHttpLoggerInterceptor())
            .addInterceptor(AuthorizationInterceptor())
        return okHttpClientBuilder.build()
    }

    private fun getHttpLoggerInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/api/json/v1/1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(getOkHttpClient())
        .build()
    val mealService: MealService = retrofit.create(MealService::class.java)
}