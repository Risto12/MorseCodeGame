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
    val rule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @After
    fun after() {
        FakeDb.resetDb()
    }

    private fun getString(id: Int) = rule.activity.baseContext.getString(id)

    /**
     * Testing that route from Start of the app to morse code letters screen and back to start
     * works
     */
    @Test
    fun testRouteToMorseCodeLettersAndBack() {
        // rule.onRoot().printToLog("TAG")
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
        rule.onNodeWithTextIgnore("Single player")
            .performClick()
        rule.onNodeWithContentDescription("Sound game mode info")
            .performClick()
        rule.onNodeWithTextAndSubStringIgnore("Not available in this version")
            .assertExists()

        rule.onNodeWithContentDescription("Blinking light game mode info")
            .performClick()
        rule.onNodeWithTextAndSubStringIgnore("Morse code is send as a blinking light.")
            .assertExists()
    }

    @Test
    fun testSinglePlayerSoundButtonDoesNotTrigger() {
        rule.onNodeWithTextIgnore("Single Player")
            .performClick()
        rule.onNodeWithTextIgnore("Sound")
            .assertExists()
            .assertIsNotEnabled()
    }

    @Test
    fun testMultiplayerLightAndBluetoothInfoBoxesAppears() {
        rule.onNodeWithTextIgnore("Multiplayer")
            .performClick()
        rule.onNodeWithContentDescription("Flash game mode info")
            .performClick()
        rule.onNodeWithTextAndSubStringIgnore(
            "This game mode lets you send morse code with your phones flashlight."
        )
            .assertExists()

        rule.onNodeWithContentDescription("Bluetooth game mode info")
            .performClick()
        rule.onNodeWithTextAndSubStringIgnore("Not available in this version")
            .assertExists()
    }

    @Test
    fun testMultiplayerBluetoothButtonDoesNotTrigger() {
        rule.onNodeWithTextIgnore("Multiplayer")
            .performClick()
        rule.onNodeWithTextIgnore("Bluetooth")
            .assertExists()
            .assertIsNotEnabled()
    }

    @Test
    fun testVersionNumberExists() {
        rule.onNodeWithTextIgnore("v1.0").assertExists()
    }
}
