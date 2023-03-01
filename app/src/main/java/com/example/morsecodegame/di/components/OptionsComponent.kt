package com.example.morsecodegame.di.components

import android.content.Context
import com.example.morsecodegame.OptionsActivity
import com.example.morsecodegame.di.modules.OptionsConfigurationsModule
import dagger.BindsInstance
import dagger.Component

@Component(modules = [OptionsConfigurationsModule::class])
interface OptionsComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): OptionsComponent
    }

    fun inject(optionsActivity: OptionsActivity)
}