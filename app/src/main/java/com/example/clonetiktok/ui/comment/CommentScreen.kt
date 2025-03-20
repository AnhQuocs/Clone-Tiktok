package com.example.clonetiktok.ui.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun CommentScreen(
    modifier: Modifier = Modifier,
    videoId: Int
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp / 2

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(screenHeight.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Comment for video: $videoId")
    }
}