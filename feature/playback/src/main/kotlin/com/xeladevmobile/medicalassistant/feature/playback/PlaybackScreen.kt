package com.xeladevmobile.medicalassistant.feature.playback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.linc.audiowaveform.AudioWaveform
import com.xeladevmobile.medicalassistant.core.designsystem.component.MedicalLoadingWheel
import com.xeladevmobile.medicalassistant.core.designsystem.icon.MedicalIcons
import com.xeladevmobile.medicalassistant.core.model.data.AudioDetails
import com.xeladevmobile.medicalassistant.core.model.data.Emotion
import com.xeladevmobile.medicalassistant.core.model.data.formattedDuration
import com.xeladevmobile.medicalassistant.core.model.data.formattedSize

@Composable
internal fun PlaybackScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: PlaybackViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    viewModel.setFilePath(context.cacheDir.absolutePath)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val audioDetails by viewModel.audioDetails.collectAsStateWithLifecycle()

    val amplitudes by viewModel.amplitudes.collectAsStateWithLifecycle()
    val playbackPosition by viewModel.playbackPosition.collectAsStateWithLifecycle()


    PlaybackScreen(
        modifier = modifier,
        uiState = uiState,
        onBackClick = onBackClick,
        onPlayPauseClick = viewModel::playPauseAudio,
        onStopClick = viewModel::stopAudio,
        amplitudes = amplitudes,
        playbackProgress = playbackPosition,
        onAnalyzeClick = {},
        audioDetails = audioDetails,
        isLoading = audioDetails == null,
    )
}

@Composable
internal fun PlaybackScreen(
    modifier: Modifier = Modifier,
    uiState: PlaybackUiState,
    onBackClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onStopClick: () -> Unit,
    onAnalyzeClick: () -> Unit,
    audioDetails: AudioDetails?,
    isLoading: Boolean,
    amplitudes: List<Int>,
    playbackProgress: Float,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

        PlaybackToolbar(onBackClick = onBackClick)

        // Displaying audio file details at the top inside a card
        if (isLoading) {
            MedicalLoadingWheel(contentDesc = stringResource(id = R.string.loading))
            Text(
                text = "We are analyzing your recording, please wait...",
                style = MaterialTheme.typography
                    .bodyMedium,
                modifier = Modifier.padding(16.dp),
            )
        } else {
            audioDetails?.let { details ->
                AudioFileDetailsCard(details)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Playback controls in the center
        PlaybackControls(
            playbackState = uiState,
            onPlayPauseClicked = onPlayPauseClick,
            onStopClicked = onStopClick,
            amplitudes = amplitudes,
            playbackProgress = playbackProgress,
        )

        Spacer(modifier = Modifier.weight(1f))

        // Bottom layout for analyzing the record
        AnalyzeAndEmotionLayout(onAnalyzeClicked = onAnalyzeClick)
    }
}

@Preview(showBackground = true)
@Composable
fun PlaybackScreenPreview() {
    PlaybackScreen(
        uiState = PlaybackUiState.Playing,
        onBackClick = {},
        onPlayPauseClick = {},
        onStopClick = {},
        onAnalyzeClick = {},
        audioDetails = AudioDetails(
            name = "recording_1",
            extension = "3gp",
            duration = 1000,
            quality = "Good",
            recordDate = "2021-09-01",
            size = 1000000,
        ),
        isLoading = false,
        amplitudes = listOf(10, 20, 15, 30, 25, 35, 40, 45, 30, 20, 10, 5),
        playbackProgress = 0f,
    )
}

@Composable
fun AnalyzeAndEmotionLayout(
    onAnalyzeClicked: () -> Unit,
    emotion: Emotion? = null, // Assuming Emotion is a data class you've defined elsewhere
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Button(
            onClick = onAnalyzeClicked,
            modifier = Modifier.padding(16.dp),
        ) {
            Text("Analyze Recording")
        }

        // The emotion display, which is only shown if there is emotion data available.
        emotion?.let {
            // Replace this Text with a fancier display for emotion as needed.
            Text(
                text = stringResource(R.string.emotion_detected, it.name),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}


@Composable
fun PlaybackControls(
    playbackState: PlaybackUiState,
    onPlayPauseClicked: () -> Unit,
    onStopClicked: () -> Unit,
    amplitudes: List<Int> = emptyList(),
    playbackProgress: Float,
) {
    Column {
        AudioWaveform(
            amplitudes = amplitudes,
            progress = playbackProgress,
            onProgressChange = {},
            progressBrush = SolidColor(Color.Magenta),
            waveformBrush = SolidColor(Color.LightGray),
            spikeWidth = 4.dp,
            spikePadding = 2.dp,
            spikeRadius = 4.dp,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            // Start/Pause Button
            Button(
                onClick = {
                    when (playbackState) {
                        PlaybackUiState.Paused, PlaybackUiState.Stopped, PlaybackUiState.Playing -> onPlayPauseClicked()
                        else -> Unit
                    }
                },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            ) {
                Icon(
                    imageVector =
                    if (playbackState is PlaybackUiState.Paused || playbackState is PlaybackUiState.Stopped)
                        Icons.Default.PlayArrow
                    else
                        Icons.Default.Pause,
                    contentDescription = "Start/Pause Recording",
                    modifier = Modifier.size(48.dp),
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onStopClicked, shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            ) {
                Icon(
                    imageVector = Icons.Default.Stop, contentDescription = "Stop/Save",
                    modifier = Modifier.size(48.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlaybackControlsPreview() {
    PlaybackControls(
        playbackState = PlaybackUiState.Playing,
        onPlayPauseClicked = {},
        onStopClicked = {},
        amplitudes = listOf(10, 20, 15, 30, 25, 35, 40, 45, 30, 20, 10, 5),
        playbackProgress = 0.5f,
    )
}

@Composable
fun AudioFileDetailsCard(details: AudioDetails) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Text(
                        text = stringResource(R.string.audio_file_details),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Text(
                        text = stringResource(R.string.audio_file_details_description),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.audio_name, details.name),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp),
                )

                Text(
                    text = stringResource(R.string.audio_extension, details.extension),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp),
                )

                Text(
                    text = stringResource(R.string.audio_duration, details.formattedDuration()),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp),
                )

                Text(
                    text = stringResource(R.string.audio_quality, details.quality),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp),
                )

                Text(
                    text = stringResource(R.string.audio_size, details.formattedSize()),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp),
                )

                Divider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                )

                Text(
                    text = stringResource(R.string.audio_record_date, details.recordDate),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    textAlign = TextAlign.End,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AudioFileDetailsCardPreview() {
    AudioFileDetailsCard(
        details = AudioDetails(
            name = "recording_1",
            extension = "3gp",
            duration = 1000,
            quality = "Good",
            recordDate = "2021-09-01",
            size = 1000,
        ),
    )
}

@Composable
private fun PlaybackToolbar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
    ) {
        IconButton(onClick = { onBackClick() }) {
            Icon(
                imageVector = MedicalIcons.ArrowBack,
                contentDescription = stringResource(
                    id = R.string.back,
                ),
            )
        }

        Text(stringResource(R.string.playback_audio), style = MaterialTheme.typography.headlineMedium)
    }
}