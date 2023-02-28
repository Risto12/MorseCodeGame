package com.example.morsecodegame

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.morsecodegame.composables.GameScreen
import com.example.morsecodegame.composables.SharedComposable
import com.example.morsecodegame.ui.theme.MorseCodeGameTheme
import com.example.morsecodegame.utility.*
import com.example.morsecodegame.viewModel.GameTimeViewModel
import com.example.morsecodegame.viewModel.GameViewModel
import com.example.morsecodegame.viewModel.LoaderViewModel
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

    private fun gameOver() = lifecycleScope.launch(Dispatchers.Default) {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            delay(8000)
            finish()
        }
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

                    Column(
                        modifier = Modifier.background(color = Color.Black)
                    ) {
                        StatusBar(
                            left = gameTimeLeft,
                            middle = "$gameQuestionCount/${gameViewModel.getAmountOfQuestions()}",
                            right = "wpm: ${gameViewModel.getWordsPerMinute()}"
                        )
                        GameScreen(R.drawable.sea_red_moon) {
                            AnswerBox(
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
                                showCorrectAnswer = showCorrectAnswer
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .background(Color.Black)
                            .fillMaxSize()
                    ) {
                        if (isGameOver) {
                            enableOrientation()
                            val reason = if (
                                gameViewModel.hasNextQuestion() && !gameTimeViewModel.hasTimeLeft()
                            ) {
                                "TIMEOUT... TAN TAN TAA...."
                            } else {
                                "TAN TAN TAA...."
                            }
                            SharedComposable.DefaultHeaderText(
                                text = "GAME OVER... $reason"
                            )
                        } else {
                            Text("Loading...", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBar(
    left: String,
    middle: String,
    right: String
) {
    Row(Modifier.heightIn(max = 30.dp)) {
        Text(
            text = left,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1.6f),
            color = Color.White
        )
        Text(
            text = middle,
            modifier = Modifier.weight(1.2f),
            color = Color.White
        )
        Text(
            text = right,
            modifier = Modifier.weight(0.7f),
            color = Color.White
        )
    }
}

@Composable
private fun BoxScope.AnswerBox(
    lightOn: Boolean,
    answerBoxOn: Boolean,
    onClickAnswer: (String) -> Unit,
    question: Question,
    showCorrectAnswer: Boolean
) {
    if (lightOn) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .clip(CircleShape)
                .background(Color.Yellow.copy(alpha = 0.9f))
                .size(13.dp)
        )
    }
    AnimatedVisibility(
        visible = answerBoxOn,
        modifier = Modifier.align(Alignment.BottomCenter),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            modifier = Modifier
                // .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            question.possibleAnswers.forEach { possibleAnswer ->
                val buttonColor = if (showCorrectAnswer) {
                    if (question.isAnswerCorrect(possibleAnswer)) Color.Green else Color.Red
                } else {
                    Color.Magenta
                }
                Button(
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = buttonColor
                    ),
                    onClick = { onClickAnswer(possibleAnswer) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp, bottom = 2.dp),
                    enabled = !showCorrectAnswer
                ) {
                    Text(possibleAnswer, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
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
            GameScreen(R.drawable.sea_red_moon) {
                AnswerBox(
                    lightOn = true,
                    answerBoxOn = true,
                    onClickAnswer = {},
                    question = QuestionGenerator
                        .generateQuestions(1, DifficultLevels.HARD).first(),
                    showCorrectAnswer = true
                )
            }
        }
    }
}