package com.xeladevmobile.medicalassistant.feature.voice

import android.media.AudioFormat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.squti.androidwaverecorder.WaveRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import javax.inject.Inject

@HiltViewModel
class VoiceViewModel @Inject constructor() : ViewModel() {
    private var mediaRecorder: WaveRecorder? = null

    private val _uiState = MutableStateFlow<VoiceUiState>(VoiceUiState.Stopped)
    val uiState: StateFlow<VoiceUiState> = _uiState.asStateFlow()

    private val _formattedDuration = MutableStateFlow("00:00")
    val formattedDuration: StateFlow<String> = _formattedDuration.asStateFlow()

    private var audioId: UUID = UUID.randomUUID()
    private var filePath: String? = null

    private var recordingDuration by mutableLongStateOf(0L)

    private var tickerJob: Job? = null

    fun startRecording(cacheFileDirectory: String) = viewModelScope.launch {
        filePath = "$cacheFileDirectory/recording_$audioId.wav"

        mediaRecorder = WaveRecorder(filePath!!).apply {
            waveConfig.sampleRate = 44100
            waveConfig.channels = AudioFormat.CHANNEL_IN_STEREO
            waveConfig.audioEncoding = AudioFormat.ENCODING_PCM_16BIT
        }
        mediaRecorder?.noiseSuppressorActive = true
        mediaRecorder?.startRecording()
        startTimer()
        _uiState.emit(VoiceUiState.Recording)
    }

    fun pauseRecording() = viewModelScope.launch {
        mediaRecorder?.pauseRecording()
        _uiState.emit(VoiceUiState.Paused)
        pauseTimer() // Pause the timer instead of stopping it
    }

    fun resumeRecording() = viewModelScope.launch {
        mediaRecorder?.resumeRecording()
        _uiState.emit(VoiceUiState.Recording)
        startTimer()
    }

    fun stopRecording() = viewModelScope.launch {
        mediaRecorder?.stopRecording()
        mediaRecorder = null
        _uiState.emit(VoiceUiState.RecordSuccess(audioId.toString()))
        stopTimer()
    }

    fun cancelRecording() {
        mediaRecorder?.stopRecording()
        mediaRecorder = null
        filePath?.let { File(it).delete() }
        stopTimer()
    }

    private fun startTimer() {
        tickerJob?.cancel() // Cancel any existing job before starting a new one
        tickerJob = viewModelScope.launch {
            while (isActive) { // Keep running while the coroutine is active
                delay(1000)
                recordingDuration += 1
                _formattedDuration.emit(getFormattedDuration())
            }
        }
    }

    // Pause the timer without resetting the recording duration
    private fun pauseTimer() {
        tickerJob?.cancel()
        tickerJob = null
    }

    private fun stopTimer() {
        tickerJob?.cancel()
        tickerJob = null
        recordingDuration = 0L
        viewModelScope.launch {
            _formattedDuration.emit(getFormattedDuration())
        }
    }

    // Call this in your composable to format the duration
    private fun getFormattedDuration(): String {
        val minutes = recordingDuration / 60
        val seconds = recordingDuration % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onCleared() {
        super.onCleared()

        // Stop the timer when the ViewModel is cleared
        stopTimer()
    }

    fun setAudioId(audioId: UUID) {
        this.audioId = audioId
    }
}