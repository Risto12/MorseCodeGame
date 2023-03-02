package com.example.morsecodegame.di.modules

import com.example.morsecodegame.repository.OptionsRepository
import com.example.morsecodegame.repository.OptionsRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class OptionsModule {

    @Binds
    abstract fun optionsRepository(optionsRepositoryImpl: OptionsRepositoryImpl): OptionsRepository

}