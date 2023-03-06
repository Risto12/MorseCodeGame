package com.example.morsecodegame.tests

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.morsecodegame.OptionsActivity
import com.example.morsecodegame.onNodeWithTextIgnore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class OptionsActivityInstrumentedTest {

    @get:Rule
    val rule = createAndroidComposeRule<OptionsActivity>()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Test
    fun testLabelsExists() {
        for (
        label in listOf(
            "Game time",
            "words per minute",
            "number of questions",
            "Difficult level"
        )
        ) {
            rule.onNodeWithTextIgnore(label)
                .assertExists()
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    @ExperimentalTestApi
    fun testConfigurationsSlidersMaxMinValuesAreRespected() = runTest {
        data class SliderValues(val name: String, val maxValue: String, val minValue: String)
        for (slider in listOf(
            SliderValues("Game time", "30 Minutes", "1 Minutes"),
            SliderValues("Words per minute", "10", "1"),
            SliderValues("Number of questions", "10", "2")
        )) {
            rule.onNodeWithTag(slider.name).performTouchInput {
                this.swipeRight(endX = 900.0f, durationMillis = 1000)
            }
            rule.onNodeWithTextIgnore(slider.maxValue).assertExists()

            rule.onNodeWithTag(slider.name).performTouchInput {
                this.swipeLeft(endX = -900.0f, durationMillis = 1000)
            }
            rule.onNodeWithTextIgnore(slider.minValue).assertExists()
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    @ExperimentalTestApi
    fun testDifficultLevelRadioButtons() = runTest {
        rule.onNodeWithTextIgnore("HARD").performClick().assertIsSelected()
        rule.onNodeWithTextIgnore("EASY").assertIsNotSelected()
        rule.onNodeWithTextIgnore("EASY").performClick().assertIsSelected()
        rule.onNodeWithTextIgnore("MEDIUM").performClick().assertIsSelected()
    }
}
