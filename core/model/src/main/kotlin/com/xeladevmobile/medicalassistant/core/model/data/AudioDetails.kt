package com.xeladevmobile.medicalassistant.core.model.data

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class AudioDetails(
    val emotion: Emotion? = null,
    val name: String,
    val extension: String,
    val duration: Int,
    val quality: String,
    val recordDate: String,
    val size: Long,
)

// Use a SimpleDateFormat to parse the date and then format it
fun AudioDetails.formattedDate(): String {
    val parser = SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.getDefault())
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = try {
        parser.parse(recordDate)
    } catch (e: Exception) {
        null
    }
    return formatter.format(date ?: Calendar.getInstance().time)
}

fun AudioDetails.formattedDuration(): String {
    val minutes = duration / 1000 / 60
    val seconds = duration / 1000 % 60
    return String.format("%02d:%02d", minutes, seconds)
}

// If size is too big, it will be displayed in MB, otherwise in KB
fun AudioDetails.formattedSize(): String {
    return if (size > 1024 * 1024) {
        String.format("%.2f MB", size / (1024.0 * 1024.0))
    } else {
        String.format("%.2f KB", size / 1024.0)
    }
}
