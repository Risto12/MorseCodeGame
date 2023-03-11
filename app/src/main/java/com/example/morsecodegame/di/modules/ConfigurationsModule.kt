package com.example.morsecodegame.di.modules

import android.content.Context
import com.example.morsecodegame.R
import com.example.morsecodegame.configurations.ConfigurationsFactory
import com.example.morsecodegame.configurations.MainInfoTextConfigurations
import com.example.morsecodegame.configurations.OptionsConfigurations
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class ConfigurationsModule {

    @Provides
    fun mainInfoTextConfigurations(@ApplicationContext context: Context): MainInfoTextConfigurations {
        return ConfigurationsFactory.configurationsFactory(
            context = context,
            configurationGenerator = MainInfoTextConfigurations.MainInfoTextConfigurationsGenerator,
            resourceId = R.raw.main
        )
    }

    @Provides
    fun optionsConfigurations(@ApplicationContext context: Context): OptionsConfigurations {
        return ConfigurationsFactory.configurationsFactory(
            context = context,
            configurationGenerator = OptionsConfigurations
                .MorseCodeLetterFactoryConfigurationsGenerator,
            resourceId = R.raw.options
        )
    }
}
