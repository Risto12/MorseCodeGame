package com.example.morsecodegame.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.morsecodegame.R

object SharedComposable {

    data class DefaultButtonConfigurations(
        val text: String,
        val click: () -> Unit,
        val available: Boolean = true,
        val enabled: Boolean = true
    )

    /**
     * More generic button that accepts composable as button content vs String.
     * Useful if you want to pass things like icons
     */
    data class DefaultButtonComposableConfigurations(
        val content: @Composable() () -> Unit,
        val click: () -> Unit,
        val available: Boolean = true,
        val enabled: Boolean = true
    )

    @Composable
    fun DefaultHeaderText(
        text: String,
        @SuppressLint("ModifierParameter")
        modifier: Modifier = Modifier.padding(bottom = 40.dp),
        fontSize: TextUnit = 20.sp,
        color: Color = MaterialTheme.colors.primary,
        textDecoration: TextDecoration = TextDecoration.None,
        textStyle: TextStyle = MaterialTheme.typography.body1
    ) {
        Text(
            text = text,
            color = color,
            fontSize = fontSize,
            fontWeight = FontWeight.Black,
            modifier = modifier,
            textDecoration = textDecoration,
            style = textStyle,
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
            fontFamily = fontFamily,
        )
    }

    @Composable
    @SuppressLint("ModifierParameter")
    fun DefaultButton(
        configurations: DefaultButtonConfigurations,
        modifier: Modifier = Modifier.padding(bottom = 15.dp)
    ) {
        val (color, textDecoration) = if (configurations.available) {
            Pair(MaterialTheme.colors.onPrimary, null)
        } else {
            Pair(MaterialTheme.colors.onPrimary, TextDecoration.LineThrough)
        }
        Button(
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = MaterialTheme.colors.primary
            ),
            modifier = modifier,
            onClick = configurations.click,
            enabled = configurations.enabled,
            elevation = ButtonDefaults.elevation(
                defaultElevation = 10.dp,
                pressedElevation = 20.dp,
                disabledElevation = 0.dp
            )
        ) {
            Text(
                text = configurations.text,
                color = color,
                fontSize = 20.sp,
                textDecoration = textDecoration,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.button
            )
        }
    }

    @Composable
    @SuppressLint("ModifierParameter")
    fun DefaultButton(
        configurations: DefaultButtonComposableConfigurations,
        modifier: Modifier = Modifier.fillMaxWidth()
    ) {
        val color = if (configurations.available) {
            MaterialTheme.colors.primary
        } else {
            MaterialTheme.colors.primary // maybe light color here ?
        }

        Button(
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = color
            ),
            modifier = modifier,
            onClick = configurations.click,
            enabled = configurations.enabled,
            elevation = ButtonDefaults.elevation(
                defaultElevation = 10.dp,
                pressedElevation = 20.dp,
                disabledElevation = 0.dp
            )
        ) {
            configurations.content()
        }
    }

    @Composable
    fun Header6(
        text: String,
        fontSize: TextUnit = 30.sp
    ) {
        DefaultHeaderText(
            text = text,
            fontSize = fontSize,
            textStyle = MaterialTheme.typography.h6
        )
    }
}
