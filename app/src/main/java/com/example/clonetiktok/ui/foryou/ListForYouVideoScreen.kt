package com.example.clonetiktok.ui.foryou

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
@Composable
fun ListForYouVideoScreen(
    modifier: Modifier = Modifier,
    onShowComment: (Int) -> Unit,
    isForYouActive: Boolean
) {
    val pagerState = rememberPagerState(pageCount = { 20 })

    VerticalPager(state = pagerState) { videoId ->
        val viewModel: VideoDetailViewModel = hiltViewModel(key = videoId.toString())

        val isActive = isForYouActive && pagerState.currentPage == videoId // Chỉ phát video khi màn hình này đang hiển thị

        VideoDetailScreen(videoId = videoId, viewModel = viewModel, isActive = isActive, onShowComment = onShowComment)
    }
}
