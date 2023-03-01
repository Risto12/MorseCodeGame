package com.example.morsecodegame.di.modules

import android.content.Context
import com.example.morsecodegame.R
import com.example.morsecodegame.configurations.ConfigurationsFactory
import com.example.morsecodegame.configurations.MainInfoTextConfigurations
import com.example.morsecodegame.configurations.OptionsConfigurations
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Module
class ConfigurationsModule {

    @Provides
    fun mainInfoTextConfigurations(context: Context): MainInfoTextConfigurations {
        return ConfigurationsFactory.configurationsFactory(
            context = context,
            configurationGenerator = MainInfoTextConfigurations.MainInfoTextConfigurationsGenerator,
            resourceId = R.raw.main
        )
    }

    @Provides
    fun optionsConfigurations(context: Context): OptionsConfigurations {
        return ConfigurationsFactory.configurationsFactory(
            context = context,
            configurationGenerator = OptionsConfigurations
                .MorseCodeLetterFactoryConfigurationsGenerator,
            resourceId = R.raw.options
        )
    }
}