package com.example.morsecodegame.di

import com.example.morsecodegame.configurations.MainInfoTextConfigurations
import com.example.morsecodegame.configurations.MorseCodeLettersInfoTextConfiguration
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
    fun morseCodeLettersInfoTextConfiguration(): MorseCodeLettersInfoTextConfiguration {
        return MorseCodeLettersInfoTextConfiguration(
            overview = "Test overview",
            wordsPerMinute = "2",
            example = "test example"
        )
    }

    @Provides
    fun mainInfoTextConfigurations(): MainInfoTextConfigurations {
        return MainInfoTextConfigurations(
            appVersion = "1.0",
            blinkingLightInfo = "blinking info",
            soundInfo = "sound info",
            flashlightInfo = "flash info",
            bluetoothInfo = "bluetooth info"
        )
    }

    @Provides
    fun optionsConfigurations(): OptionsConfigurations {
        return OptionsConfigurations(
            gameTimeMax = 30.0f,
            gameTimeMin = 1.0f,
            wordsPerMinuteMax = 10.0f,
            wordsPerMinuteMin = 1.0f,
            numberOfQuestionsMax = 10.0f,
            numberOfQuestionsMin = 2.0f
        )
    }
}
