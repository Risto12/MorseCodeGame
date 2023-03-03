package com.example.morsecodegame.di

import com.example.morsecodegame.db.entity.OptionsEntity
import com.example.morsecodegame.di.modules.OptionsRepositoryModule
import com.example.morsecodegame.model.Options
import com.example.morsecodegame.repository.OptionsRepository
import com.example.morsecodegame.utility.DifficultLevels
import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class FakeOptionsRepositoryImpl @Inject constructor() : OptionsRepository {
    private val fakeDb = mutableListOf(
        Options(
            gameTimeInMinutes = 2,
            difficultLevel = DifficultLevels.EASY,
            numberOfQuestions = 2,
            wordsPerMinute = 2
        ).toOptionsEntity()
    )

    override fun create(entity: OptionsEntity) {
        error("Not needed")
    }

    override fun update(entity: OptionsEntity) {
        // TODO not implemented yet
    }

    override fun delete(entity: OptionsEntity) {
        error("Not needed")
    }

    override fun get(id: Int): Flow<OptionsEntity?> = fakeDb.asFlow()
}

@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [OptionsRepositoryModule::class]
)
@Module
abstract class TestRepositoryModule {

    @Binds
    abstract fun optionsRepository(optionsRepositoryImpl: FakeOptionsRepositoryImpl): OptionsRepository
}