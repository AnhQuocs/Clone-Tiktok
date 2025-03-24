package com.example.clonetiktok.ui.video.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clonetiktok.ui.theme.CloneTiktokTheme

@Composable
fun VideoInfoArea(
    modifier: Modifier = Modifier,
    accountName: String,
    videoName: String,
    hashtag: List<String>,
    songName: String
) {
    Column(
        modifier = modifier.wrapContentHeight(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = accountName,
            style = MaterialTheme.typography.headlineMedium.copy(color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold),
            maxLines = 1
        )
        Text(
            text = videoName,
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White, fontSize = 16.sp, lineHeight = 18.sp),
            maxLines = 2
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = hashtag.joinToString(" "),
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold),
            maxLines = 1
        )
        Spacer(Modifier.height(4.dp))
        Row {
            Icon(
                imageVector = Icons.Rounded.MusicNote,
                contentDescription = "music",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = songName,
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White, fontSize = 16.sp),
                maxLines = 1
            )
        }
    }
}

@Preview
@Composable
private fun VideoInfoAreaPreView() {
    CloneTiktokTheme {
        VideoInfoArea(
            accountName = "AnhQuocs",
            videoName = "First Video",
            hashtag = listOf(
                "#jetpack_compose",
                "#android",
                "#tiktok"
            ),
            songName = "Tràn bộ nhớ (DƯƠNG DOMIC)"
        )
    }
}