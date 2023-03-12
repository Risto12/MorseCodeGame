package com.galaxy.morsecodegame.utility

import androidx.lifecycle.LifecycleOwner

interface DebugLifecycleObserver {
    fun addDebugLifecycleObserver(lifecycleOwner: LifecycleOwner)
}
