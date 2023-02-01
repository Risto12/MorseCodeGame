package com.example.morsecodegame

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Practising instrument testing
 * TODO make tests that toggles activities buttons and check that not exceptions occur for 1.0b version
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val rule = createAndroidComposeRule<MorseCodeLettersActivity>()

    @Test
    fun useAppContext() {
        rule.onNodeWithText("Overview", ignoreCase = true, useUnmergedTree = true).assertExists()
    }
}