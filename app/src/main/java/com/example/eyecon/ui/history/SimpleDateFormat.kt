package com.example.eyecon.ui.history

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun formatDate(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")

    val outputFormat = SimpleDateFormat("dd MMMM yyyy hh:mm a", Locale.getDefault())
    outputFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")

    try {
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    } catch (e: Exception) {
        return dateString
    }
}