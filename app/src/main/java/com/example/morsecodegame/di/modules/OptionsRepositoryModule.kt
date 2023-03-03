package com.example.morsecodegame.di.modules

import com.example.morsecodegame.repository.OptionsRepository
import com.example.morsecodegame.repository.OptionsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class OptionsRepositoryModule {

    @Binds
    abstract fun optionsRepository(optionsRepositoryImpl: OptionsRepositoryImpl): OptionsRepository
}
