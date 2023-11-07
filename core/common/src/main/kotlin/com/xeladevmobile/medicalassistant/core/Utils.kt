package com.xeladevmobile.medicalassistant.core

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Utility function to convert duration to a formatted string (e.g., "07:13")
fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds)
}

// Utility function to convert the creation date to a readable string
fun formatCreatedDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return formatter.format(Date(timestamp))
}