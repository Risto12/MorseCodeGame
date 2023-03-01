package com.example.morsecodegame.di.components

import android.content.Context
import com.example.morsecodegame.MainActivity
import com.example.morsecodegame.OptionsActivity
import com.example.morsecodegame.di.modules.ConfigurationsModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Component(modules = [ConfigurationsModule::class])
interface OptionsComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): OptionsComponent
    }

    fun inject(optionsActivity: OptionsActivity)
    fun inject(mainActivity: MainActivity)
}