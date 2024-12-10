package com.example.eyecon.data.photo.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.eyecon.data.photo.local.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(historyEntity : HistoryEntity)

    @Update
    suspend fun updatePhoto(history : HistoryEntity)

    @Query("SELECT * FROM history_table WHERE idUser = :idUser ORDER BY createdAt DESC")
    fun getHistoryByUserId(idUser: String): LiveData<List<HistoryEntity>>

    @Query("DELETE FROM history_table WHERE idUser = :idUser")
    suspend fun deleteAll(idUser: String)

}