package com.example.clonetiktok.ui.foryou

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.example.clonetiktok.ui.video.VideoDetailScreen
import com.example.clonetiktok.ui.video.VideoDetailViewModel

@UnstableApi
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListForYouVideoScreen() {
    val pagerState = rememberPagerState(pageCount = { 10 })

    VerticalPager(state = pagerState) { videoId ->
        val viewModel: VideoDetailViewModel = hiltViewModel(key = videoId.toString())

        val isActive = pagerState.currentPage == videoId

        VideoDetailScreen(videoId = videoId, viewModel = viewModel, isActive = isActive)
    }
}
