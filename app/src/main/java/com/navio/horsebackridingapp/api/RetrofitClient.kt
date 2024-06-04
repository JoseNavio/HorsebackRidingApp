package com.navio.horsebackridingapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.Interceptor

object RetrofitClient {
    private const val BASE_URL = "https://josenavio.site/api/"

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    fun getInstance(context: Context): ApiService {

        val sharedPref = context.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(Interceptor { chain ->
                val original = chain.request()

                val requestBuilder = original.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .method(original.method, original.body)

                val request = requestBuilder.build()
                chain.proceed(request)
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(ApiService::class.java)
    }
}