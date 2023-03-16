package com.galaxy.morsecodegame.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CheckBoxWithText(
    text: @Composable() () -> Unit,
    onStateChange: (Boolean) -> Unit,
    checkBoxTestTag: String = "checkBoxTestTag",
    rowModifier: Modifier = Modifier
) {
    var onCheckChange by rememberSaveable { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = rowModifier
    ) {
        text()
        Checkbox(
            checked = onCheckChange,
            onCheckedChange = {
                onCheckChange = it
                onStateChange(it)
            },
            modifier = Modifier.testTag(checkBoxTestTag)
        )
    }
}

data class SwitchTexts(
    val left: String? = null,
    val right: String? = null
)

@Composable
fun SwitchWithText(
    text: @Composable() () -> Unit,
    onStateChange: (Boolean) -> Unit,
    switchTexts: SwitchTexts = SwitchTexts()
) {
    var checkedState by rememberSaveable { mutableStateOf(false) }
    val localContext = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        text()
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            switchTexts.left?.let {
                DefaultText(
                    text = it,
                    modifier = Modifier.padding(
                        end = 5.dp
                    ),
                    fontSize = 8.sp
                )
            }
            Switch(
                checked = checkedState,
                onCheckedChange = {
                    checkedState = it
                    onStateChange(it)
                },
                colors = SwitchDefaults.colors(
                    uncheckedTrackColor = MaterialTheme.colors.primaryVariant,
                    uncheckedThumbColor = MaterialTheme.colors.primaryVariant,
                    checkedTrackColor = MaterialTheme.colors.background,
                    checkedThumbColor = MaterialTheme.colors.background
                )
            )
            switchTexts.right?.let {
                DefaultText(
                    text = it,
                    modifier = Modifier.padding(
                        start = 5.dp
                    ),
                    fontSize = 8.sp
                )
            }
        }
    }
}
