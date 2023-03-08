package com.example.morsecodegame.di

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.morsecodegame.db.AppDatabase
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
import kotlinx.coroutines.flow.*

object FakeDb {

    private val defaultOptions = Options(
        gameTimeInMinutes = 2,
        difficultLevel = DifficultLevels.EASY,
        numberOfQuestions = 2,
        wordsPerMinute = 2
    ).toOptionsEntity()

    val db: AppDatabase = initDatabase()

    private fun initDatabase(): AppDatabase {
        synchronized(this) {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val room = Room.inMemoryDatabaseBuilder(
                context,
                AppDatabase::class.java
            ).build()
            room.optionsDao().createOptions(defaultOptions)
            return room
        }
    }

    fun resetDb() {
        db.optionsDao().updateOptions(defaultOptions)
    }
}

class FakeOptionsRepositoryImpl @Inject constructor() : OptionsRepository {

    override fun create(entity: OptionsEntity) {
        error("Not needed")
    }

    override fun update(entity: OptionsEntity) {
        FakeDb.db.optionsDao().updateOptions(entity)
    }

    override fun delete(entity: OptionsEntity) {
        error("Not needed")
    }

    override fun get(id: Int): Flow<OptionsEntity?> = FakeDb.db.optionsDao().getOptions()
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
