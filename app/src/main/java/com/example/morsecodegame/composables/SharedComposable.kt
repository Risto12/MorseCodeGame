package com.example.morsecodegame.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


val DEFAULT_THEME_COLOR = Color.Magenta

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
        fontSize: TextUnit = 20.sp,
        modifier: Modifier = Modifier.padding(bottom = 40.dp)
    ) {
        Text(
            text = text,
            color = DEFAULT_THEME_COLOR,
            fontSize = fontSize,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            modifier = modifier
        )
    }

    @Composable
    fun DefaultText(
        text: String,
        fontSize: TextUnit = 15.sp,
        modifier: Modifier = Modifier.padding(bottom = 5.dp)
    ) {
        Text(
            text = text,
            color = DEFAULT_THEME_COLOR,
            fontSize = fontSize,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            modifier = modifier
        )
    }

    @Composable
    fun DefaultButton(
        configurations: DefaultButtonConfigurations,
        modifier: Modifier = Modifier.padding(bottom = 15.dp),
    ) {
        val (color, textDecoration) = if (configurations.available) {
            Pair(DEFAULT_THEME_COLOR, null)
        } else {
            Pair(Color.Gray, TextDecoration.LineThrough)
        }
        Button(
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = color
            ),
            modifier = modifier,
            onClick = configurations.click,
            enabled = configurations.enabled
        ) {
            Text(
                text = configurations.text,
                color = Color.White,
                fontSize = 20.sp,
                textDecoration = textDecoration
            )
        }
    }

    @Composable
    fun DefaultButton(
        configurations: DefaultButtonComposableConfigurations,
        modifier: Modifier = Modifier.fillMaxWidth(),
    ) {
        val color = if (configurations.available) DEFAULT_THEME_COLOR else Color.Gray
        Button(
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = color
            ),
            modifier = modifier,
            onClick = configurations.click,
            enabled = configurations.enabled,
        ) {
            configurations.content()
        }
    }

    @Composable
    fun Header(
        text: String,
        fontSize: TextUnit = 40.sp
    ) {
        DefaultHeaderText(
            text = text,
            fontSize = fontSize
        )
    }
}

@Composable
fun GameScreen(
    imageId: Int,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painterResource(imageId),
            contentDescription = "Stormy sea",
            modifier = Modifier.matchParentSize()
            )
        content()
    }
}