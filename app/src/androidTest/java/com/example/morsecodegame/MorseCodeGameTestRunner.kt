package com.example.morsecodegame

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class MorseCodeGameTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, MorseCodeTestApplication::class.java.name, context)
    }
}
