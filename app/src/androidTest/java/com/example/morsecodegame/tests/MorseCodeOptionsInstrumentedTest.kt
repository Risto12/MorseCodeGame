package com.example.morsecodegame.tests

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.morsecodegame.OptionsActivity
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MorseCodeOptionsInstrumentedTest {

    @get:Rule
    val rule = createAndroidComposeRule<OptionsActivity>()

    @Test
    fun testCorrectConfigsAreLoadedFromDb() {
        // TODO
    }

    @Test
    fun testOnGoingModificationsAreSavedDuringConfigurationChange() {
        // TODO
    }

    @Test
    fun testConfigsAreSavedToDb() {
        // TODO
    }
}
