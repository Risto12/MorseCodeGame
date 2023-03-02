package com.example.morsecodegame.di.modules

import com.example.morsecodegame.repository.OptionsRepository
import com.example.morsecodegame.repository.OptionsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class OptionsModule {

    @Binds
    abstract fun optionsRepository(optionsRepositoryImpl: OptionsRepositoryImpl): OptionsRepository

}