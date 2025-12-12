package com.example.smartlottery.service

import com.example.smartlottery.model.AiPrediction
import com.example.smartlottery.model.Draw
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("draw/latest")
    suspend fun getLatestDraw(@Query("code") code: String): Response<Draw>

    @GET("draw/history")
    suspend fun getDrawHistory(@Query("code") code: String): Response<List<Draw>>

    @POST("predict")
    suspend fun getAiPrediction(@Query("lottery_code") code: String): Response<AiPrediction>
}
