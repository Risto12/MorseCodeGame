package com.example.morsecodegame.utility

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class LifecycleDebugLogger(private val tag: String = "debugLogger") :
    LifecycleEventObserver, DebugLifecycleObserver {

    override fun addDebugLifecycleObserver(lifecycleOwner: LifecycleOwner) =
        lifecycleOwner.lifecycle.addObserver(this)

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        Log.d(tag, "${event.name} was triggered")
    }
}
