package com.xeladevmobile.medicalassistant.feature.records

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xeladevmobile.medicalassistant.core.formatCreatedDate
import com.xeladevmobile.medicalassistant.core.formatDuration
import com.xeladevmobile.medicalassistant.core.model.data.Audio
import com.xeladevmobile.medicalassistant.core.model.data.audiosPreview

@Composable
fun AudioRecordItem(
    audio: Audio,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = audio.path.substringAfterLast('/'),
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "Duration: ${formatDuration(audio.duration)}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "Created: ${formatCreatedDate(audio.createdDate)}",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            // Add additional UI elements if needed, for example, an icon to play the audio
        }
    }
}

@Preview
@Composable
fun AudioRecordItemPreview() {
    AudioRecordItem(
        audio = audiosPreview.first(),
        onClick = {},
    )
}