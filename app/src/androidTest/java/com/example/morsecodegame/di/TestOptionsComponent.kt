package com.example.morsecodegame.di

import dagger.Component


@Component(modules = [TestConfigurationsModule::class])
interface TestOptionsComponent : OptionsComponent {}