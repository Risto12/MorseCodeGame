package com.galaxy.morsecodegame.di.modules

import android.content.Context
import com.galaxy.morsecodegame.R
import com.galaxy.morsecodegame.configurations.ConfigurationsFactory
import com.galaxy.morsecodegame.configurations.MainInfoTextConfigurations
import com.galaxy.morsecodegame.configurations.OptionsConfigurations
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
