package com.example.morsecodegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.morsecodegame.composables.SharedComposable
import com.example.morsecodegame.configurations.MorseCodeLettersInfoTextConfiguration
import com.example.morsecodegame.morsecode.MorseCodeLetter
import com.example.morsecodegame.ui.theme.MorseCodeGameTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

/***
 * Activity presents information related to Morse code and all the letters in DI, DA format
 */
@AndroidEntryPoint
class MorseCodeLettersActivity : ComponentActivity() {

    @Inject
    lateinit var configurations: MorseCodeLettersInfoTextConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val back = { finish() }
        setContent {
            val configurations by rememberSaveable { mutableStateOf(configurations) }
            MorseCodeGameTheme {
                MorseCodeLetters(
                    overviewText = configurations.overview,
                    wordsPerMinuteText = configurations.wordsPerMinute,
                    exampleText = configurations.example,
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
    val listState = rememberLazyListState()
    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    val morseCodeLetterScope = rememberCoroutineScope() // TODO check this scope

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colors.background)) {
        AnimatedVisibility(visible = showButton) {
            SharedComposable.DefaultButton(
                configurations = SharedComposable.DefaultButtonConfigurations(
                    text = "Back to top",
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
                .fillMaxSize()
                .weight(2.5f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                SharedComposable.DefaultHeaderText(text = "Morse letters", fontSize = 25.sp)
            }
            MorseCodeLetter.values().forEach {
                val units = it
                    .units.map { unit -> unit.name + " " }
                    .toString()
                    .replace("[", " = ")
                    .replace("]", " ")
                    .trimEnd()
                item { SharedComposable.DefaultHeaderText(it.name + " " + units) }
            }
            item {
                Divider(
                    modifier = Modifier.padding(top = 40.dp),
                    color = MaterialTheme.colors.onPrimary,
                    thickness = 1.dp
                )
            }
        }
        SharedComposable.DefaultButton(
            configurations = SharedComposable.DefaultButtonConfigurations(
                text = "Back",
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
    Column {
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
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
            color = MaterialTheme.colors.onPrimary
        )
        Divider(
            modifier = Modifier.padding(top = 25.dp, bottom = 35.dp),
            color = MaterialTheme.colors.onPrimary,
            thickness = 1.dp
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
