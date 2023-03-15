package com.galaxy.morsecodegame

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.galaxy.morsecodegame.ui.composables.SharedComposable
import com.galaxy.morsecodegame.ui.composables.SharedComposable.DefaultHeaderText
import com.galaxy.morsecodegame.ui.composables.StatusBar
import com.galaxy.morsecodegame.ui.composables.TelegraphImage
import com.galaxy.morsecodegame.ui.theme.*
import com.galaxy.morsecodegame.utility.*
import com.galaxy.morsecodegame.viewModel.GameTimeViewModel
import com.galaxy.morsecodegame.viewModel.GameViewModel
import com.galaxy.morsecodegame.viewModel.LoaderViewModel
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.*

const val WAIT_BEFORE_NEXT_QUESTION = 500L
const val WAIT_BEFORE_SENDING_NEXT_SIGNAL = 1500L
const val WAIT_BEFORE_CLEARING_ANSWERS = 3500L
const val SINGLE_PLAYER_TAG = "Single player activity"

class SinglePlayerActivity : ComponentActivity() {
    private val loaderViewModel: LoaderViewModel by viewModels()
    private val gameTimeViewModel: GameTimeViewModel by viewModels()
    private lateinit var gameViewModel: GameViewModel

    init {
        // timer logic. Can be used in init because repeatOnLifeCycle -> Started
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (gameTimeViewModel.hasTimeLeft()) {
                    delay(1000)
                    gameTimeViewModel.decreaseOneSecond()
                }
            }
        }
    }

    private fun gameOver() = launchIOCoroutine {
        delay(4000)
        finish()
    }

    private fun cancel() {
        finish()
    }

    private fun lockScreenOrientation() {
        /***
         * Locking the screen orientation when flashing the morse light for two reasons:
         * 1. IMO the user experience is nicer if the screen wont orientate accidentally
         * 2. Launched effect logic would become really cumbersome because it relaunches with
         * every screen orientation even with the key.
         */
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
    }

    private fun enableOrientation() {
        // Release screen when flashing is done
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameSettings = intent.getOptions()
        gameViewModel = ViewModelProvider(
            this@SinglePlayerActivity,
            GameViewModel.Companion.ViewModelFactory(gameSettings)
        )[GameViewModel::class.java]
        if (!loaderViewModel.isLoadingDone.value) {
            gameTimeViewModel.setTime(gameSettings.gameTimeInMinutes.minutes)
            loaderViewModel.setLoadingDone()
        }

        setContent {
            MorseCodeGameTheme {
                val isLoadingDone by loaderViewModel.isLoadingDone.collectAsState()
                var isGameOver by rememberSaveable { mutableStateOf(false) }
                if (isLoadingDone && !isGameOver) {
                    val gameTimeLeft by gameTimeViewModel.timeLeft.collectAsState()
                    val gameQuestionData by gameViewModel.gameViewModelData.collectAsState()
                    var answerBoxOn by rememberSaveable { mutableStateOf(false) }
                    var lightOn by rememberSaveable { mutableStateOf(false) }
                    var showCorrectAnswer by rememberSaveable { mutableStateOf(false) }

                    val gameQuestion = gameQuestionData.question
                    val gameQuestionCount = gameQuestionData.questionNumber
                    val key = gameQuestion.answer + " " + gameQuestionCount
                    LaunchedEffect(key1 = key) {
                        if (!answerBoxOn) {
                            lockScreenOrientation()
                            Log.v(SINGLE_PLAYER_TAG, "launched effect launched with key $key")
                            // Flashing light logic
                            launch {
                                showCorrectAnswer = false
                                delay(WAIT_BEFORE_SENDING_NEXT_SIGNAL)
                                MorseCodeTimer.onOffTimer(
                                    gameQuestion.answer,
                                    gameViewModel.getWordsPerMinute()
                                ) { on: Boolean -> lightOn = on }
                                lightOn = false
                                answerBoxOn = true
                                enableOrientation()
                            }
                        }
                    }
                    Column {
                        StatusBar(
                            left = gameTimeLeft,
                            middle = "$gameQuestionCount/${gameViewModel.getAmountOfQuestions()}",
                            right = "wpm: ${gameViewModel.getWordsPerMinute()}"
                        )
                        SinglePlayerScreen(
                            lightOn = lightOn,
                            answerBoxOn = answerBoxOn,
                            onClickAnswer = {
                                showCorrectAnswer = true
                                launchIOCoroutine {
                                    // Next question logic after answer
                                    delay(WAIT_BEFORE_CLEARING_ANSWERS)
                                    answerBoxOn = false
                                    // With out this wait player would see next answer because
                                    // fading takes time but new answers are already drawn
                                    delay(WAIT_BEFORE_NEXT_QUESTION)
                                    if (
                                        !gameViewModel.hasNextQuestion() ||
                                        !gameTimeViewModel.hasTimeLeft()
                                    ) {
                                        isGameOver = true
                                        gameOver()
                                    } else {
                                        gameViewModel.nextQuestion()
                                    }
                                }
                            },
                            question = gameQuestion,
                            showCorrectAnswer = showCorrectAnswer,
                            onClickCancel = ::cancel
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colors.background)
                            .fillMaxSize()
                    ) {
                        if (isGameOver) {
                            enableOrientation()
                            GameOver(
                                isTimeout = gameViewModel.hasNextQuestion() && !gameTimeViewModel.hasTimeLeft()
                            )
                        } else {
                            Text("Loading...", color = MaterialTheme.colors.primary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SinglePlayerScreen(
    lightOn: Boolean,
    answerBoxOn: Boolean,
    onClickAnswer: (String) -> Unit,
    question: Question,
    showCorrectAnswer: Boolean,
    onClickCancel: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        if (lightOn) {
            Image(
                painterResource(R.drawable.vintage_light_bulb),
                contentDescription = "Flashlight",
                modifier = Modifier
                    .heightIn(min = 200.dp)
                    .widthIn(min = 200.dp)
                    .clip(CircleShape)
                    .rotate(180f)
                    .background(MaterialTheme.colors.primary)
                    .align(Alignment.Center)
            )
        }
        if (!answerBoxOn) {
            val modifier = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Modifier.align(Alignment.BottomCenter).padding(bottom = 30.dp)
            } else {
                Modifier.align(Alignment.CenterEnd).padding(end = 50.dp).clip(CircleShape)
            }
            SharedComposable.DefaultButton(
                configurations = SharedComposable.DefaultButtonConfigurations(
                    text = LocalContext.current.getStringUpper(R.string.common_cancel),
                    click = onClickCancel
                ),
                modifier = modifier
            )
        }
        AnimatedVisibility(
            visible = answerBoxOn,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(
                    color = MaterialTheme.colors.secondaryVariant
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DefaultHeaderText(
                    text = LocalContext.current.getString(R.string.single_player_answer),
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                )
                question.possibleAnswers.forEach { possibleAnswer ->
                    val (buttonColor, textColor) = if (showCorrectAnswer) {
                        val correctAnswer = if (question.isAnswerCorrect(possibleAnswer)) {
                            VintageMint
                        } else {
                            VintageRedDeep
                        }
                        Pair(correctAnswer, MaterialTheme.colors.onPrimary)
                    } else {
                        Pair(MaterialTheme.colors.primary, MaterialTheme.colors.onPrimary)
                    }
                    Button(
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = buttonColor
                        ),
                        onClick = { onClickAnswer(possibleAnswer) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp, start = 5.dp, end = 5.dp, bottom = 2.dp),
                        enabled = !showCorrectAnswer
                    ) {
                        SharedComposable.DefaultText(
                            possibleAnswer.uppercase(),
                            color = textColor,
                            fontFamily = FontFamily.Default
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BoxScope.GameOver(
    isTimeout: Boolean
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .wrapContentSize()
            .align(Alignment.Center)
    ) {
        val reason = if (isTimeout) "TIMEOUT" else "NO MORE QUESTIONS"
        DefaultHeaderText(
            text = "GAME OVER",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            paddingBottom = 5.dp
        )
        TelegraphImage(Modifier.align(Alignment.CenterHorizontally))
        DefaultHeaderText(
            text = reason,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 5.dp),
            paddingBottom = 0.dp
        )
    }
}

@Composable
@Preview
fun SinglePlayerActivityPreview() {
    MorseCodeGameTheme {
        Column(
            modifier = Modifier.background(color = Color.Black)
        ) {
            StatusBar(left = "5:00", middle = "1/10", right = "wpm: 20")
            SinglePlayerScreen(
                lightOn = true,
                answerBoxOn = false,
                onClickAnswer = {},
                question = QuestionGenerator
                    .generateQuestions(1, DifficultLevels.HARD).first(),
                showCorrectAnswer = true,
                onClickCancel = {}
            )
        }
    }
}

@Composable
@Preview
fun SinglePlayerActivityGameOverPreview() {
    MorseCodeGameTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
        ) {
            GameOver(isTimeout = true)
        }
    }
}
