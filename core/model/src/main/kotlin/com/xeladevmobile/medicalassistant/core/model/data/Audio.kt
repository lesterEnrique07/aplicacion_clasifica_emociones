package com.xeladevmobile.medicalassistant.core.model.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Audio(
    val extension: String,
    val duration: Int,
    val path: String,
    val createdDate: Long,
    val emotion: Emotion,
)

enum class Emotion {
    Neutral, Angry, Happiness, Disgust, Fear
}

fun List<Audio>.groupByCreatedDate(): Map<String, List<Audio>> {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()) // Change the pattern if needed
    // Group by the formatted date string
    return this.groupBy { audio ->
        dateFormat.format(Date(audio.createdDate))
    }
}

fun List<Audio>.calculateStatistics(): List<PatientStatistics> {
    val emotionCount = this.groupingBy { it.emotion }.eachCount()
    val averageDurationPerEmotion = this.groupBy { it.emotion }
        .mapValues { (_, audios) ->
            audios.map { it.duration }.average()
        }
    val longestRecording = this.maxByOrNull { it.duration }
    val shortestRecording = this.minByOrNull { it.duration }

    // Assuming a function that calculates the trend of emotions over time
    val emotionTrend = calculateEmotionTrend(this)

    // Assuming a function that calculates the diversity of emotions
    val emotionDiversity = calculateEmotionDiversity(this)

    val emotionCountFormatted = emotionCount.entries.joinToString(separator = "\n") {
        "• ${it.key}: ${it.value}"
    }

    val averageDurationFormatted = averageDurationPerEmotion.entries.joinToString(separator = "\n") {
        "• ${it.key}: ${String.format("%.2f sec", it.value)}"
    }

    val longestRecordingFormatted = longestRecording?.let {
        "• Emotion: ${it.emotion}\n• Duration: ${it.duration} sec"
    } ?: "Not available"

    val shortestRecordingFormatted = shortestRecording?.let {
        "• Emotion: ${it.emotion}\n• Duration: ${it.duration} sec"
    } ?: "Not available"

    val mostCommonEmotion = emotionCount.maxByOrNull { it.value }
    val mostCommonEmotionContextualMessage = when (mostCommonEmotion?.key) {
        Emotion.Angry -> "Anger can be a powerful motivator. It's important to find healthy outlets for this energy."
        Emotion.Happiness -> "Happiness is contagious. Spread joy with your presence."
        Emotion.Disgust -> "Disgust can be a natural response to unpleasant experiences. Reflect on what can be learned from these feelings."
        Emotion.Fear -> "Fear can protect us, but it can also hinder us. Face your fears with courage and grow stronger."
        Emotion.Neutral -> "A calm sea does not make a skilled sailor. Embrace the waves of emotion."
        else -> "Emotions are complex, each one tells us something about ourselves."
    }

    return listOf(
        PatientStatistics(
            header = "Most Common Emotion",
            description = "The most frequently recorded emotion",
            value = "• Emotion: ${mostCommonEmotion?.key}\n• Total times: ${mostCommonEmotion?.value}\n\n$mostCommonEmotionContextualMessage",
            updatedAt = "2023-04-01",
        ),
        PatientStatistics(
            header = "Average Duration per Emotion",
            description = "Average duration of recordings for each emotion",
            value = averageDurationFormatted,
            updatedAt = "2023-04-01",
        ),
        PatientStatistics(
            header = "Distribution of Emotions",
            description = "Counts of each emotion recorded",
            value = emotionCountFormatted,
            updatedAt = "2023-04-01",
        ),
        PatientStatistics(
            header = "Longest Recording",
            description = "The longest audio recording and its emotion",
            value = longestRecordingFormatted,
            updatedAt = "2023-04-01",
        ),
        PatientStatistics(
            header = "Shortest Recording",
            description = "The shortest audio recording and its emotion",
            value = shortestRecordingFormatted,
            updatedAt = "2023-04-01",
        ),
        PatientStatistics(
            header = "Emotion Trend Over Time",
            description = "How emotions have varied over time",
            value = emotionTrend,
            updatedAt = "2023-04-01",
        ),
        PatientStatistics(
            header = "Emotion Diversity",
            description = "The diversity of emotions recorded",
            value = emotionDiversity,
            updatedAt = "2023-04-01",
        ),
        PatientStatistics(
            header = "Average Duration per Emotion",
            description = "The average duration of recordings for each emotion is listed below:",
            value = averageDurationFormatted,
            updatedAt = "2023-04-01",
        ),
        PatientStatistics(
            header = "Distribution of Emotions",
            description = "The count of each emotion recorded is as follows:",
            value = emotionCountFormatted,
            updatedAt = "2023-04-01",
        ),
    )
}

fun calculateEmotionTrend(audios: List<Audio>): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val emotionsByDate = audios.groupBy {
        dateFormat.format(Date(it.createdDate))
    }.mapValues { entry ->
        entry.value.groupingBy { it.emotion }.eachCount()
    }

    val trendStringBuilder = StringBuilder()
    emotionsByDate.toSortedMap().forEach { (date, emotions) ->
        trendStringBuilder.append("\n$date: ")
        emotions.entries.sortedByDescending { it.value }.forEach { (emotion, count) ->
            trendStringBuilder.append("$emotion($count) ")
        }
    }

    return trendStringBuilder.toString()
}


fun calculateEmotionDiversity(audios: List<Audio>): String {
    val emotionCounts = audios.groupingBy { it.emotion }.eachCount()
    val totalAudios = audios.count()

    return emotionCounts.entries.joinToString(separator = "\n") { (emotion, count) ->
        val percentage = (count.toDouble() / totalAudios) * 100
        "• $emotion: ${String.format("%.2f%%", percentage)}"
    }
}

val audiosPreview = listOf(
    Audio(
        extension = "mp3",
        duration = 433,
        path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
        createdDate = System.currentTimeMillis().minus(1000 * 60 * 60 * 24),
        emotion = Emotion.Angry,
    ),
    Audio(
        extension = "mp3",
        duration = 200,
        path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
        createdDate = System.currentTimeMillis().minus(2000 * 60 * 60 * 24),
        emotion = Emotion.Disgust,
    ),
    Audio(
        extension = "mp3",
        duration = 156,
        path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3",
        createdDate = System.currentTimeMillis().minus(3000 * 60 * 60 * 24),
        emotion = Emotion.Neutral,
    ),
    Audio(
        extension = "mp3",
        duration = 200,
        path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3",
        createdDate = System.currentTimeMillis().minus(4000 * 60 * 60 * 24),
        emotion = Emotion.Happiness,
    ),
    Audio(
        extension = "mp3",
        duration = 11,
        path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3",
        createdDate = System.currentTimeMillis().minus(5000 * 60 * 60 * 24),
        emotion = Emotion.Angry,
    ),
    Audio(
        extension = "mp3",
        duration = 12,
        path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3",
        createdDate = System.currentTimeMillis().minus(6000 * 60 * 60 * 24),
        emotion = Emotion.Fear,
    ),
    Audio(
        extension = "mp3",
        duration = 13,
        path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3",
        createdDate = System.currentTimeMillis().minus(1500 * 60 * 60 * 24),
        emotion = Emotion.Fear,
    ),
    Audio(
        extension = "mp3",
        duration = 14,
        path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-8.mp3",
        createdDate = System.currentTimeMillis().minus(2500 * 60 * 60 * 24),
        emotion = Emotion.Disgust,
    ),
    Audio(
        extension = "wav",
        duration = 1000,
        path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-9.mp3",
        createdDate = System.currentTimeMillis().minus(3500 * 60 * 60 * 24),
        emotion = Emotion.Angry,
    ),
)
