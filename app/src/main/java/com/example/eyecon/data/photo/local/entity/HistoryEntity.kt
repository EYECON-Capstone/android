package com.example.eyecon.data.photo.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "history_table")
@Parcelize
data class HistoryEntity(
    @field:ColumnInfo("idUser")
    val idUser: String,

    @field:ColumnInfo("result")
    val result: String,

    @PrimaryKey
    @field:ColumnInfo("createdAt")
    val createdAt: String,

    @field:ColumnInfo("diagnosa")
    val diagnosa: String,

    @field:ColumnInfo("img_url")
    val imgUrl: String,

    @field:ColumnInfo("id")
    val id: String,


) : Parcelable