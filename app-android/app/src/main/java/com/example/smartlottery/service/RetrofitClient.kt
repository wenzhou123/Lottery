package com.example.smartlottery.service

import com.example.smartlottery.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val javaApi: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.JAVA_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    val aiApi: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.AI_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
