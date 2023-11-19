package com.xeladevmobile.medicalassistant.feature.voice

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.xeladevmobile.medicalassistant.core.designsystem.icon.MedicalIcons
import com.xeladevmobile.medicalassistant.core.designsystem.theme.MedicalTheme
import java.io.File
import java.util.UUID

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun VoiceScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: VoiceViewModel = hiltViewModel(),
    onRecordFinish: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formattedDuration by viewModel.formattedDuration.collectAsStateWithLifecycle()

    val context = LocalContext.current

    // Permission state
    val permissionState = rememberPermissionState(permission = android.Manifest.permission.RECORD_AUDIO)

    when (permissionState.status) {
        PermissionStatus.Granted -> {
            // Permission granted, show the recording UI
            VoiceScreen(
                modifier = modifier,
                uiState = uiState,
                onBackClick = onBackClick,
                formattedDuration = formattedDuration,
                onRecord = onRecordFinish,
                onStartRecording = { viewModel.startRecording(context.cacheDir.absolutePath) },
                onPause = { viewModel.pauseRecording() },
                onResume = { viewModel.resumeRecording() },
                onStop = { viewModel.stopRecording() },
                onCancel = { viewModel.cancelRecording() },
            )
        }

        else -> {
            MicrophonePermissionRequest(
                onPermissionRequest = { permissionState.launchPermissionRequest() },
                modifier = modifier,
                onBackClick = onBackClick,
            )
        }
    }
}

@Composable
fun MicrophonePermissionRequest(
    onPermissionRequest: () -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

        VoiceToolbar(onBackClick = onBackClick)

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = "Microphone",
            modifier = Modifier
                .size(48.dp)
                .animateMicPermissionIcon(),
            tint = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "To record your voice, we need access to your microphone.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onPermissionRequest) {
            Text("Grant Permission")
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun Modifier.animateMicPermissionIcon() = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "",
    )
    scale(scale)
}

@Preview(showBackground = true)
@Composable
fun MicrophonePermissionRequestPreview() {
    MedicalTheme {
        MicrophonePermissionRequest(onPermissionRequest = {}, onBackClick = {})
    }
}


@Composable
internal fun VoiceScreen(
    modifier: Modifier = Modifier,
    uiState: VoiceUiState,
    onBackClick: () -> Unit,
    formattedDuration: String,
    onRecord: (String) -> Unit,
    onStartRecording: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit,
    onCancel: () -> Unit,
) {
    when (uiState) {
        VoiceUiState.Paused, VoiceUiState.Recording, VoiceUiState.Stopped -> {
            // Do nothing we only are interested in the other states
        }

        is VoiceUiState.RecordSuccess -> {
            onRecord(uiState.audioId)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

        VoiceToolbar(onBackClick = onBackClick)

        Spacer(modifier = Modifier.weight(1f))

        // Recording title
        if (uiState == VoiceUiState.Paused) {
            Text("Press play to start recording", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Timer display
        Text(
            text = formattedDuration,
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Start/Pause Button
        Button(
            onClick = {
                when (uiState) {
                    VoiceUiState.Recording -> onPause()
                    VoiceUiState.Paused -> onResume()
                    VoiceUiState.Stopped -> onStartRecording()
                    else -> Unit
                }
            },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        ) {
            Icon(
                imageVector = if (uiState == VoiceUiState.Paused || uiState == VoiceUiState.Stopped) Icons.Default
                    .PlayArrow else Icons
                    .Default
                    .Pause,
                contentDescription = "Start/Pause Recording",
                modifier = Modifier.size(48.dp),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Stop and Cancel Buttons
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(
                onClick = onCancel, shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            ) {
                Icon(
                    imageVector = Icons.Default.Close, contentDescription = "Cancel",
                    modifier = Modifier.size(32.dp),
                )
            }

            Button(
                onClick = onStop, shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            ) {
                Icon(
                    imageVector = Icons.Default.Stop, contentDescription = "Stop/Save",
                    modifier = Modifier.size(32.dp),
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.2f))

        Text(
            "The stop recording button will automatically save your current recording.",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            style = MaterialTheme
                .typography
                .bodyMedium,
        )

        Spacer(modifier = Modifier.weight(1f))

        // Recording Animation
        AnimatedVisibility(visible = uiState == VoiceUiState.Recording) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RecordingAnimation()
                Spacer(modifier = Modifier.width(8.dp))
                Text("Recording...")
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun VoiceToolbar(
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

        Text(stringResource(R.string.record_your_voice), style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun RecordingAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "",
    )

    Box(
        modifier = Modifier
            .size(10.dp)
            .background(Color.Red.copy(alpha = alpha), CircleShape),
    )
}

@Preview(showBackground = true)
@Composable
private fun VoiceScreenPreview() {
    VoiceScreen(
        uiState = VoiceUiState.Paused,
        formattedDuration = "00:00",
        onRecord = {},
        onPause = {},
        onResume = {},
        onStartRecording = {},
        onStop = {},
        onCancel = {},
        onBackClick = {},
    )
}