package com.galaxy.morsecodegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galaxy.morsecodegame.morsecode.MorseCodeLetter
import com.galaxy.morsecodegame.ui.composables.SharedComposable
import com.galaxy.morsecodegame.ui.theme.MorseCodeGameTheme
import com.galaxy.morsecodegame.utility.getStringUpper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/***
 * Activity presents information related to Morse code and all the letters in DI, DA format
 */
@AndroidEntryPoint
class MorseCodeLettersActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val back = { finish() }
        setContent {
            MorseCodeGameTheme {
                MorseCodeLetters(
                    overviewText = getString(R.string.morse_letters_overview),
                    wordsPerMinuteText = getString(R.string.morse_letters_words_per_minute),
                    exampleText = getString(R.string.morse_letters_example),
                    onClickBack = back
                )
            }
        }
    }
}

@Composable
fun MorseCodeLetters(
    overviewText: String,
    wordsPerMinuteText: String,
    exampleText: String,
    onClickBack: () -> Unit
) {
    val localContext = LocalContext.current
    val listState = rememberLazyListState()
    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    val morseCodeLetterScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        AnimatedVisibility(visible = showButton) {
            SharedComposable.DefaultButton(
                configurations = SharedComposable.DefaultButtonConfigurations(
                    text = localContext.getStringUpper(R.string.morse_letters_top),
                    click = {
                        morseCodeLetterScope.launch { listState.animateScrollToItem(0) }
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(2.5f)
                .background(color = MaterialTheme.colors.primary).border(
                    10.dp,
                    MaterialTheme.colors.background
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Divider(
                    modifier = Modifier.padding(bottom = 10.dp),
                    color = MaterialTheme.colors.background,
                    thickness = 10.dp
                )
            }
            item {
                TextWithDividerOnBottom(
                    topic = "Overview",
                    text = overviewText
                )
                TextWithDividerOnBottom(
                    topic = "Words per minute",
                    text = wordsPerMinuteText
                )
            }
            item {
                TextWithDividerOnBottom(
                    topic = "Example",
                    text = exampleText
                )
            }
            item {
                SharedComposable.DefaultHeaderText(
                    text = "Morse letters",
                    fontSize = 25.sp,
                    color = MaterialTheme.colors.onPrimary
                )
            }
            item {
                MorseCodeLetter.values().forEach {
                    val units = it
                        .units.map { unit -> unit.name + " " }
                        .toString()
                        .replace("[", " = ")
                        .replace("]", " ")
                        .trimEnd()
                    SharedComposable.DefaultText(
                        text = it.name + " " + units,
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                            .fillMaxWidth()
                            .wrapContentWidth()
                    )
                }
            }
            item {
                Divider(
                    modifier = Modifier.padding(top = 40.dp),
                    color = MaterialTheme.colors.background,
                    thickness = 1.dp
                )
            }
        }
        SharedComposable.DefaultButton(
            configurations = SharedComposable.DefaultButtonConfigurations(
                text = localContext.getStringUpper(R.string.common_back),
                click = onClickBack
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun TextWithDividerOnBottom(
    topic: String,
    text: String
) {
    Column(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {
        SharedComposable.DefaultText(
            text = topic,
            fontSize = 25.sp,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                .align(Alignment.CenterHorizontally),
            color = MaterialTheme.colors.onPrimary
        )
        SharedComposable.DefaultText(
            text = text,
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
            color = MaterialTheme.colors.onPrimary
        )
        Divider(
            modifier = Modifier.padding(top = 25.dp, bottom = 20.dp),
            color = MaterialTheme.colors.background,
            thickness = 30.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MorseCodeLetterActivityPreview() {
    MorseCodeGameTheme {
        MorseCodeLetters(
            "overview text",
            "words per minute text",
            "example text"
        ) { }
    }
}
