package com.example.eyecon.data.photo.remote.retrofit

import com.example.eyecon.data.photo.remote.response.PhotoResponse
import com.example.eyecon.data.photo.remote.response.DetailResponse
import com.example.eyecon.data.photo.remote.response.HistoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @Multipart
    @POST("/predict")
    suspend fun predictPhoto(
        @Part image: MultipartBody.Part,
        @Part("id_user") idUser: RequestBody
    ): PhotoResponse

    @GET("/predict/{id}")
    suspend fun getDetailItem(
        @Path("id") id: String
    ): DetailResponse

}
