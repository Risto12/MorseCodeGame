package com.example.morsecodegame.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
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
import com.example.morsecodegame.ui.theme.MorseCodeGameTheme
import com.example.morsecodegame.ui.theme.VintageBlack
import com.example.morsecodegame.ui.theme.VintageWhite


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
        fontSize: TextUnit = 20.sp
    ) {
        Text(
            text = text,
            color = MaterialTheme.colors.onPrimary,
            fontSize = fontSize,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            modifier = modifier
        )
    }

    @Composable
    @SuppressLint("ModifierParameter")
    fun DefaultText(
        text: String,
        modifier: Modifier = Modifier.padding(bottom = 5.dp),
        fontSize: TextUnit = 15.sp,
        color: Color = MaterialTheme.colors.secondary
    ) {
        Text(
            text = text,
            color = color,
            fontSize = fontSize,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            modifier = modifier
        )
    }

    @Composable
    @SuppressLint("ModifierParameter")
    fun DefaultButton(
        configurations: DefaultButtonConfigurations,
        modifier: Modifier = Modifier.padding(bottom = 15.dp)
    ) {
        val (color, textDecoration) = if (configurations.available) {
            Pair(MaterialTheme.colors.secondaryVariant, null)
        } else {
            Pair(MaterialTheme.colors.secondaryVariant, TextDecoration.LineThrough)
        }
        Button(
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = MaterialTheme.colors.onPrimary
            ),
            modifier = modifier,
            onClick = configurations.click,
            enabled = configurations.enabled
        ) {
            Text(
                text = configurations.text,
                color = color,
                fontSize = 20.sp,
                textDecoration = textDecoration
            )
        }
    }

    @Composable
    @SuppressLint("ModifierParameter")
    fun DefaultButton(
        configurations: DefaultButtonComposableConfigurations,
        modifier: Modifier = Modifier.fillMaxWidth()
    ) {
        val color = if (configurations.available)
            MaterialTheme.colors.onPrimary
        else
            MaterialTheme.colors.onPrimary // maybe light color here ?

        Button(
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = color
            ),
            modifier = modifier,
            onClick = configurations.click,
            enabled = configurations.enabled
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
    content: @Composable BoxScope.() -> Unit
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
