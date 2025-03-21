package com.example.clonetiktok.ui.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_ALL
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import com.example.clonetiktok.data.repositories.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class VideoDetailViewModel @Inject constructor(
    val videoPlayer: ExoPlayer,
    val videoRepository: VideoRepository
): ViewModel() {

    private var _uiState = MutableStateFlow<VideoDetailUiState>(VideoDetailUiState.Default) // mutableLiveData
    val uiState: StateFlow<VideoDetailUiState> // liveData
        get() = _uiState

    init {
        videoPlayer.repeatMode = REPEAT_MODE_ALL
        videoPlayer.playWhenReady = true
        videoPlayer.prepare()
    }

    fun handleAction(action: VideoDetailAction) {
        when (action) {
            is VideoDetailAction.LoadData -> {
                videoPlayer.stop() // ⛔ Dừng video trước đó
                videoPlayer.clearMediaItems()
                videoPlayer.seekTo(0) // 🔄 Đặt lại thời gian về 0
                loadVideo(action.id) // ⏯ Tải và phát video mới
            }
            is VideoDetailAction.ToggleVideo -> {
                toggleVideo()
            }
        }
    }

    private fun loadVideo(videoId: Int) {
        _uiState.value = VideoDetailUiState.Loading
        viewModelScope.launch {
            delay(100L)
            val video = videoRepository.getVideo()
            // play
            playVideo(videoResourceId = video)
            _uiState.value = VideoDetailUiState.Success
        }
    }

    private fun playVideo(videoResourceId: Int) {
        val uri = RawResourceDataSource.buildRawResourceUri(videoResourceId)
        val mediaItem = MediaItem.fromUri(uri)
        videoPlayer.clearMediaItems()
        videoPlayer.setMediaItem(mediaItem)
        videoPlayer.prepare()
        videoPlayer.play()
    }

    fun playVideo() {
        videoPlayer.playWhenReady = true
        videoPlayer.volume = 1f // ✅ Bật âm thanh khi active
    }

    fun pauseVideo() {
        videoPlayer.playWhenReady = false
        videoPlayer.volume = 0f // ✅ Tắt âm thanh khi không active
    }


    private fun toggleVideo() {
        if(videoPlayer.isLoading) {

        } else {
            if(videoPlayer.isPlaying) {
                videoPlayer.pause()
            } else {
                videoPlayer.play()
            }
        }
    }
}

// MVVM
sealed interface VideoDetailUiState {
    object Default: VideoDetailUiState
    object Loading: VideoDetailUiState
    object Success: VideoDetailUiState
    data class Error(val msg: String): VideoDetailUiState
}

sealed class VideoDetailAction {
    data class LoadData(val id: Int): VideoDetailAction()
    object ToggleVideo: VideoDetailAction()
}
