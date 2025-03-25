package com.example.clonetiktok.data.repositories

import com.example.clonetiktok.R
import javax.inject.Inject

class VideoRepository @Inject constructor() {
    private val videos = listOf(
        R.raw.remix,
        R.raw.remix2,
        R.raw.remix3,
        R.raw.remix4,
        R.raw.remix5,
        R.raw.remix6,
        R.raw.remix7,
        R.raw.test,
        R.raw.test2,
        R.raw.test3,
        R.raw.test4,
        R.raw.car,
        R.raw.car2,
        R.raw.car3,
        R.raw.car4,
        R.raw.car5,
        R.raw.lixi,
        R.raw.mc,
        R.raw.ui,
    )

    fun getVideo() = videos.random()
}