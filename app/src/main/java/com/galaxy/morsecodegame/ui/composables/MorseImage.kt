package com.galaxy.morsecodegame.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.galaxy.morsecodegame.R

@Composable
fun TelegraphImage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colors.primary)
    ) {
        Image(
            painterResource(R.drawable.telegraph),
            contentDescription = "Telegraph",
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .padding(start = 40.dp, end = 40.dp)
        )
    }
}
