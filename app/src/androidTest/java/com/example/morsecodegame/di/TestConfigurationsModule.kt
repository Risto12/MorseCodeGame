package com.example.morsecodegame.di

import com.example.morsecodegame.configurations.MainInfoTextConfigurations
import com.example.morsecodegame.configurations.OptionsConfigurations
import com.example.morsecodegame.di.modules.ConfigurationsModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ConfigurationsModule::class]
)
@Module
class TestConfigurationsModule {
    @Provides
    fun mainInfoTextConfigurations(): MainInfoTextConfigurations {
        return MainInfoTextConfigurations(
            appVersion = "1.0"
        )
    }

    @Provides
    fun optionsConfigurations(): OptionsConfigurations {
        return OptionsConfigurations(
            gameTimeMax = 30.0f,
            gameTimeMin = 1.0f,
            wordsPerMinuteMax = 11.0f,
            wordsPerMinuteMin = 1.0f,
            numberOfQuestionsMax = 10.0f,
            numberOfQuestionsMin = 2.0f
        )
    }
}
