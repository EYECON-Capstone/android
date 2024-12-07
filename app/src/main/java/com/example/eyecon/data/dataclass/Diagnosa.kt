package com.example.eyecon.data.dataclass

data class Diagnosa(
    val id: Int,
    val title: String,
    val imageResId: Int,
    val subtitle: String,
    val description: String,
    val recommendations: List<Rekomendasi>
)

data class Rekomendasi(
    val icon: Int,
    val title: String
)
