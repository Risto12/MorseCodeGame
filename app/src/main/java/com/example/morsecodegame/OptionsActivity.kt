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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.morsecodegame.composables.DEFAULT_THEME_COLOR
import com.example.morsecodegame.composables.SharedComposable
import com.example.morsecodegame.composables.SharedComposable.DefaultButton
import com.example.morsecodegame.configurations.ConfigurationsFactory
import com.example.morsecodegame.configurations.OptionsConfigurations
import com.example.morsecodegame.di.components.OptionsComponent
import com.example.morsecodegame.model.Options
import com.example.morsecodegame.ui.theme.MorseCodeGameTheme
import com.example.morsecodegame.utility.DifficultLevels
import com.example.morsecodegame.utility.launchIOCoroutine
import com.example.morsecodegame.viewModel.OptionsViewModel
import javax.inject.Inject
import kotlin.math.roundToInt

class OptionsActivity : ComponentActivity() {

    private val optionsViewModel: OptionsViewModel by viewModels()

    @Inject
    lateinit var optionsConfigurations: OptionsConfigurations

    private fun saveChanges() = launchIOCoroutine {
        optionsViewModel.save()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MorseCodeGameApplication).optionsComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            MorseCodeGameTheme {
                val configurations by rememberSaveable { mutableStateOf(optionsConfigurations) }
                val options by optionsViewModel.optionsViewModelData.collectAsState()
                OptionsScreen(
                    options = options,
                    onGameTimeSliderValueChange = { sliderValue: Float ->
                        optionsViewModel
                            .updateConfiguration(
                                this,
                                options::gameTimeInMinutes,
                                sliderValue.roundToInt()
                            )
                    },
                    onWordsPerMinuteSliderValueChange = { sliderValue: Float ->
                        optionsViewModel
                            .updateConfiguration(
                                this,
                                options::wordsPerMinute,
                                sliderValue.roundToInt()
                            )
                    },
                    onNumberOfQuestionsSliderValueChange = { sliderValue: Float ->
                        optionsViewModel
                            .updateConfiguration(
                                this,
                                options::numberOfQuestions,
                                sliderValue.roundToInt()
                            )
                    },
                    onDifficultLevelRadioButtonChange = { difficultLevel ->
                        optionsViewModel
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
        modifier = Modifier.background(color = Color.White)
    ) {
        item {
            TextWithSlider(
                headerText = "Game time",
                sliderText = "${(options.gameTimeInMinutes)} minutes",
                sliderValue = options.gameTimeInMinutes.toFloat(),
                onSliderValueChange = onGameTimeSliderValueChange,
                sliderValueRange = configurations.gameTimeMin..configurations.gameTimeMax
            )
        }
        item {
            TextWithSlider(
                headerText = "words per minute",
                sliderText = "${(options.wordsPerMinute)}",
                sliderValue = options.wordsPerMinute.toFloat(),
                onSliderValueChange = onWordsPerMinuteSliderValueChange,
                sliderValueRange = configurations.wordsPerMinuteMin..configurations.wordsPerMinuteMax
            )
        }
        item {
            TextWithSlider(
                headerText = "Number of questions",
                sliderText = "${(options.numberOfQuestions)}",
                sliderValue = options.numberOfQuestions.toFloat(),
                onSliderValueChange = onNumberOfQuestionsSliderValueChange,
                sliderValueRange = configurations.numberOfQuestionsMin..configurations.numberOfQuestionsMax
            )
        }
        item {
            DifficultLevelRadioButtonsWithText(
                headerText = "Difficult level:",
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
    sliderValueRange: ClosedFloatingPointRange<Float>
) {
    SharedComposable.DefaultHeaderText(
        text = headerText,
        fontSize = 25.sp
    )
    SharedComposable.DefaultText(
        text = sliderText
    )
    Slider(
        value = sliderValue,
        onValueChange = onSliderValueChange,
        valueRange = sliderValueRange,
        colors = SliderDefaults.colors(
            activeTrackColor = DEFAULT_THEME_COLOR,
            thumbColor = DEFAULT_THEME_COLOR
        ),
        modifier = Modifier.padding(bottom = 30.dp)
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
                    }
                )
                Text(
                    text = difficultLevel.name
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
