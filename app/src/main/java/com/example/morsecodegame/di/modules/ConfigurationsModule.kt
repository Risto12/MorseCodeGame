package com.example.morsecodegame.di.modules

import android.content.Context
import com.example.morsecodegame.R
import com.example.morsecodegame.configurations.ConfigurationsFactory
import com.example.morsecodegame.configurations.MainInfoTextConfigurations
import com.example.morsecodegame.configurations.MorseCodeLettersInfoTextConfiguration
import com.example.morsecodegame.configurations.OptionsConfigurations
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@InstallIn(SingletonComponent::class)
@Module
class ConfigurationsModule  {

    @Provides
    fun morseCodeLettersInfoTextConfiguration(@ApplicationContext context: Context): MorseCodeLettersInfoTextConfiguration {
        return ConfigurationsFactory.configurationsFactory(
            context = context,
            configurationGenerator = MorseCodeLettersInfoTextConfiguration
                .MorseCodeLetterFactoryConfigurationsBuilder,
            resourceId = R.raw.morseletter
        )
    }

    @Provides
    fun mainInfoTextConfigurations(@ApplicationContext context: Context): MainInfoTextConfigurations {
        return ConfigurationsFactory.configurationsFactory(
            context = context,
            configurationGenerator = MainInfoTextConfigurations.MainInfoTextConfigurationsGenerator,
            resourceId = R.raw.main
        )
    }

    @Provides
    fun optionsConfigurations(@ApplicationContext context: Context): OptionsConfigurations { // TODO Chck ApplicationContext
        return ConfigurationsFactory.configurationsFactory(
            context = context,
            configurationGenerator = OptionsConfigurations
                .MorseCodeLetterFactoryConfigurationsGenerator,
            resourceId = R.raw.options
        )
    }
}