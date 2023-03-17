package com.galaxy.morsecodegame.ui.composables

import android.content.res.Configuration

fun Int.isSmallOrientationScreen(fromWidth: Boolean = true): Boolean {
    val bigOrientationScreen = if (fromWidth) 500 else 800
    return this < bigOrientationScreen
}

fun Int.isSmallLandScapeScreen(): Boolean {
    val bigLandScapeScreen = 900
    return this < bigLandScapeScreen
}

fun Int.isPortrait() = this == Configuration.ORIENTATION_PORTRAIT
