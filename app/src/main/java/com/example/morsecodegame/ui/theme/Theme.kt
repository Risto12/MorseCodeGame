package com.example.morsecodegame.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = VintageRed,
    primaryVariant = VintageRedDark,
    onPrimary = VintageBlack,
    secondary = VintageWhite,
    secondaryVariant = VintageWhiteDark,
    onSecondary = VintageBlack,
    background = VintageRed
)

// Well these are copies but maybe in future there be differences
@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = VintageRed,
    primaryVariant = VintageRedDark,
    onPrimary = VintageWhite,
    secondary = VintageBlack,
    secondaryVariant = VintageBlackDark,
    onSecondary = VintageWhite,
    background = VintageRed
)

@Composable
fun MorseCodeGameTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
