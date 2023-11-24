package com.xeladevmobile.medicalassistant.feature.playback

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.linc.audiowaveform.AudioWaveform
import com.xeladevmobile.medicalassistant.core.designsystem.component.MedicalButton
import com.xeladevmobile.medicalassistant.core.designsystem.component.MedicalLoadingWheel
import com.xeladevmobile.medicalassistant.core.designsystem.icon.MedicalIcons
import com.xeladevmobile.medicalassistant.core.model.data.AudioDetails
import com.xeladevmobile.medicalassistant.core.model.data.Emotion
import com.xeladevmobile.medicalassistant.core.model.data.formattedDate
import com.xeladevmobile.medicalassistant.core.model.data.formattedDuration
import com.xeladevmobile.medicalassistant.core.model.data.formattedSize

@Composable
internal fun PlaybackScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: PlaybackViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onFinishClick: () -> Unit,
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
        onAnalyzeClick = viewModel::analyzeAudio,
        onFinishClick = onFinishClick,
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
    onFinishClick: () -> Unit,
    audioDetails: AudioDetails?,
    isLoading: Boolean,
    amplitudes: List<Int>,
    playbackProgress: Float,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .scrollable(rememberScrollState(), orientation = Orientation.Vertical),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

        PlaybackToolbar(onBackClick = onBackClick)

        // Displaying audio file details at the top inside a card
        if (isLoading) {
            LoadingAnalysis()
        } else {
            audioDetails?.let { details ->
                AudioFileDetailsCard(uiState, details, onAnalyzeClick = onAnalyzeClick)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (uiState is PlaybackUiState.Analyzed) {
            MedicalButton(onClick = onFinishClick) {
                Text(text = stringResource(R.string.continue_))
            }
        } else {
            // Playback controls in the center
            PlaybackControls(
                playbackState = uiState,
                onPlayPauseClicked = onPlayPauseClick,
                onStopClicked = onStopClick,
                amplitudes = amplitudes,
                playbackProgress = playbackProgress,
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true, locale = "es")
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
            quality = "Buena",
            recordDate = "20230901T211833.000Z",
            size = 1000000,
        ),
        isLoading = false,
        amplitudes = listOf(10, 20, 15, 30, 25, 35, 40, 45, 30, 20, 10, 5),
        playbackProgress = 0f,
        onFinishClick = {},
    )
}

@Preview(showBackground = true)
@Composable
fun PlaybackScreenScrollablePreview() {
    PlaybackScreen(
        uiState = PlaybackUiState.Analyzed(Emotion.Anger),
        onBackClick = {},
        onPlayPauseClick = {},
        onStopClick = {},
        onAnalyzeClick = {},
        audioDetails = AudioDetails(
            name = "recording_1",
            extension = "3gp",
            duration = 1000,
            quality = "Good",
            recordDate = "20230901T211833.000Z",
            size = 1000000,
        ),
        isLoading = false,
        amplitudes = listOf(10, 20, 15, 30, 25, 35, 40, 45, 30, 20, 10, 5),
        playbackProgress = 0f,
        onFinishClick = {},
    )
}

@Composable
fun AnalyzeAndEmotionLayout(
    uiState: PlaybackUiState,
    onAnalyzeClicked: () -> Unit,
    emotion: Emotion? = null,
) {
    if (emotion != null && uiState !is PlaybackUiState.Analyzed && uiState !is PlaybackUiState.Loading) {
        AnalyzedEmotion(emotion)
    } else if (uiState !is PlaybackUiState.Analyzed && uiState !is PlaybackUiState.Loading) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(
                onClick = onAnalyzeClicked,
                modifier = Modifier.padding(16.dp),
            ) {
                Text(stringResource(R.string.analyze_recording))
            }
        }
    }

    when (uiState) {
        is PlaybackUiState.Analyzed -> {
            AnalyzedEmotion(uiState.result)
        }

        PlaybackUiState.Loading -> {
            LoadingAnalysis()
        }

        else -> {
            // Ignore
        }
    }
}

@Composable
private fun LoadingAnalysis() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MedicalLoadingWheel(contentDesc = stringResource(id = R.string.loading))
        Text(
            text = stringResource(R.string.analyzing_audio),
            style = MaterialTheme.typography
                .bodyMedium,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Composable
private fun AnalyzedEmotion(emotion: Emotion) {
    // Define a color based on the emotion
    val backgroundColor = colorForEmotion(emotion)
    // Define an emoji based on the emotion
    val emoji = emojiForEmotion(emotion)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
        ), // Set the background color based on emotion
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Column {
                Text(text = "Emotion", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = emotion.name,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            // Add animated emoji based on the emotion
            AnimatedEmoji(emoji = emoji)
        }
    }
}

@Composable
fun AnimatedEmoji(emoji: String) {
    val animated = remember { Animatable(initialValue = 0f) }

    // This will run the animation when this composable enters the Composition
    LaunchedEffect(true) {
        animated.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing),
        )
    }

    Text(
        text = emoji,
        modifier = Modifier
            .graphicsLayer {
                val scale = lerp(0.8f, 1f, animated.value)
                scaleX = scale
                scaleY = scale
                alpha = animated.value
            },
        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp),
    )
}

@Composable
fun PlaybackControls(
    playbackState: PlaybackUiState,
    onPlayPauseClicked: () -> Unit,
    onStopClicked: () -> Unit,
    amplitudes: List<Int> = emptyList(),
    playbackProgress: Float,
) {
    if (playbackState is PlaybackUiState.Loading) {
        MedicalLoadingWheel(contentDesc = stringResource(id = R.string.loading))
        Text(
            text = "We are analyzing your recording, please wait...",
            style = MaterialTheme.typography
                .bodyMedium,
            modifier = Modifier.padding(16.dp),
        )
        return
    } else {
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
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
fun AudioFileDetailsCard(
    uiState: PlaybackUiState, details: AudioDetails,
    onAnalyzeClick: () -> Unit,
) {
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

                AnalyzeAndEmotionLayout(uiState, onAnalyzeClicked = onAnalyzeClick, emotion = details.emotion)

                Text(
                    text = stringResource(R.string.audio_record_date, details.formattedDate()),
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier
                        .fillMaxWidth(),
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
            recordDate = "20230901T211833.000Z",
            size = 1000,
        ),
        onAnalyzeClick = {},
        uiState = PlaybackUiState.Stopped,
    )
}

@Preview(showBackground = true)
@Composable
fun AudioFileDetailsCardWithEmotionPreview() {
    AudioFileDetailsCard(
        details = AudioDetails(
            name = "recording_1",
            extension = "3gp",
            duration = 1000,
            quality = "Buena",
            recordDate = "20230901T211833.000Z",
            size = 1000,
        ),
        onAnalyzeClick = {},
        uiState = PlaybackUiState.Stopped,
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

// Helper function to get a color based on the emotion
fun colorForEmotion(emotion: Emotion): Color {
    return when (emotion) {
        Emotion.Neutral -> Color(0xFF8D8989)
        Emotion.Anger -> Color(0xFFE53935) // A darker red for better contrast
        Emotion.Happiness -> Color(0xFF968022) // A golden shade for better contrast
        Emotion.Disgust -> Color(0xFF4CAF50) // A darker green for better contrast
        Emotion.Fear -> Color(0xFFC542DB) // A darker purple for better contrast
        Emotion.Sadness -> Color(0xFF2196F3) // A darker blue for better contrast
    }
}

// Helper function to get an emoji based on the emotion
fun emojiForEmotion(emotion: Emotion): String {
    return when (emotion) {
        Emotion.Neutral -> "😐"
        Emotion.Anger -> "😠"
        Emotion.Happiness -> "😄"
        Emotion.Disgust -> "🤢"
        Emotion.Fear -> "😨"
        Emotion.Sadness -> "😢"
    }
}