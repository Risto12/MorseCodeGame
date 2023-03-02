package com.example.morsecodegame.di.components

import android.content.Context
import com.example.morsecodegame.MainActivity
import com.example.morsecodegame.MorseCodeLettersActivity
import com.example.morsecodegame.OptionsActivity
import com.example.morsecodegame.di.modules.ConfigurationsModule
import dagger.BindsInstance
import dagger.Component


@Component(modules = [ConfigurationsModule::class])
interface ConfigurationsComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ConfigurationsComponent
    }

    fun inject(mainActivity: MainActivity) // TODO move this to OptionsModule when
    fun inject(morseCodeLettersActivity: MorseCodeLettersActivity)
}
