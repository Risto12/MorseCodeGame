package com.example.morsecodegame

import android.app.Application
import com.example.morsecodegame.db.AppDatabase
import com.example.morsecodegame.di.components.DaggerOptionsComponent
import com.example.morsecodegame.di.components.OptionsComponent


class MorseCodeGameApplication : Application() {
    val optionsComponent: OptionsComponent by lazy {
        DaggerOptionsComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        AppDatabase.setDatabase(this)
    }
}
