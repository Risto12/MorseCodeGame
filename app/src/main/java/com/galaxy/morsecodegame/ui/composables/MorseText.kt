package com.galaxy.morsecodegame.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.protobuf.ExperimentalApi
import com.galaxy.morsecodegame.R
import com.galaxy.morsecodegame.ui.theme.RetroVintagePurple
import com.galaxy.morsecodegame.ui.theme.RetroVintageRed

@Composable
@ExperimentalTextApi
fun DefaultHeaderText(
    text: String,
    @SuppressLint("ModifierParameter")
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 20.sp,
    color: Color = MaterialTheme.colors.primary,
    textDecoration: TextDecoration = TextDecoration.None,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    paddingBottom: Dp = 40.dp
) {
    Text(
        text = text,
        color = color,
        fontSize = fontSize,
        fontWeight = FontWeight.Black,
        modifier = modifier.padding(bottom = paddingBottom),
        textDecoration = textDecoration,
        style = TextStyle(
                brush = Brush.linearGradient(
                    colors = listOf(
                        RetroVintagePurple, RetroVintageRed
                    )
                )
                ),
        fontFamily = FontFamily(Font(R.font.shrikhand_regular)),
        letterSpacing = 1.5.sp
    )
}

@Composable
@SuppressLint("ModifierParameter")
fun DefaultText(
    text: String,
    modifier: Modifier = Modifier.padding(bottom = 5.dp),
    fontSize: TextUnit = 15.sp,
    color: Color = MaterialTheme.colors.onPrimary,
    textDecoration: TextDecoration = TextDecoration.None,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    fontFamily: FontFamily = FontFamily(Font(R.font.shrikhand_regular)),
    letterSpacing: TextUnit = 1.sp,
    fontWeight: FontWeight = FontWeight.Black
) {
    Text(
        text = text,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        modifier = modifier,
        textDecoration = textDecoration,
        style = textStyle,
        letterSpacing = letterSpacing,
        fontFamily = fontFamily
    )
}

@Composable
@ExperimentalTextApi
fun Header6(
    text: String,
    fontSize: TextUnit = 30.sp,
    paddingBottom: Dp = 40.dp
) {
    DefaultHeaderText(
        text = text,
        fontSize = fontSize,
        textStyle = MaterialTheme.typography.h6,
        paddingBottom = paddingBottom
    )
}
