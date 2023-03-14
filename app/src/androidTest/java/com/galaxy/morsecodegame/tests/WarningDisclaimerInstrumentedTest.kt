package com.galaxy.morsecodegame.tests

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.datastore.core.DataStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.galaxy.morsecodegame.MainActivity
import com.galaxy.morsecodegame.di.FakeDb
import com.galaxy.morsecodegame.onNodeWithTextAndSubStringIgnore
import com.galaxy.morsecodegame.onNodeWithTextIgnore
import com.galaxy.morsecodegame.viewModel.WarningViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class WarningDisclaimerInstrumentedTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()


    @Before
    fun before() {

    }

    @After
    fun after() {

    }

    @Test
    fun testWarningDisclaimerIsShown() {
        rule.onNodeWithTextIgnore("WARNING")
            .assertExists()
        rule.onNodeWithTextAndSubStringIgnore("This app contains fast flashing images and lights.")
            .assertExists()
        rule.onNodeWithTextIgnore("OK")
            .assertExists()
    }


    @Test
    fun testWarningDisclaimerNotShowAgainIsNotShownAgain() {

    }


}