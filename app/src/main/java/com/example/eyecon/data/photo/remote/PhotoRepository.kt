package com.example.eyecon.data.photo.remote

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.eyecon.data.photo.local.entity.HistoryEntity
import com.example.eyecon.data.photo.local.room.HistoryDao
import com.example.eyecon.data.photo.remote.response.Data
import com.example.eyecon.data.photo.remote.response.DataItem
import com.example.eyecon.data.photo.remote.response.DetailResponse
import com.example.eyecon.data.photo.remote.response.PhotoResponse
import com.example.eyecon.data.photo.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

class PhotoRepository private constructor(
    private val apiService: ApiService,
    private val historyDao : HistoryDao
) {
    suspend fun insertHistory(history: HistoryEntity) {
        return withContext(Dispatchers.IO){
            historyDao.insertPhoto(history)
        }
    }
    suspend fun deleteHistory(idUser: String) {
        return withContext(Dispatchers.IO){
            historyDao.deleteAll(idUser)
        }
    }


    suspend fun predictphoto(
        image: MultipartBody.Part,
        idUser: RequestBody
    ): Result<DataItem> {
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.predictPhoto(image, idUser)
            }
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    fun getHistoryByUserId(idUser: String)=historyDao.getHistoryByUserId(idUser)

    suspend fun getDetailPhoto(id: String): Result<Data> {
        return try  {
            val response = withContext(Dispatchers.IO) {
                apiService.getDetailItem(id)
            }
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    companion object {
        @Volatile
        private var instance: PhotoRepository? = null
        fun getInstance(
            apiService: ApiService,
            historyDao: HistoryDao
        ): PhotoRepository =
            instance ?: synchronized(this) {
                instance ?: PhotoRepository(apiService, historyDao)
            }.also { instance = it }
    }
}