package com.example.eyecon.data.photo.remote.response

import com.google.gson.annotations.SerializedName

data class PhotoResponse(

	@field:SerializedName("data")
	val data: DataItem,

	@field:SerializedName("status")
	val status: String
)

data class DataItem(

	@field:SerializedName("result")
	val result: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("diagnosa")
	val diagnosa: String,

	@field:SerializedName("img_url")
	val imgUrl: String,

	@field:SerializedName("id_user")
	val idUser: String,

	@field:SerializedName("id")
	val id: String
)
