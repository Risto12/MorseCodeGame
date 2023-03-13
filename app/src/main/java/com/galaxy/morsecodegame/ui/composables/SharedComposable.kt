package com.galaxy.morsecodegame.ui.composables

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.galaxy.morsecodegame.R
import com.galaxy.morsecodegame.ui.theme.VintageGreen
import com.galaxy.morsecodegame.ui.theme.VintageRedDark
import com.galaxy.morsecodegame.utility.getStringUpper

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
            fontFamily = fontFamily
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
}

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

@Composable
fun InfoPopup(
    infoText: String,
    onDismissRequest: () -> Unit
) {
    val localContext = LocalContext.current
    Popup(
        onDismissRequest = { onDismissRequest() },
        alignment = Alignment.Center
    ) {
        Surface(
            elevation = 9.dp,
            color = MaterialTheme.colors.primary,
            border = BorderStroke(2.dp, color = MaterialTheme.colors.onPrimary),
            contentColor = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .defaultMinSize(100.dp, 100.dp)
                .sizeIn(maxWidth = 350.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SharedComposable.DefaultText(
                    text = infoText,
                    modifier = Modifier.padding(
                        start = 5.dp,
                        top = 5.dp,
                        bottom = 20.dp,
                        end = 5.dp
                    )
                )
                SharedComposable.DefaultText(
                    text = localContext.getStringUpper(R.string.common_ok),
                    color = VintageGreen,
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 20.dp)
                        .clickable { onDismissRequest() }
                )
            }
        }
    }
}

@Composable
fun InfoWarningPopup(
    infoText: String,
    onClickOk: (Boolean) -> Unit,
    onClickCancel: () -> Unit
) {
    var onStateChange by rememberSaveable {
        mutableStateOf(false)
    }
    val localContext = LocalContext.current
    Popup(
        onDismissRequest = { },
        alignment = Alignment.Center
    ) {
        val modifier = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Modifier
                .defaultMinSize(100.dp, 200.dp)
                .sizeIn(maxWidth = 250.dp)
        } else {
            Modifier
                .defaultMinSize(100.dp, 200.dp)
                .sizeIn(maxWidth = 600.dp)
        }
        Surface(
            elevation = 9.dp,
            color = MaterialTheme.colors.primary,
            border = BorderStroke(2.dp, color = MaterialTheme.colors.onPrimary),
            contentColor = MaterialTheme.colors.onPrimary,
            modifier = modifier
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SharedComposable.DefaultText(
                    text = localContext.getString(R.string.common_warning),
                    color = VintageRedDark,
                    modifier = Modifier.padding(top = 5.dp),
                    fontSize = 20.sp
                )
                SharedComposable.DefaultText(
                    text = infoText,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(
                        start = 20.dp,
                        top = 10.dp,
                        bottom = 20.dp,
                        end = 20.dp
                    ),
                    fontFamily = FontFamily.Default,
                    textStyle = MaterialTheme.typography.body1
                )
                SwitchWithText(
                    text = {
                        SharedComposable.DefaultText(
                            text = localContext.getString(R.string.common_switch_dont_show),
                            modifier = Modifier.padding(
                                start = 5.dp,
                                top = 10.dp,
                                end = 5.dp
                            ),
                            fontSize = 12.sp
                        )
                    },
                    onStateChange = { onStateChange = it }
                )
                SharedComposable.DefaultText(
                    text = localContext.getStringUpper(R.string.common_ok),
                    color = VintageGreen,
                    modifier = Modifier
                        .padding(top = 15.dp, bottom = 10.dp)
                        .clickable { onClickOk(onStateChange) }
                )
                SharedComposable.DefaultText(
                    text = localContext.getStringUpper(R.string.common_cancel),
                    color = VintageRedDark,
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 25.dp)
                        .clickable { onClickCancel() }
                )
            }
        }
    } 
}

@Composable
fun SwitchWithText(
    text: @Composable() () -> Unit,
    onStateChange: (Boolean) -> Unit
) {
    var checkedState by rememberSaveable { mutableStateOf(false) }
    val localContext = LocalContext.current
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
            ){
        text()
        Row (
            verticalAlignment = Alignment.CenterVertically,
        ){
            SharedComposable.DefaultText(
                text = localContext.getString(R.string.common_no),
                modifier = Modifier.padding(
                    end = 5.dp
                ),
                fontSize = 8.sp
            )
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
            SharedComposable.DefaultText(
                text = localContext.getString(R.string.common_yes),
                modifier = Modifier.padding(
                    start = 5.dp,
                ),
                fontSize = 8.sp
            )
        }
        }

}
