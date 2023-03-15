package com.galaxy.morsecodegame.tests

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.galaxy.morsecodegame.MainActivity
import com.galaxy.morsecodegame.di.FakeDb
import com.galaxy.morsecodegame.onNodeWithTextIgnore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// runBlockingTest -> runTest
// TestCoroutineDispatcher -> StandardTestDispatcher
// TestCouroutineScope -> TestScope
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class PathInstrumentedTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @After
    fun cleaning() {
        FakeDb.resetDb()
    }

    private val flashingAndBlinkingMaxWpm = "wpm: 3"

    private fun toggleSettingsToMaxAndSave() {
        rule.onNodeWithTextIgnore("Options")
            .performClick()
        for (
        slider in listOf("Game time", "Words per minute", "Number of questions")
        ) {
            rule.onNodeWithTag(slider).performTouchInput {
                this.swipeRight(endX = 900.0f, durationMillis = 500)
            }
        }
        rule.onNodeWithTextIgnore("MEDIUM").performClick()
        rule.onNodeWithTextIgnore("save").performClick()

        rule.onNodeWithTextIgnore("WARNING")
            .assertExists()
        rule.onNodeWithTextIgnore("ok")
            .performClick()
    }

    // Testing that options changes are applied
    @Test
    @ExperimentalCoroutinesApi
    @ExperimentalTestApi
    fun testChangingOptionsAndStartingSinglePlayerActivity() {
        toggleSettingsToMaxAndSave()

        rule.onNodeWithTextIgnore("Single player").performClick()
        rule.onNodeWithTextIgnore("blinking light").performClick()

        rule.onNodeWithTextIgnore("1/10").assertExists()
        rule.onNodeWithTextIgnore(flashingAndBlinkingMaxWpm).assertExists()
    }

    @Test
    @ExperimentalCoroutinesApi
    @ExperimentalTestApi
    fun testChangingOptionsAndFlashlightActivity() {
        toggleSettingsToMaxAndSave()

        rule.onNodeWithTextIgnore("multiplayer").performClick()
        rule.onNodeWithTextIgnore("flashlight").performClick()

        rule.onNodeWithTextIgnore(flashingAndBlinkingMaxWpm).assertExists()
    }
}
