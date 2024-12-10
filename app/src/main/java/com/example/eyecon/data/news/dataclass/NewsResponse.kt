package com.example.eyecon.data.news.dataclass

data class NewsItem(
    val title: String,
    val publishedAt: String
)

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)

data class Article(
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
) {
    fun getReadTime(): String {
        // Estimasi waktu baca berdasarkan panjang konten
        val wordCount = content?.split(" ")?.size ?: 0
        val minutes = (wordCount / 200) + 1 // Asumsi kecepatan baca 200 kata/menit
        return "$minutes Min Read"
    }

    fun getPostedTime(): String {
        return "Posted 1 Week Ago" // Bisa diimplementasikan logika waktu yang lebih akurat
    }
}