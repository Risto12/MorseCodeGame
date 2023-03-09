package com.example.morsecodegame

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.morsecodegame.ui.composables.SharedComposable
import com.example.morsecodegame.ui.composables.SharedComposable.DefaultHeaderText
import com.example.morsecodegame.ui.theme.*
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
                            showCorrectAnswer = showCorrectAnswer
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colors.background)
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
    Row(Modifier.heightIn(max = 30.dp).background(color = VintageRedDark)) {
        val textColor = MaterialTheme.colors.primary
        Text(
            text = left,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1.6f),
            color = textColor
        )
        Text(
            text = middle,
            modifier = Modifier.weight(1.2f),
            color = textColor
        )
        Text(
            text = right,
            modifier = Modifier.weight(0.7f),
            color = textColor
        )
    }
}

@Composable
private fun SinglePlayerScreen(
    lightOn: Boolean,
    answerBoxOn: Boolean,
    onClickAnswer: (String) -> Unit,
    question: Question,
    showCorrectAnswer: Boolean
) {
    Box(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colors.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Divider(
                color = MaterialTheme.colors.background,
                modifier = Modifier.weight(0.1f)
            )
            val lightColor = if (lightOn) VintageYellow else MaterialTheme.colors.background
            Box(
                modifier = Modifier
                    .clip(RectangleShape)
                    .background(lightColor)
                    .size(100.dp)
                    .weight(1f)
                    .align(Alignment.CenterHorizontally)
            )
            Box(modifier = Modifier.weight(2f)) {
                Image(
                    painterResource(R.drawable.baseline_flashlight_on_24),
                    contentDescription = "Flashlight",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        AnimatedVisibility(
            visible = answerBoxOn,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter).background(
                color = MaterialTheme.colors.background
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
                            Color.Green
                        } else {
                            Color.Red
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
                            .padding(top = 2.dp, bottom = 2.dp),
                        enabled = !showCorrectAnswer
                    ) {
                        Text(possibleAnswer, color = textColor, fontWeight = FontWeight.Bold)
                    }
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
            SinglePlayerScreen(
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
