package com.example.eyecon.ui.addphoto

import android.content.Context
import com.example.eyecon.data.photo.local.room.HistoryDatabase
import com.example.eyecon.data.photo.remote.PhotoRepository
import com.example.eyecon.data.photo.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): PhotoRepository {
        val apiService = ApiConfig.getApiService()
        val db = HistoryDatabase.getDatabase(context)
        val dao = db.historyDao()
        return PhotoRepository.getInstance(apiService,dao)
    }
}