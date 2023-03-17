package com.galaxy.morsecodegame

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galaxy.morsecodegame.configurations.OptionsConfigurations
import com.galaxy.morsecodegame.model.Options
import com.galaxy.morsecodegame.ui.composables.DefaultButton
import com.galaxy.morsecodegame.ui.composables.DefaultButtonConfigurations
import com.galaxy.morsecodegame.ui.composables.DefaultHeaderText
import com.galaxy.morsecodegame.ui.composables.DefaultText
import com.galaxy.morsecodegame.ui.theme.MorseCodeGameTheme
import com.galaxy.morsecodegame.utility.DifficultLevels
import com.galaxy.morsecodegame.utility.getStringUpper
import com.galaxy.morsecodegame.utility.launchIOCoroutine
import com.galaxy.morsecodegame.viewModel.OptionsConfigurationsViewModel
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
                Box (
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.background(color = MaterialTheme.colors.background)
                        .fillMaxSize()
                ) {
                    val configurations by rememberSaveable { mutableStateOf(optionsConfigurations) }
                    val options by optionsConfigurationsViewModel.optionsViewModelData.collectAsState()
                    val localContext = LocalContext.current
                    OptionsScreen(
                        options = options,
                        onGameTimeSliderValueChange = { sliderValue: Float ->
                            optionsConfigurationsViewModel
                                .updateConfiguration(
                                    localContext,
                                    options::gameTimeInMinutes,
                                    sliderValue.roundToInt()
                                )
                        },
                        onWordsPerMinuteSliderValueChange = { sliderValue: Float ->
                            optionsConfigurationsViewModel
                                .updateConfiguration(
                                    localContext,
                                    options::wordsPerMinute,
                                    sliderValue.roundToInt()
                                )
                        },
                        onNumberOfQuestionsSliderValueChange = { sliderValue: Float ->
                            optionsConfigurationsViewModel
                                .updateConfiguration(
                                    localContext,
                                    options::numberOfQuestions,
                                    sliderValue.roundToInt()
                                )
                        },
                        onDifficultLevelRadioButtonChange = { difficultLevel ->
                            optionsConfigurationsViewModel
                                .updateConfiguration(
                                    localContext,
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
                    DefaultButtonConfigurations(
                        text = LocalContext.current.getStringUpper(R.string.common_save),
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
        DefaultHeaderText(
            text = headerText,
            fontSize = 20.sp
        )
        DefaultText(
            text = sliderText,
            color = MaterialTheme.colors.primary
        )
        Slider(
            value = sliderValue,
            onValueChange = onSliderValueChange,
            valueRange = sliderValueRange,
            colors = SliderDefaults.colors(
                activeTrackColor = MaterialTheme.colors.primary,
                thumbColor = MaterialTheme.colors.primaryVariant
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
            DefaultHeaderText(
                text = headerText,
                fontSize = 20.sp,
                paddingBottom = 5.dp
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
                    val radioButtonsColor = MaterialTheme.colors.primary
                    RadioButton(
                        selected = (difficultLevel.name == defaultDifficultLevel),
                        onClick = {
                            onOptionSelected(difficultLevel.name)
                            onClickRadioButton(difficultLevel)
                        },
                        colors = RadioButtonDefaults.colors(
                            radioButtonsColor,
                            radioButtonsColor,
                            radioButtonsColor
                        )
                    )
                    Text(
                        text = difficultLevel.name,
                        color = radioButtonsColor
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
