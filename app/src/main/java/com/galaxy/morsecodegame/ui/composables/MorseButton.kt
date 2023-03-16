package com.galaxy.morsecodegame.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
