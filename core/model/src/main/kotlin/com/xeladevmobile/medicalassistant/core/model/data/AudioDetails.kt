package com.xeladevmobile.medicalassistant.core.model.data

data class AudioDetails(
    val name: String,
    val extension: String,
    val duration: Long,
    val quality: String,
    val recordDate: String,
    val size: Long,
)

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
