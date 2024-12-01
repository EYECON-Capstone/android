package com.example.eyecon.data.api

import com.example.eyecon.data.dataclass.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/everything")
    suspend fun getNews(
        @Query("q") query: String = "mata",
        @Query("language") language: String = "id",
        @Query("apiKey") apiKey: String
    ): NewsResponse
}