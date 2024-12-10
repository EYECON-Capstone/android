package com.example.eyecon.data.photo.remote.retrofit

import com.example.eyecon.data.photo.remote.response.PhotoResponse
import com.example.eyecon.data.photo.remote.response.DetailResponse
import com.example.eyecon.data.photo.remote.response.HistoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    // Predict a Photo
    @Multipart
    @POST("/predict")
    suspend fun predictPhoto(
        @Part image: MultipartBody.Part,
        @Part("id_user") idUser: RequestBody
    ): PhotoResponse

    // Retrieve Diagnose History
    @GET("/predict/histories/{id_user}")
    suspend fun getDiagnoseHistory(
        @Path("id_user") idUser: String
    ): HistoryResponse

    // Retrieve Diagnose History Item
    @GET("/predict/{id}")
    suspend fun getDetailItem(
        @Path("id") id: String
    ): DetailResponse

    // Delete a History Item
    @DELETE("/predict/{id}")
    suspend fun deleteDiagnoseHistoryItem(
        @Path("id") id: String
    ): HistoryResponse
}
