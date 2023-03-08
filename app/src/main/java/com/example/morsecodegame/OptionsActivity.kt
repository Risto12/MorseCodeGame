package com.example.morsecodegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.morsecodegame.composables.SharedComposable
import com.example.morsecodegame.composables.SharedComposable.DefaultButton
import com.example.morsecodegame.configurations.OptionsConfigurations
import com.example.morsecodegame.model.Options
import com.example.morsecodegame.ui.theme.MorseCodeGameTheme
import com.example.morsecodegame.utility.DifficultLevels
import com.example.morsecodegame.utility.launchIOCoroutine
import com.example.morsecodegame.viewModel.OptionsConfigurationsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class OptionsActivity : ComponentActivity() {

    private val optionsConfigurationsViewModel: OptionsConfigurationsViewModel by viewModels()

    @Inject
    lateinit var optionsConfigurations: OptionsConfigurations

    private fun saveChanges() = launchIOCoroutine {
        optionsConfigurationsViewModel.save()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MorseCodeGameTheme {
                val configurations by rememberSaveable { mutableStateOf(optionsConfigurations) }
                val options by optionsConfigurationsViewModel.optionsViewModelData.collectAsState()
                OptionsScreen(
                    options = options,
                    onGameTimeSliderValueChange = { sliderValue: Float ->
                        optionsConfigurationsViewModel
                            .updateConfiguration(
                                this,
                                options::gameTimeInMinutes,
                                sliderValue.roundToInt()
                            )
                    },
                    onWordsPerMinuteSliderValueChange = { sliderValue: Float ->
                        optionsConfigurationsViewModel
                            .updateConfiguration(
                                this,
                                options::wordsPerMinute,
                                sliderValue.roundToInt()
                            )
                    },
                    onNumberOfQuestionsSliderValueChange = { sliderValue: Float ->
                        optionsConfigurationsViewModel
                            .updateConfiguration(
                                this,
                                options::numberOfQuestions,
                                sliderValue.roundToInt()
                            )
                    },
                    onDifficultLevelRadioButtonChange = { difficultLevel ->
                        optionsConfigurationsViewModel
                            .updateConfiguration(
                                this,
                                options::difficultLevel,
                                difficultLevel
                            )
                    },
                    onSaveButtonClicked = { saveChanges() },
                    configurations = configurations
                )
            }
        }
    }
}

@Composable
private fun OptionsScreen(
    options: Options,
    configurations: OptionsConfigurations,
    onGameTimeSliderValueChange: (Float) -> Unit,
    onWordsPerMinuteSliderValueChange: (Float) -> Unit,
    onNumberOfQuestionsSliderValueChange: (Float) -> Unit,
    onDifficultLevelRadioButtonChange: (DifficultLevels) -> Unit,
    onSaveButtonClicked: () -> Unit
) {
    val listState = rememberLazyListState()
    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        state = listState,
        contentPadding = PaddingValues(all = 30.dp),
        modifier = Modifier.background(color = MaterialTheme.colors.background)
    ) {
        item {
            val sliderName = "Game time"
            TextWithSlider(
                headerText = sliderName,
                sliderText = "${(options.gameTimeInMinutes)} minutes",
                sliderValue = options.gameTimeInMinutes.toFloat(),
                onSliderValueChange = onGameTimeSliderValueChange,
                sliderValueRange = configurations.gameTimeMin..configurations.gameTimeMax,
                sliderTestTag = sliderName
            )
        }
        item {
            val sliderName = "Words per minute"
            TextWithSlider(
                headerText = sliderName,
                sliderText = "${(options.wordsPerMinute)}",
                sliderValue = options.wordsPerMinute.toFloat(),
                onSliderValueChange = onWordsPerMinuteSliderValueChange,
                sliderValueRange = configurations.wordsPerMinuteMin..configurations.wordsPerMinuteMax,
                sliderTestTag = sliderName
            )
        }
        item {
            val sliderName = "Number of questions"
            TextWithSlider(
                headerText = sliderName,
                sliderText = "${(options.numberOfQuestions)}",
                sliderValue = options.numberOfQuestions.toFloat(),
                onSliderValueChange = onNumberOfQuestionsSliderValueChange,
                sliderValueRange = configurations.numberOfQuestionsMin..configurations.numberOfQuestionsMax,
                sliderTestTag = sliderName
            )
        }
        item {
            DifficultLevelRadioButtonsWithText(
                headerText = "Difficult level",
                onClickRadioButton = onDifficultLevelRadioButtonChange,
                defaultDifficultLevel = options.difficultLevel.name
            )
        }
        item {
            DefaultButton(
                SharedComposable.DefaultButtonConfigurations(
                    text = "save",
                    click = onSaveButtonClicked
                ),
                modifier = Modifier.padding(top = 20.dp)
            )
        }
    }
}

@Composable
private fun TextWithSlider(
    headerText: String,
    sliderText: String,
    sliderValue: Float,
    onSliderValueChange: (Float) -> Unit,
    sliderValueRange: ClosedFloatingPointRange<Float>,
    sliderTestTag: String
) {
    SharedComposable.DefaultHeaderText(
        text = headerText,
        fontSize = 25.sp
    )
    SharedComposable.DefaultText(
        text = sliderText,
        color = MaterialTheme.colors.onPrimary
    )
    Slider(
        value = sliderValue,
        onValueChange = onSliderValueChange,
        valueRange = sliderValueRange,
        colors = SliderDefaults.colors(
            activeTrackColor = MaterialTheme.colors.onPrimary,
            thumbColor = MaterialTheme.colors.secondary
        ),
        modifier = Modifier
            .padding(bottom = 30.dp)
            .testTag(sliderTestTag)
    )
}

// TODO make more generic
@Composable
fun DifficultLevelRadioButtonsWithText(
    headerText: String,
    onClickRadioButton: (DifficultLevels) -> Unit,
    defaultDifficultLevel: String
) {
    val radioOptions = DifficultLevels.values()
    val (_, onOptionSelected) = remember { mutableStateOf(defaultDifficultLevel) }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        SharedComposable.DefaultHeaderText(
            text = headerText,
            fontSize = 25.sp,
            modifier = Modifier.padding(bottom = 5.dp)
        )
        radioOptions.forEach { difficultLevel ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .selectable(
                        selected = (difficultLevel.name == defaultDifficultLevel),
                        onClick = {
                            onOptionSelected(difficultLevel.name)
                            onClickRadioButton(difficultLevel)
                        }
                    )
                    .fillMaxWidth()
            ) {
                RadioButton(
                    selected = (difficultLevel.name == defaultDifficultLevel),
                    onClick = { // TODO get the medium button on the same vertical level as the other difficulties
                        onOptionSelected(difficultLevel.name)
                        onClickRadioButton(difficultLevel)
                    },
                    colors = RadioButtonDefaults.colors(
                        MaterialTheme.colors.onPrimary,
                        MaterialTheme.colors.onPrimary,
                        MaterialTheme.colors.onPrimary
                    )
                )
                Text(
                    text = difficultLevel.name,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}

@Composable
@Preview
fun OptionsActivityPreview() {
    MorseCodeGameTheme {
        OptionsScreen(
            options = Options(
                gameTimeInMinutes = 3,
                difficultLevel = DifficultLevels.EASY,
                numberOfQuestions = 2,
                wordsPerMinute = 2
            ),
            onGameTimeSliderValueChange = {},
            onWordsPerMinuteSliderValueChange = {},
            onNumberOfQuestionsSliderValueChange = {},
            onDifficultLevelRadioButtonChange = {},
            onSaveButtonClicked = {},
            configurations = OptionsConfigurations(
                1f,
                10f,
                2f,
                15f,
                4f,
                8f
            )
        )
    }
}
