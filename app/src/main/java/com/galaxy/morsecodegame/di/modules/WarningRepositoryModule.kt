package com.galaxy.morsecodegame.di.modules

import com.galaxy.morsecodegame.repository.WarningRepository
import com.galaxy.morsecodegame.repository.WarningRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class WarningRepositoryModule {

    @Binds
    abstract fun warningRepositoryModule(warningRepositoryImpl: WarningRepositoryImpl): WarningRepository

}