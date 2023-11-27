package com.xeladevmobile.medicalassistant.feature.home

import com.xeladevmobile.medicalassistant.core.model.data.Audio
import com.xeladevmobile.medicalassistant.core.model.data.Emotion
import com.xeladevmobile.medicalassistant.core.model.data.PatientStatistics
import java.text.SimpleDateFormat
import java.util.*

fun List<Audio>.calculateStatistics(locale: Locale): List<PatientStatistics> {
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
        "• ${it.key}: ${String.format(if (locale.language == "es") "%.2f seg" else "%.2f sec", it.value)}"
    }

    val longestRecordingFormatted = longestRecording?.let {
        "• ${if (locale.language == "es") "Emoción" else "Emotion"}: ${it.emotion}\n• ${if (locale.language == "es") "Duración" else "Duration"}: ${it.duration / 1000} ${if (locale.language == "es") "seg" else "sec"}"
    } ?: if (locale.language == "es") "No disponible" else "Not available"

    val shortestRecordingFormatted = shortestRecording?.let {
        "• ${if (locale.language == "es") "Emoción" else "Emotion"}: ${it.emotion}\n• ${if (locale.language == "es") "Duración" else "Duration"}: ${it.duration / 1000} ${if (locale.language == "es") "seg" else "sec"}"
    } ?: if (locale.language == "es") "No disponible" else "Not available"

    val mostCommonEmotion = emotionCount.maxByOrNull { it.value }
    val mostCommonEmotionContextualMessage = when (mostCommonEmotion?.key) {
        Emotion.Anger -> if (locale.language == "es") "La ira puede ser un poderoso motivador. Es importante encontrar salidas saludables para esta energía." else "Anger can be a powerful motivator. It's important to find healthy outlets for this energy."
        Emotion.Happiness -> if (locale.language == "es") "La felicidad es contagiosa. Comparte alegría con tu presencia." else "Happiness is contagious. Share joy with your presence."
        Emotion.Disgust -> if (locale.language == "es") "El asco puede ser una respuesta natural a experiencias desagradables. Reflexiona sobre lo que se puede aprender de estos sentimientos." else "Disgust can be a natural response to unpleasant experiences. Reflect on what can be learned from these feelings."
        Emotion.Fear -> if (locale.language == "es") "El miedo nos puede proteger, pero también puede obstaculizarnos. Enfréntate a tus miedos con valentía y crece más fuerte." else "Fear can protect us, but it can also hold us back. Face your fears with courage and grow stronger."
        Emotion.Neutral -> if (locale.language == "es") "Un mar en calma no hace un marinero hábil. Acepta las olas de emoción." else "A calm sea does not make a skilled sailor. Embrace the waves of emotion."
        Emotion.Sadness -> if (locale.language == "es") "La tristeza es una parte natural de la vida. Aprende a aceptarla y a dejarla ir." else "Sadness is a natural part of life. Learn to accept it and let it go."
        else -> if (locale.language == "es") "Las emociones son complejas, cada una nos dice algo sobre nosotros mismos." else "Emotions are complex, each one tells us something about ourselves."
    }

    return listOf(
        PatientStatistics(
            header = if (locale.language == "es") "Emoción Más Común" else "Most Common Emotion",
            description = if (locale.language == "es") "La emoción grabada con más frecuencia" else "The emotion recorded most frequently",
            value = "• Emoción: ${mostCommonEmotion?.key}\n• Total de veces: ${mostCommonEmotion?.value}\n\n$mostCommonEmotionContextualMessage",
            updatedAt = System.currentTimeMillis(),
        ),
        PatientStatistics(
            header = if (locale.language == "es") "Duración Promedio por Emoción" else "Average Duration per Emotion",
            description = if (locale.language == "es") "Duración promedio de las grabaciones para cada emoción" else "Average duration of recordings for each emotion",
            value = averageDurationFormatted,
            updatedAt = System.currentTimeMillis(),
        ),
        PatientStatistics(
            header = if (locale.language == "es") "Distribución de Emociones" else "Distribution of Emotions",
            description = if (locale.language == "es") "Cantidad de cada emoción grabada" else "Quantity of each recorded emotion",
            value = emotionCountFormatted,
            updatedAt = System.currentTimeMillis(),
        ),
        PatientStatistics(
            header = if (locale.language == "es") "Grabación Más Larga" else "Longest Recording",
            description = if (locale.language == "es") "La grabación de audio más larga y su emoción" else "The longest audio recording and its emotion",
            value = longestRecordingFormatted,
            updatedAt = System.currentTimeMillis(),
        ),
        PatientStatistics(
            header = if (locale.language == "es") "Grabación Más Corta" else "Shortest Recording",
            description = if (locale.language == "es") "La grabación de audio más corta y su emoción" else "The shortest audio recording and its emotion",
            value = shortestRecordingFormatted,
            updatedAt = System.currentTimeMillis(),
        ),
        PatientStatistics(
            header = if (locale.language == "es") "Tendencia de Emoción a lo Largo del Tiempo" else "Emotion Trend Over Time",
            description = if (locale.language == "es") "Cómo han variado las emociones a lo largo del tiempo" else "How emotions have varied over time",
            value = emotionTrend,
            updatedAt = System.currentTimeMillis(),
        ),
        PatientStatistics(
            header = if (locale.language == "es") "Diversidad de Emociones" else "Diversity of Emotions",
            description = if (locale.language == "es") "La diversidad de emociones grabadas" else "The diversity of recorded emotions",
            value = emotionDiversity,
            updatedAt = System.currentTimeMillis(),
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
