package com.example.eyecon.data.news.dataclass

data class ChatMessage(
    val message: String,
    val isBot: Boolean = false
)