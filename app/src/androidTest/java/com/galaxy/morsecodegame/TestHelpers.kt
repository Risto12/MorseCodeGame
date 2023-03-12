package com.galaxy.morsecodegame

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.onNodeWithText

fun SemanticsNodeInteractionsProvider.onNodeWithTextIgnore(text: String) =
    onNodeWithText(text, ignoreCase = true)

fun SemanticsNodeInteractionsProvider.onNodeWithTextAndSubStringIgnore(text: String) =
    onNodeWithText(text, ignoreCase = true, substring = true)
