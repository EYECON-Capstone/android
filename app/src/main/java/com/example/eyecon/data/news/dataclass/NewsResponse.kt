package com.example.eyecon.data.news.dataclass

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
        val wordCount = content?.split(" ")?.size ?: 0
        val minutes = (wordCount / 100) + 1
        return "$minutes Min Read"
    }

    fun getPostedTime(): String {
        return "Posted 1 Week Ago"
    }
}