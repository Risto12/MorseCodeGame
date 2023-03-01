package com.example.morsecodegame.di

import com.example.morsecodegame.di.components.OptionsComponent
import dagger.Component


@Component(modules = [TestConfigurationsModule::class])
interface TestOptionsComponent : OptionsComponent {}