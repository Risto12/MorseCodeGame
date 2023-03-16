package com.galaxy.morsecodegame.tests

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.galaxy.morsecodegame.MainActivity
import com.galaxy.morsecodegame.di.FakeDataStore
import com.galaxy.morsecodegame.onNodeWithTextAndSubStringIgnore
import com.galaxy.morsecodegame.onNodeWithTextIgnore
import com.galaxy.morsecodegame.viewModel.WARNING_DATA_STORE_KEY
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class WarningDisclaimerTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @After
    fun after() {
        FakeDataStore.reset()
    }

    private val warningDisclaimerText = "This app contains fast flashing images and lights.\n\nIt may cause discomfort and trigger seizures for people with photosensitive epilepsy.\n\nIf you experience any adverse symptoms, such as dizziness, disorientation, or seizures, please immediately discontinue use and consult with a medical professional."

    private fun getDataStoreValue() = FakeDataStore.get(
        booleanPreferencesKey(WARNING_DATA_STORE_KEY)
    )

    private fun disclaimerExistsCheck() {
        rule.onNodeWithTextIgnore("WARNING")
            .assertExists()
        rule.onNodeWithTextAndSubStringIgnore(warningDisclaimerText)
            .assertExists()
    }

    private fun clickOk() {
        rule.onNodeWithTextIgnore("OK")
            .assertExists()
            .performClick()
    }

    @Test
    fun testWarningDisclaimerIsShown() {
        disclaimerExistsCheck()
        clickOk()
    }

    @Test
    fun testWarningDisclaimerDisabledCheckBoxIsSaved() {
        Assert.assertEquals(false, getDataStoreValue())
        disclaimerExistsCheck()
        rule.onNodeWithTag("disclaimerCheckBox")
            .performClick()
        clickOk()
        Assert.assertEquals(true, getDataStoreValue())
    }
}

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class TestWarningDisclaimerDisabled {

    init {
        FakeDataStore.save(booleanPreferencesKey(WARNING_DATA_STORE_KEY), true)
    }

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @After
    fun after() {
        FakeDataStore.reset()
    }

    @Test
    fun testWarningDisclaimerIsDisabled() {
        rule.onNodeWithTextIgnore("WARNING").assertDoesNotExist()
    }
}
