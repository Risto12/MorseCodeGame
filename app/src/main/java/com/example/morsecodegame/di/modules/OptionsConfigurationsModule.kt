package com.example.morsecodegame.di.modules

import android.content.Context
import com.example.morsecodegame.R
import com.example.morsecodegame.configurations.ConfigurationsFactory
import com.example.morsecodegame.configurations.OptionsConfigurations
import dagger.Module
import dagger.Provides

@Module
class OptionsConfigurationsModule {

    @Provides
    fun configurations(context: Context): OptionsConfigurations {
        return ConfigurationsFactory.configurationsFactory(
            context = context,
            configurationBuilder = OptionsConfigurations
                .MorseCodeLetterFactoryConfigurationsBuilder,
            resourceId = R.raw.options
        )
    }
}