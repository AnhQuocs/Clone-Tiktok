package com.example.clonetiktok.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.media3.common.util.UnstableApi
import com.example.clonetiktok.R
import com.example.clonetiktok.ui.comment.CommentScreen
import com.example.clonetiktok.ui.following.FollowingScreen
import com.example.clonetiktok.ui.following.ListFollowingVideoScreen
import com.example.clonetiktok.ui.foryou.ListForYouVideoScreen
import com.example.clonetiktok.ui.theme.CloneTiktokTheme
import com.example.clonetiktok.ui.user.ProfileScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@UnstableApi
@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(pageCount = { 3 }, initialPage = 1)

    val scrollToPage: (Boolean) -> Unit = {
        isForYou->
        val page = if(isForYou) 1 else 0
        coroutineScope.launch {
            pagerState.scrollToPage(page = page)
        }
    }

    var isShowTabContent by remember { mutableStateOf(true) }

    val toggleTabContent = {isShow: Boolean ->
        if(isShowTabContent != isShow) {
            isShowTabContent = isShow
        }
    }

    var currentVideoId by remember { mutableStateOf(-1) }

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            isShowTabContent = page != 2  // Ẩn tab khi vào ProfileScreen
        }
    }

    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen = remember { mutableStateOf(false) }
    val showCommentBottomSheet: (Int) -> Unit = { videoId ->
        currentVideoId = videoId
        isSheetOpen.value = true  // ✅ Cần bật giá trị này lên để hiển thị sheet
        coroutineScope.launch {
            sheetState.show()
        }
    }

    val hideCommentBottomSheet: () -> Unit = {
        currentVideoId = -1
        isSheetOpen.value = false  // ✅ Tắt hiển thị khi đóng
        coroutineScope.launch {
            sheetState.hide()
        }
    }

    if (isSheetOpen.value) {
        ModalBottomSheet(
            onDismissRequest = { hideCommentBottomSheet() },  // ✅ Gọi hàm đóng
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            if (currentVideoId != -1) {
                CommentScreen(videoId = currentVideoId)
            }
        }
    }

    val isForYouActive = pagerState.currentPage == 1 // ✅ Kiểm tra màn hình hiện tại

    Scaffold(
        bottomBar = {
            AnimatedVisibility(visible = isShowTabContent) {
                TikTokBottomAppBar(onOpenHome =  {}, onAddVideo = {})
            }
        }
    ) { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val (tabContentView, body) = createRefs()

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.constrainAs(body) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            ) { page ->
                val isFollowingScreenActive = pagerState.currentPage == 0
                when(page) {
                    0 -> FollowingScreen(isScreenActive = isFollowingScreenActive)
                    2 -> ProfileScreen()
                    else -> ListForYouVideoScreen(
                        isForYouActive = isForYouActive,
                        onShowComment = showCommentBottomSheet
                    )
                }
            }

            AnimatedVisibility(visible = isShowTabContent) {
                TabContentView(
                    isTabSelectedIndex = pagerState.currentPage,
                    onSelectedTab = {
                            isForYou ->
                        scrollToPage(isForYou)
                    },
                    modifier = Modifier.constrainAs(tabContentView) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                )
            }
        }
    }
}

@Composable
fun TikTokBottomAppBar(
    modifier: Modifier = Modifier,
    onOpenHome: () -> Unit,
    onAddVideo: () -> Unit
) {
    val iconSelected = NavigationBarItemDefaults.colors(
        indicatorColor = Color.Transparent // Xóa nền oval xanh lá
    )

    NavigationBar(containerColor = Color.Black, modifier = Modifier.height(66.dp)) {
        NavigationBarItem(
            selected = true,
            onClick = {onOpenHome()},
            icon = {
                Icon(painter = painterResource(id = R.drawable.ic_home), contentDescription = "home icon", modifier = Modifier.size(28.dp))
            },
            label = { Text("Home", style = TextStyle(color = Color.White, fontSize = 14.sp))},
            colors = iconSelected
        )

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = {
                Icon(Icons.Default.ShoppingCart, contentDescription = "cart icon", modifier = Modifier.size(28.dp))
            },
            label = { Text("Shop", style = TextStyle(color = Color.White, fontSize = 14.sp))},
            colors = iconSelected
        )

        NavigationBarItem(
            selected = false,
            onClick = {onAddVideo()},
            icon = {
                Icon(painter = painterResource(id = R.drawable.ic_add_video), contentDescription = "add video icon")
            },
            colors = iconSelected
        )

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = {
                Icon(painter = painterResource(id = R.drawable.ic_inbox), contentDescription = "inbox icon", modifier = Modifier.size(28.dp))
            },
            label = { Text("Inbox", style = TextStyle(color = Color.White, fontSize = 14.sp))},
            colors = iconSelected
        )

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = {
                Icon(painter = painterResource(id = R.drawable.ic_profile), contentDescription = "profile icon", modifier = Modifier.size(28.dp))
            },
            label = { Text("Profile", style = TextStyle(color = Color.White, fontSize = 14.sp))},
            colors = iconSelected
        )
    }
}

@Composable
fun TabContentItemView(
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean,
    isForYou: Boolean,
    onSelectedTab:(isForYou: Boolean) -> Unit
) {
    val alpha = if(isSelected) 1f else 0.6f

    Column(
        modifier = modifier
            .wrapContentSize()
            .clickable {
                onSelectedTab(isForYou)
            }
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White.copy(alpha = alpha))
        )
        Spacer(Modifier.height(8.dp))

        if(isSelected) {
            Divider(
                color = Color.White,
                thickness = 2.dp,
                modifier = Modifier.width(24.dp)
            )
        }
    }
}

@Composable
fun TabContentView(
    modifier: Modifier = Modifier,
    isTabSelectedIndex: Int,
    onSelectedTab:(isForYou: Boolean) -> Unit
) {
    ConstraintLayout(
        modifier = modifier.fillMaxWidth()
    ) {
        val (tabContent, iconSearch) = createRefs()
        Row(
            modifier = Modifier
                .wrapContentSize()
                .constrainAs(tabContent) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            TabContentItemView(
                title = "Following",
                isSelected = isTabSelectedIndex == 0,
                isForYou = false,
                onSelectedTab = onSelectedTab
            )

            Spacer(Modifier.width(12.dp))

            TabContentItemView(
                title = "For You",
                isSelected = isTabSelectedIndex == 1,
                isForYou = true,
                onSelectedTab = onSelectedTab
            )
        }

        IconButton(
            onClick = {},
            modifier = Modifier.constrainAs(iconSearch) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end, margin = 16.dp)
            }
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = "search icon",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
private fun TabContentItemViewPreviewSelected() {
    CloneTiktokTheme {
        TabContentItemView(
            title = "For You",
            isSelected = true,
            isForYou = true,
            onSelectedTab = {}
        )
    }
}

@Preview
@Composable
private fun TabContentItemViewPreviewUnSelected() {
    CloneTiktokTheme {
        TabContentItemView(
            title = "Following",
            isSelected = false,
            isForYou = false,
            onSelectedTab = {}
        )
    }
}

@Preview
@Composable
private fun TabContentViewPreview() {
    CloneTiktokTheme {
        TabContentView(
            isTabSelectedIndex = 1,
            onSelectedTab = {}
        )
    }
}