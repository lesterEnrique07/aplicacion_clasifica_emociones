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

fun List<Audio>.calculateStatistics(): List<PatientStatistics> {
    val emotionCount = this.groupingBy { it.emotion }.eachCount()
    val averageDurationPerEmotion = this.groupBy { it.emotion }
        .mapValues { (_, audios) ->
            audios.map { it.duration / 1000.0 }.average() // Convert milliseconds to seconds
        }
    val longestRecording = this.maxByOrNull { it.duration }
    val shortestRecording = this.minByOrNull { it.duration }

    val emotionTrend = calculateEmotionTrend(this)
    val emotionDiversity = calculateEmotionDiversity(this)

    val emotionCountFormatted = emotionCount.entries.joinToString(separator = "\n") {
        "• ${it.key}: ${it.value}"
    }

    val averageDurationFormatted = averageDurationPerEmotion.entries.joinToString(separator = "\n") {
        "• ${it.key}: ${String.format("%.2f seg", it.value)}"
    }

    val longestRecordingFormatted = longestRecording?.let {
        "• Emoción: ${it.emotion}\n• Duración: ${it.duration / 1000} seg"
    } ?: "No disponible"

    val shortestRecordingFormatted = shortestRecording?.let {
        "• Emoción: ${it.emotion}\n• Duración: ${it.duration / 1000} seg"
    } ?: "No disponible"

    val mostCommonEmotion = emotionCount.maxByOrNull { it.value }
    val mostCommonEmotionContextualMessage = when (mostCommonEmotion?.key) {
        Emotion.Anger -> "La ira puede ser un poderoso motivador. Es importante encontrar salidas saludables para esta energía."
        Emotion.Happiness -> "La felicidad es contagiosa. Comparte alegría con tu presencia."
        Emotion.Disgust -> "El asco puede ser una respuesta natural a experiencias desagradables. Reflexiona sobre lo que se puede aprender de estos sentimientos."
        Emotion.Fear -> "El miedo nos puede proteger, pero también puede obstaculizarnos. Enfréntate a tus miedos con valentía y crece más fuerte."
        Emotion.Neutral -> "Un mar en calma no hace un marinero hábil. Acepta las olas de emoción."
        Emotion.Sadness -> "La tristeza es una parte natural de la vida. Aprende a aceptarla y a dejarla ir."
        else -> "Las emociones son complejas, cada una nos dice algo sobre nosotros mismos."
    }

    return listOf(
        PatientStatistics(
            header = "Emoción Más Común",
            description = "La emoción grabada con más frecuencia",
            value = "• Emoción: ${mostCommonEmotion?.key}\n• Total de veces: ${mostCommonEmotion?.value}\n\n$mostCommonEmotionContextualMessage",
            updatedAt = "2023-11-20",
        ),
        PatientStatistics(
            header = "Duración Promedio por Emoción",
            description = "Duración promedio de las grabaciones para cada emoción",
            value = averageDurationFormatted,
            updatedAt = "2023-11-20",
        ),
        PatientStatistics(
            header = "Distribución de Emociones",
            description = "Cantidad de cada emoción grabada",
            value = emotionCountFormatted,
            updatedAt = "2023-11-20",
        ),
        PatientStatistics(
            header = "Grabación Más Larga",
            description = "La grabación de audio más larga y su emoción",
            value = longestRecordingFormatted,
            updatedAt = "2023-11-20",
        ),
        PatientStatistics(
            header = "Grabación Más Corta",
            description = "La grabación de audio más corta y su emoción",
            value = shortestRecordingFormatted,
            updatedAt = "2023-11-20",
        ),
        PatientStatistics(
            header = "Tendencia de Emoción a lo Largo del Tiempo",
            description = "Cómo han variado las emociones a lo largo del tiempo",
            value = emotionTrend,
            updatedAt = "2023-11-20",
        ),
        PatientStatistics(
            header = "Diversidad de Emociones",
            description = "La diversidad de emociones grabadas",
            value = emotionDiversity,
            updatedAt = "2023-11-20",
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
