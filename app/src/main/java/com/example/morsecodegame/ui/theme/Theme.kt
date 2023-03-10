package com.example.morsecodegame.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
// TODO these colors look so good that need to extract them to color package for re-use in new applications
@SuppressLint("ConflictingOnColor")
private val VintageRustyBlueColorPalette = darkColors(
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
val VintageStrawberryCupcakeColorPalette = lightColors(
    primary = VintageWhite,
    primaryVariant = VintageWhiteDark,
    onPrimary = VintageBlack,
    secondary = VintageRed,
    secondaryVariant = VintageRedDark,
    onSecondary = VintageWhite,
    background = VintageRed
)

// TODO this needs more adjusting
@SuppressLint("ConflictingOnColor")
val VintageMintChocolateColorPalette = lightColors(
    primary = VintageRedDeep,
    primaryVariant = VintageApricotGray,
    onPrimary = VintageWhiteDark,
    secondary = VintageGreenDark,
    secondaryVariant = VintageGreenDark,
    onSecondary = VintageWhite,
    background = VintageGreenBright
)

@Composable
fun MorseCodeGameTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        VintageRustyBlueColorPalette
    } else {
        VintageStrawberryCupcakeColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
