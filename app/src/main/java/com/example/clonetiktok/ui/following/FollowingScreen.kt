package com.example.clonetiktok.ui.following

import android.provider.MediaStore.Video
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import com.example.clonetiktok.R
import com.example.clonetiktok.data.repositories.VideoRepository
import com.example.clonetiktok.designsystem.TiktokVideoPlayer
import com.example.clonetiktok.ui.theme.CloneTiktokTheme
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@UnstableApi
@Composable
fun FollowingScreen(
    modifier: Modifier = Modifier,
    isScreenActive: Boolean
) {
    val pagerState = rememberPagerState(pageCount = { 20 })
    val cardWidth = (LocalConfiguration.current.screenWidthDp * 2/3) - 24 //hiển thị các video bên cạnh
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val paddingValue = (screenWidth - cardWidth) / 2

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(48.dp))

        Text(
            "Trending Creators",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color.White
            )
        )

        Spacer(Modifier.height(12.dp))

        Text(
            "Follow an account to see their latest videos here.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Gray
            )
        )

        Spacer(Modifier.height(36.dp))

        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f), // kích thước video
            state = pagerState,
            pageSize = PageSize.Fixed(cardWidth.dp),
            pageSpacing = 12.dp, // khoảng cách giữa các video
            contentPadding = PaddingValues(horizontal = paddingValue.dp) // padding start đầu tiên
        ) {
                page ->
            val isPlaying = isScreenActive && pagerState.currentPage == page // Xác định trang trung tâm
            Card(
                modifier = Modifier
                    .width(cardWidth.dp)
                    .aspectRatio(0.7f)
                    .graphicsLayer {
                        val pageOffset = (
                                (pagerState.currentPage - page) + pagerState
                                    .currentPageOffsetFraction
                                ).absoluteValue

                        // We animate the alpha, between 50% and 100%
                        scaleY = lerp(
                            start = 0.7f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
                    .clip(RoundedCornerShape(16.dp))
            ) {
                CreatorCard(
                    name = "AnhQuocs ${page+1}",
                    idName = "@anhquocs_${page+1}",
                    onFollow = {},
                    onClose = {},
                    isPlaying = isPlaying
                )
            }
        }
    }
}

@UnstableApi
@Composable
fun CreatorCard(
    modifier: Modifier = Modifier,
    name: String,
    idName: String,
    onFollow: () -> Unit,
    onClose: () -> Unit,
    isPlaying: Boolean,
    videoRepository: VideoRepository = remember { VideoRepository() }
) {
    val context = LocalContext.current
    val videoPlayer = remember(name) { ExoPlayer.Builder(context).build().apply {
        repeatMode = Player.REPEAT_MODE_ALL
        playWhenReady = false
        prepare()
    }}

    val randomVideo = remember { videoRepository.getVideo() }
    val uri = RawResourceDataSource.buildRawResourceUri(randomVideo)
    val mediaItem = MediaItem.fromUri(uri)
    videoPlayer.setMediaItem(mediaItem)

    // Cập nhật trạng thái của player khi isPlaying thay đổi
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            videoPlayer.seekTo(0)
            videoPlayer.play()
        } else {
            videoPlayer.pause()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            videoPlayer.release()
        }
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
    ) {
        val (videoIntro, btnClose, imgAvatar, tvName, tvIdName, btnFollow) = createRefs()

        TiktokVideoPlayer(player = videoPlayer, modifier = Modifier.constrainAs(videoIntro) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            height = Dimension.fillToConstraints
            width = Dimension.fillToConstraints
        })

        IconButton(
            onClick = onClose,
            modifier = Modifier
                .constrainAs(btnClose) {
                    top.linkTo(parent.top, margin = 12.dp)
                    end.linkTo(parent.end, margin = 12.dp)
                }
                .size(16.dp)
        ) {
            Icon(
                Icons.Sharp.Close, contentDescription = "close icon", tint = Color.White
            )
        }

        Button(
            onClick = onFollow,
            modifier = Modifier
                .constrainAs(btnFollow) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, margin = 12.dp)
                }
                .padding(
                    horizontal = 56.dp
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE94359),
                contentColor = Color.White
            )
        ) {
            Text("Follow", style = MaterialTheme.typography.bodyLarge)
        }

        Text(
            text = idName,
            style = MaterialTheme.typography.titleMedium.copy(color = Color.Gray),
            modifier = Modifier.constrainAs(tvIdName) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(btnFollow.top)
            }
        )

        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
            modifier = Modifier.constrainAs(tvName) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(tvIdName.top)
            }
        )

        AvatarFollowing(modifier = Modifier.constrainAs(imgAvatar) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(tvName.top, margin = 8.dp)
        })
    }
}

@Composable
fun AvatarFollowing(
    modifier: Modifier = Modifier
) {
    val sizeAvatar = LocalConfiguration.current.screenWidthDp * 0.15

    Image(
        painter = painterResource(id = R.drawable.ic_dog),
        contentDescription = "avatar",
        modifier = modifier
            .size(sizeAvatar.dp)
            .background(color = Color.White, shape = CircleShape)
            .border(2.dp, color = Color.White, shape = CircleShape)
            .clip(CircleShape)
    )
}

@Preview
@Composable
private fun AvatarFollowingPreview() {
    CloneTiktokTheme {
        AvatarFollowing()
    }
}