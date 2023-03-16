package com.galaxy.morsecodegame.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StatusBar(
    left: String? = null,
    middle: String? = null,
    right: String? = null
) {
    Box(
        Modifier
            .heightIn(max = 30.dp)
            .background(color = MaterialTheme.colors.secondaryVariant)
            .fillMaxWidth()
    ) {
        val textColor = MaterialTheme.colors.primary
        left?.run {
            Text(
                text = this,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterStart),
                color = textColor
            )
        }
        middle?.run {
            Text(
                text = this,
                modifier = Modifier.align(Alignment.BottomCenter),
                color = textColor
            )
        }
        right?.run {
            Text(
                text = this,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 5.dp),
                color = textColor
            )
        }
    }
}
