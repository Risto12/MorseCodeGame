package com.example.morsecodegame

import android.app.Application
import com.example.morsecodegame.db.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class MorseCodeGameApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppDatabase.setDatabase(this)
    }
}
