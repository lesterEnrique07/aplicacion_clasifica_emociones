package com.xeladevmobile.medicalassistant.core.model.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class Audio(
    val id: String,
    val extension: String,
    val duration: Int,
    val path: String,
    val createdDate: Long,
    val emotion: Emotion,
)

enum class Emotion {
    Neutral, Anger, Happiness, Disgust, Fear, Sadness
}

fun List<Audio>.groupByCreatedDate(): Map<String, List<Audio>> {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()) // Change the pattern if needed
    // Group by the formatted date string
    return this.groupBy { audio ->
        dateFormat.format(Date(audio.createdDate))
    }
}

val audiosPreview = listOf(
    Audio(
        extension = "mp3",
        duration = 433,
        path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
        createdDate = System.currentTimeMillis().minus(1000 * 60 * 60 * 24),
        emotion = Emotion.Anger,
        id = UUID.randomUUID().toString(),
    ),
    Audio(
        extension = "mp3",
        duration = 200,
        path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
        createdDate = System.currentTimeMillis().minus(2000 * 60 * 60 * 24),
        emotion = Emotion.Disgust,
        id = UUID.randomUUID().toString(),
    ),
    Audio(
        extension = "mp3",
        duration = 156,
        path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3",
        createdDate = System.currentTimeMillis().minus(3000 * 60 * 60 * 24),
        emotion = Emotion.Neutral,
        id = UUID.randomUUID().toString(),
    ),
    Audio(
        extension = "mp3",
        duration = 200,
        path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3",
        createdDate = System.currentTimeMillis().minus(4000 * 60 * 60 * 24),
        emotion = Emotion.Happiness,
        id = UUID.randomUUID().toString(),
    ),
    Audio(
        extension = "mp3",
        duration = 11,
        path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3",
        createdDate = System.currentTimeMillis().minus(5000 * 60 * 60 * 24),
        emotion = Emotion.Anger,
        id = UUID.randomUUID().toString(),
    ),
    Audio(
        extension = "mp3",
        duration = 12,
        path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3",
        createdDate = System.currentTimeMillis().minus(6000 * 60 * 60 * 24),
        emotion = Emotion.Fear,
        id = UUID.randomUUID().toString(),
    ),
    Audio(
        extension = "mp3",
        duration = 13,
        path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3",
        createdDate = System.currentTimeMillis().minus(1500 * 60 * 60 * 24),
        emotion = Emotion.Fear,
        id = UUID.randomUUID().toString(),
    ),
    Audio(
        extension = "mp3",
        duration = 14,
        path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-8.mp3",
        createdDate = System.currentTimeMillis().minus(2500 * 60 * 60 * 24),
        emotion = Emotion.Disgust,
        id = UUID.randomUUID().toString(),
    ),
    Audio(
        extension = "wav",
        duration = 1000,
        path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-9.mp3",
        createdDate = System.currentTimeMillis().minus(3500 * 60 * 60 * 24),
        emotion = Emotion.Anger,
        id = UUID.randomUUID().toString(),
    ),
)