package com.galaxy.morsecodegame

import android.app.Application
import com.galaxy.morsecodegame.db.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class MorseCodeGameApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppDatabase.setDatabase(this)
    }
}
