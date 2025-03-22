package com.example.clonetiktok.ui.video

import android.provider.MediaStore.Video
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.clonetiktok.designsystem.TiktokVideoPlayer
import com.example.clonetiktok.ui.video.composables.SideBarView
import com.example.clonetiktok.ui.video.composables.VideoInfoArea

@UnstableApi
@Composable
fun VideoDetailScreen(
    videoId: Int,
    viewModel: VideoDetailViewModel = hiltViewModel(),
    isActive: Boolean, // ✅ Nhận trạng thái active từ VerticalPager
    onShowComment: (Int) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    var isAppInForeground by remember { mutableStateOf(true) } // ✅ Thêm biến kiểm soát app

    if (uiState.value == VideoDetailUiState.Default) {
        viewModel.handleAction(VideoDetailAction.LoadData(videoId))
    }

    // ✅ Lắng nghe sự kiện Lifecycle của app
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            isAppInForeground = event == Lifecycle.Event.ON_RESUME
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // ✅ Kiểm soát phát/dừng video và âm lượng
    LaunchedEffect(isActive, isAppInForeground) {
        if (isActive && isAppInForeground) {
            viewModel.videoPlayer.seekTo(0)
            viewModel.playVideo()
            viewModel.videoPlayer.playWhenReady = true
            viewModel.videoPlayer.volume = 1f
        } else {
            viewModel.videoPlayer.pause()
            viewModel.videoPlayer.playWhenReady = false // 🔴 Dừng phát hoàn toàn
            viewModel.videoPlayer.volume = 0f
        }
    }


    DisposableEffect(Unit) {
        onDispose {
            viewModel.pauseVideo()
        }
    }

    VideoDetailScreen(
        uiState = uiState.value,
        player = viewModel.videoPlayer,
        onShowComment = {
            onShowComment(videoId)
        }
    ) { action -> viewModel.handleAction(action) }

    Log.d("VideoPlayer", "Player State: ${viewModel.videoPlayer.playWhenReady}")
}

@UnstableApi
@Composable
fun VideoDetailScreen(
    uiState: VideoDetailUiState,
    player: Player,
    onShowComment: () -> Unit,
    handleAction: (VideoDetailAction) -> Unit
) {
    when(uiState) {
        is VideoDetailUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading...")
            }
        }
        is VideoDetailUiState.Success -> {
            VideoDetailScreen(player = player, handleAction = handleAction, onShowComment = onShowComment)
        }
        else -> {

        }
    }
}

@UnstableApi
@Composable
fun VideoDetailScreen(
    player: Player,
    handleAction: (VideoDetailAction) -> Unit,
    onShowComment: () -> Unit
) {

    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .clickable(
            onClick = {
                handleAction(VideoDetailAction.ToggleVideo)
            }
        )) {
        val (videoPlayer, sideBar, videoInfo) = createRefs()

        TiktokVideoPlayer(player = player, modifier = Modifier.constrainAs(videoPlayer) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.matchParent
            height = Dimension.matchParent
        })

        SideBarView(
            onAvatarClick = {},
            onLikeClick = {},
            onChatClick = onShowComment,
            onSaveClick = {},
            onShareClick = {},
            modifier = Modifier.constrainAs(sideBar) {
                end.linkTo(parent.end, margin = 16.dp)
                bottom.linkTo(parent.bottom, margin = 24.dp)
            }
        )

        VideoInfoArea(
            accountName = "AnhQuocs",
            videoName = "Clone Tiktok",
            hashtag = listOf(
                "#jetpack_compose",
                "#android",
                "#tiktok"
            ),
            songName = "Tràn bộ nhớ (ATSH)",
            modifier = Modifier.constrainAs(videoInfo) {
                start.linkTo(parent.start, margin = 16.dp)
                bottom.linkTo(sideBar.bottom)
                end.linkTo(sideBar.start, margin = 40.dp)
                width = Dimension.fillToConstraints
            }
        )
    }
}