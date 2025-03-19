package com.example.clonetiktok.ui.following

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.example.clonetiktok.ui.video.VideoDetailScreen
import com.example.clonetiktok.ui.video.VideoDetailViewModel

@UnstableApi
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListFollowingVideoScreen(
    modifier: Modifier = Modifier,
    onShowComment: (Int) -> Unit,
    isFollowingActive: Boolean // 🔥 Biết được màn hình có đang hiển thị hay không
) {
    val pagerState = rememberPagerState(pageCount = { 10 })

    VerticalPager(state = pagerState) { videoId ->
        val viewModel: VideoDetailViewModel = hiltViewModel(key = videoId.toString())

        val isActive = isFollowingActive && pagerState.currentPage == videoId // ✅ Chỉ phát video khi đúng trang và đúng màn hình

        VideoDetailScreen(videoId = videoId, viewModel = viewModel, isActive = isActive, onShowComment = onShowComment)
    }
}
