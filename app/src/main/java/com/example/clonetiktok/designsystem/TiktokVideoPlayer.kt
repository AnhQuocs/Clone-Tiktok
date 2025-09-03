package com.example.clonetiktok.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@UnstableApi
@Composable
fun TiktokVideoPlayer(
    modifier: Modifier = Modifier,
    player: Player
) {
    AndroidView(factory = {
        context ->
        PlayerView(context).also {
            it.player = player
            it.useController = false
            it.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        }
    }, modifier = modifier)
}