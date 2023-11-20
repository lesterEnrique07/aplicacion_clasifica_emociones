package com.xeladevmobile.medicalassistant.feature.playback

import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeladevmobile.medicalassistant.core.data.repository.AnalyzeAudioRepository
import com.xeladevmobile.medicalassistant.core.data.repository.UserDataRepository
import com.xeladevmobile.medicalassistant.core.data.util.AudioManager
import com.xeladevmobile.medicalassistant.core.domain.GetAudioRecordUseCase
import com.xeladevmobile.medicalassistant.core.domain.InsertAudioUseCase
import com.xeladevmobile.medicalassistant.core.model.data.AudioDetails
import com.xeladevmobile.medicalassistant.feature.playback.navigation.PlaybackArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PlaybackViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val audioManager: AudioManager,
    private val audioAnalyzeAudioRepository: AnalyzeAudioRepository,
    private val insertAudioUseCase: InsertAudioUseCase,
    private val userDataRepository: UserDataRepository,
    private val getAudioRecordsUseCase: GetAudioRecordUseCase,
) : ViewModel() {
    private val playbackArgs: PlaybackArgs = PlaybackArgs(savedStateHandle)

    private val audioId = playbackArgs.audioId
    private var filePath: String = ""

    private var mediaPlayer: MediaPlayer? = null

    private val _uiState = MutableStateFlow<PlaybackUiState>(PlaybackUiState.Stopped)
    val uiState = _uiState.asStateFlow()

    private val _audioDetails = MutableStateFlow<AudioDetails?>(null)
    val audioDetails: StateFlow<AudioDetails?> = _audioDetails.asStateFlow()

    private val _amplitudes = MutableStateFlow<List<Int>>(emptyList())
    val amplitudes: StateFlow<List<Int>> = _amplitudes.asStateFlow()

    private val _playbackPosition = MutableStateFlow(0f)
    val playbackPosition: StateFlow<Float> = _playbackPosition.asStateFlow()

    private val handler = Handler(Looper.getMainLooper())
    private val playbackPositionUpdater = object : Runnable {
        override fun run() {
            val progress = updatePlaybackPosition()
            Log.d("Playback", "playbackPosition: $progress")
            viewModelScope.launch { _playbackPosition.emit(progress) }
            if (mediaPlayer?.isPlaying == true) {
                handler.postDelayed(this, 100) // Update every 100 ms
            }
        }
    }

    private fun startPlaybackPositionTracking() {
        handler.post(playbackPositionUpdater)
    }

    private fun stopPlaybackPositionTracking() {
        handler.removeCallbacks(playbackPositionUpdater)
    }

    fun setFilePath(cachePath: String) {
        filePath = "$cachePath/recording_$audioId.wav"

        viewModelScope.launch {
            val result = getAudioRecordsUseCase(audioId).firstOrNull()
            if (result != null) {
                val audioDetails = getAudioDetails(result.path).copy(emotion = result.emotion)
                _audioDetails.emit(audioDetails)
            } else {
                _audioDetails.emit(getAudioDetails(filePath))
            }
            loadAudioAmplitudes(filePath)
        }
    }

    fun playPauseAudio() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(filePath)

                setOnCompletionListener {
                    viewModelScope.launch {
                        stopAudio()
                        _playbackPosition.emit(0f) // Reset the playback position
                    }
                }

                setOnPreparedListener {
                    start()
                    startPlaybackPositionTracking()
                    Log.d("PreparedListener", "playPauseAudio: Prepared")
                    viewModelScope.launch {
                        _uiState.emit(PlaybackUiState.Playing)
                    }
                }

                prepareAsync()
            }
        } else {
            mediaPlayer?.let {
                if (_uiState.value.isPlaying) {
                    it.pause()
                    viewModelScope.launch {
                        _uiState.emit(PlaybackUiState.Paused)
                    }
                    stopPlaybackPositionTracking()
                } else {
                    it.start()
                    startPlaybackPositionTracking()
                    viewModelScope.launch {
                        _uiState.emit(PlaybackUiState.Playing)
                    }
                }
            }
        }
    }

    fun stopAudio() {
        mediaPlayer?.let {
            if (_uiState.value.isPlaying || _uiState.value.isPaused) {
                it.stop()
                it.reset()
                it.release()
                viewModelScope.launch {
                    _uiState.emit(PlaybackUiState.Stopped)
                    _playbackPosition.emit(0f) // Reset the playback position
                }
                stopPlaybackPositionTracking()
            }
        }

        mediaPlayer = null
    }

    private fun getAudioDetails(filePath: String): AudioDetails {
        val file = File(filePath)
        val retriever = MediaMetadataRetriever()

        retriever.setDataSource(filePath)

        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toIntOrNull() ?: 0
        val quality = retrieveQuality(retriever)
        val recordDate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE) ?: ""
        val size = file.length()
        val name = file.nameWithoutExtension
        val extension = file.extension

        retriever.release()

        return AudioDetails(
            name = name,
            extension = extension,
            duration = duration,
            quality = quality,
            recordDate = recordDate,
            size = size,
        )
    }

    // This is a placeholder. Audio quality is not directly available as metadata.
    // You may need to define your own criteria for 'quality' based on bitrate or other available metadata.
    private fun retrieveQuality(retriever: MediaMetadataRetriever): String {
        val bitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)?.toIntOrNull()
        return when {
            bitrate == null -> "Unknown"
            bitrate > 256000 -> "High"
            bitrate > 128000 -> "Medium"
            else -> "Low"
        }
    }

    private suspend fun loadAudioAmplitudes(filePath: String) {
        try {
            val amplitudes = audioManager.getAmplitudes(filePath)
            _amplitudes.emit(amplitudes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updatePlaybackPosition(): Float {
        val currentPosition = mediaPlayer?.currentPosition ?: 0
        val totalDuration = mediaPlayer?.duration?.toFloat() ?: 1f
        return (currentPosition / totalDuration).coerceIn(0f, 1f)
    }

    fun analyzeAudio() {
        viewModelScope.launch {
            _uiState.emit(PlaybackUiState.Loading)
            val result = audioAnalyzeAudioRepository.analyzeAudio(filePath)
            userDataRepository.userData.collectLatest {
                insertAudioUseCase(filePath, patientId = it.patientId, emotion = result)
                _uiState.emit(PlaybackUiState.Analyzed(result))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
        handler.removeCallbacks(playbackPositionUpdater)
    }
}