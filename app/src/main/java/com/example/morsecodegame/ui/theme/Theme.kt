package com.example.morsecodegame.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
//TODO these colors look so good that need to extract them to color package for re-use in new applications
@SuppressLint("ConflictingOnColor")
private val VintageBlueColorPalette = darkColors(
    primary = VintageRedDeep,
    primaryVariant = VintageWhiteWorn,
    onPrimary = VintageWhiteWorn,
    secondary = VintageBlueDeep,
    secondaryVariant = VintageBlue,
    onSecondary = VintageWhite,
    background = VintageBlueDeep
)

// Well these are copies but maybe in future there be differences
@SuppressLint("ConflictingOnColor")
val VintageRedColorPalette = lightColors(
    primary = VintageWhite,
    primaryVariant = VintageWhiteDark,
    onPrimary = VintageBlack,
    secondary = VintageRed,
    secondaryVariant = VintageRedDark,
    onSecondary = VintageWhite,
    background = VintageRed
)

@Composable
fun MorseCodeGameTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        VintageBlueColorPalette
    } else {
        VintageRedColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
