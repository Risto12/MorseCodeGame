package com.example.morsecodegame

import android.app.Application
import android.os.Environment
import android.util.Log
import com.example.morsecodegame.configurations.ConfigurationsFactory
import com.example.morsecodegame.db.AppDatabase
import com.example.morsecodegame.utility.launchIOCoroutine

class MorseCodeGameApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.setDatabase(this)
    }
}