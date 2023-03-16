package com.galaxy.morsecodegame.ui.composables

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.galaxy.morsecodegame.R
import com.galaxy.morsecodegame.ui.theme.VintageGreen
import com.galaxy.morsecodegame.ui.theme.VintageRedDark
import com.galaxy.morsecodegame.utility.getStringUpper
import com.galaxy.morsecodegame.utility.isPortrait

fun Modifier.addBigPopupSize(configuration: Configuration): Modifier {
    return if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        this
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.85f)
            .padding(top = 10.dp)
    } else {
        this
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.85f)
            .padding(top = 10.dp)
    }
}

@Composable
fun ScrollDownPopupLayout(
    onDismissRequest: () -> Unit,
    items: LazyListScope.() -> Unit
) {
    val localContext = LocalContext.current
    Popup(
        onDismissRequest = onDismissRequest,
        alignment = Alignment.Center
    ) {
        val listState = rememberLazyListState()
        val showButton by remember {
            derivedStateOf {
                listState.layoutInfo.visibleItemsInfo.size < listState.layoutInfo.totalItemsCount
            }
        }
        Surface(
            elevation = 9.dp,
            color = MaterialTheme.colors.primary,
            border = BorderStroke(2.dp, color = MaterialTheme.colors.onPrimary),
            contentColor = MaterialTheme.colors.onPrimary,
            modifier = Modifier.addBigPopupSize(LocalConfiguration.current)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(6f),
                    content = items
                )
                AnimatedVisibility(visible = showButton) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.05f)
                    ) {
                        DefaultText(
                            text = localContext.getStringUpper(R.string.common_scroll_down),
                            modifier = Modifier.align(Alignment.Center),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoWarningPopup(
    infoHeader: String,
    infoText: String,
    warningText: String,
    onDismissRequest: () -> Unit
) {
    val localContext = LocalContext.current
    ScrollDownPopupLayout(
        onDismissRequest = onDismissRequest
    ) {
        item {
            DefaultHeaderText(
                color = MaterialTheme.colors.onPrimary,
                text = infoHeader.uppercase(),
                modifier = Modifier.padding(
                    top = 10.dp
                ),
                paddingBottom = 15.dp
            )
            DefaultText(
                text = infoText,
                fontSize = 13.sp,
                modifier = Modifier.padding(
                    start = 20.dp,
                    bottom = 20.dp,
                    end = 20.dp
                ),
                textStyle = MaterialTheme.typography.body1
            )
        }
        item {
            DefaultText(
                text = localContext.getStringUpper(R.string.common_warning),
                color = VintageRedDark,
                modifier = Modifier.padding(top = 5.dp),
                fontSize = 20.sp
            )
            DefaultText(
                text = warningText.uppercase(),
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
        }
        item {
            DefaultText(
                text = localContext.getStringUpper(R.string.common_ok),
                color = VintageGreen,
                modifier = Modifier
                    .padding(bottom = 10.dp, top = 5.dp)
                    .clickable { onDismissRequest() }
            )
        }
    }
}

@Composable
fun InfoWarningDisclaimerPopup(
    infoText: String,
    onClickOk: (Boolean) -> Unit,
    onClickCancel: () -> Unit
) {
    var onStateChange by rememberSaveable {
        mutableStateOf(false)
    }
    val localContext = LocalContext.current
    ScrollDownPopupLayout(
        onDismissRequest = {} // Close this window only by clicking ok or cancel
    ) {
        item {
            DefaultText(
                text = localContext.getStringUpper(R.string.common_warning),
                color = VintageRedDark,
                modifier = Modifier.padding(top = 30.dp),
                fontSize = 20.sp
            )
            DefaultText(
                text = infoText.uppercase(),
                fontSize = 13.sp,
                modifier = Modifier.padding(
                    start = 20.dp,
                    top = 25.dp,
                    bottom = 20.dp,
                    end = 20.dp
                ),
                fontFamily = FontFamily.Default,
                textStyle = MaterialTheme.typography.body1
            )
        }
        item {
            CheckBoxWithText(
                text = {
                    DefaultText(
                        text = localContext.getString(R.string.common_switch_dont_show),
                        modifier = Modifier.padding(
                            start = 10.dp
                        ),
                        fontSize = 12.sp
                    )
                },
                checkBoxTestTag = "disclaimerCheckBox",
                onStateChange = { onStateChange = it },
                rowModifier = if (LocalConfiguration.current.orientation.isPortrait()) {
                    Modifier.padding(top = 90.dp, bottom = 10.dp)
                } else {
                    Modifier.padding(top = 20.dp, bottom = 10.dp)
                }
            )
            DefaultText(
                text = localContext.getStringUpper(R.string.common_ok),
                color = VintageGreen,
                modifier = Modifier
                    .padding(top = 25.dp, bottom = 10.dp)
                    .clickable { onClickOk(onStateChange) }
            )
            DefaultText(
                text = localContext.getStringUpper(R.string.common_cancel),
                color = VintageRedDark,
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 25.dp)
                    .clickable { onClickCancel() }
            )
        }
        item {} // When this is shown the scroll down will disappear
    }
}

// InfoHeader could be added
@Composable
fun InfoPopup(
    infoText: String,
    onDismissRequest: () -> Unit
) {
    val localContext = LocalContext.current
    Popup(
        onDismissRequest = onDismissRequest,
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
                DefaultText(
                    text = infoText,
                    modifier = Modifier.padding(
                        start = 5.dp,
                        top = 5.dp,
                        bottom = 20.dp,
                        end = 5.dp
                    )
                )
                DefaultText(
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
