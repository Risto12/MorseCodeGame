package com.galaxy.morsecodegame.utility

import kotlinx.coroutines.*

fun launchIOCoroutine(block: suspend CoroutineScope.() -> Unit): Job =
    CoroutineScope(context = Dispatchers.IO).launch(block = block)
