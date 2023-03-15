package com.galaxy.morsecodegame.tests

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.galaxy.morsecodegame.MainActivity
import com.galaxy.morsecodegame.di.FakeDb
import com.galaxy.morsecodegame.onNodeWithTextAndSubStringIgnore
import com.galaxy.morsecodegame.onNodeWithTextIgnore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MainActivityInstrumentedTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @After
    fun after() {
        FakeDb.resetDb()
    }

    @Test
    fun testWarningDisclaimerIsShown() {
        rule.onNodeWithTextIgnore("WARNING")
            .assertExists()
        rule.onNodeWithTextAndSubStringIgnore(
            "This app contains fast flashing images and lights.\n\nIt may cause discomfort and trigger seizures for people with photosensitive epilepsy.\n\nIf you experience any adverse symptoms, such as dizziness, disorientation, or seizures, please immediately discontinue use and consult with a medical professional."
        )
            .assertExists()
        rule.onNodeWithTextIgnore("OK")
            .assertExists()
    }

    private fun clickWarningDisclaimerOk() {
        rule.onNodeWithTextIgnore("WARNING")
            .assertExists()
        rule.onNodeWithTextIgnore("ok")
            .performClick()
    }

    /**
     * Testing that route from Start of the app to morse code letters screen and back to start
     * works
     */
    @Test
    fun testRouteToMorseCodeLettersAndBack() {
        clickWarningDisclaimerOk()
        rule.onNodeWithTextIgnore("Morse Code")
            .performClick()
        rule.onNodeWithText("Overview")
            .assertExists()
        rule.onNodeWithTextIgnore("Back")
            .performClick()
        rule.onNodeWithText("Morse Code Game")
            .assertExists()
    }

    @Test
    fun testSinglePlayerLightAndSoundInfoBoxesAppears() {
        clickWarningDisclaimerOk()
        rule.onNodeWithTextIgnore("Single player")
            .performClick()
        rule.onNodeWithContentDescription("Sound game mode info")
            .performClick()
        rule.onNodeWithTextAndSubStringIgnore("Not available in this version")
            .assertExists()

        rule.onNodeWithContentDescription("Blinking light game mode info")
            .performClick()
        rule.onNodeWithTextAndSubStringIgnore("Morse code is send as a blinking light. Your chosen difficult level will determine is the morse one letter, one word, or multiple words.\n\nThe blinking speed is determined by your wpm setting. The maximum wpm is limited to $MAX_WPM.\n\nThe game will end when the limit of available questions is reached or the timer reaches 0. You can still answer the last question after the time is up.\n\nTo alter the settings go to options menu.")
            .assertExists()
        rule.onNodeWithTextAndSubStringIgnore(
            "This mode contains fast flashing images.\n\nIt may cause discomfort and trigger seizures for people with photosensitive epilepsy.\n\nIf you experience any adverse symptoms, such as dizziness, disorientation, or seizures, please immediately discontinue use and consult with a medical professional."
        )
            .assertExists()
    }

    @Test
    fun testSinglePlayerSoundButtonDoesNotTrigger() {
        clickWarningDisclaimerOk()
        rule.onNodeWithTextIgnore("Single Player")
            .performClick()
        rule.onNodeWithTextIgnore("Sound")
            .assertExists()
            .assertIsNotEnabled()
    }

    @Test
    fun testMultiplayerLightAndBluetoothInfoBoxesAppears() {
        clickWarningDisclaimerOk()
        rule.onNodeWithTextIgnore("Multiplayer")
            .performClick()
        rule.onNodeWithContentDescription("Flash game mode info")
            .performClick()
        rule.onNodeWithTextAndSubStringIgnore(
            "This game mode lets you send morse code with your phone's flashlight.\n\nCheck that no app is using the flashlight before you play e.g. camera.\n\nThe speed of the flash is determined by wpm setting that you can alter from options but the max wpm is limited to $MAX_WPM.\n\nTo alter the settings go to options menu."
        ).assertExists()
        rule.onNodeWithTextAndSubStringIgnore(
          "This mode contains fast flashing lights.\n\nIt may cause discomfort and trigger seizures for people with photosensitive epilepsy.\n\nIf you experience any adverse symptoms, such as dizziness, disorientation, or seizures, please immediately discontinue use and consult with a medical professional."
        )
            .assertExists()

        rule.onNodeWithContentDescription("Bluetooth game mode info")
            .performClick()
        rule.onNodeWithTextAndSubStringIgnore("Not available in this version")
            .assertExists()
    }

    @Test
    fun testMultiplayerBluetoothButtonDoesNotTrigger() {
        clickWarningDisclaimerOk()
        rule.onNodeWithTextIgnore("Multiplayer")
            .performClick()
        rule.onNodeWithTextIgnore("Bluetooth")
            .assertExists()
            .assertIsNotEnabled()
    }

    @Test
    fun testVersionNumberExists() {
        clickWarningDisclaimerOk()
        rule.onNodeWithTextIgnore("v1.0").assertExists()
    }
}
