package com.galaxy.morsecodegame.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.galaxy.morsecodegame.di.modules.WarningPreferencesDatastoreModule
import com.galaxy.morsecodegame.di.modules.WarningRepositoryModule
import com.galaxy.morsecodegame.repository.WarningDataStore
import com.galaxy.morsecodegame.repository.WarningRepository
import com.galaxy.morsecodegame.repository.WarningRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

const val TEST_DATASTORE = "test_datastore"

object FakeDataStore {
    private var dataStore = mutableMapOf<String, Boolean>()

    fun save(key: Preferences.Key<Boolean>, disabled: Boolean) {
        dataStore[key.name] = disabled
    }

    fun get(key: Preferences.Key<Boolean>): Boolean {
        return dataStore[key.name] ?: false
    }

    fun reset() {
        dataStore = mutableMapOf()
    }
}

class FakeWarningDataStore @Inject constructor() : WarningDataStore {
    override suspend fun save(key: Preferences.Key<Boolean>, disable: Boolean) {
        FakeDataStore.save(key, disable)
    }

    override suspend fun load(key: Preferences.Key<Boolean>): Boolean = FakeDataStore.get(key)
}

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [WarningPreferencesDatastoreModule::class]
)
@Module
object TestWarningPreferencesDatastoreModule {
    @Provides
    @Singleton
    fun testWarningDatastorePreferences(@ApplicationContext appContext: Context): DataStore<Preferences> {
        // quick fix otherwise hilt would complain about multiple datastore open. This was the best
        // solution I could find
        val randomNumber = Random.nextInt()
        return PreferenceDataStoreFactory.create(
            produceFile = { appContext.preferencesDataStoreFile(TEST_DATASTORE) }
        )
    }
}

@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [WarningRepositoryModule::class]
)
@Module
abstract class TestWarningRepositoryModule {

    @Binds
    abstract fun warningRepositoryModule(warningRepositoryImpl: WarningRepositoryImpl): WarningRepository

    @Binds
    abstract fun warningDatastore(warningDataStoreImpl: FakeWarningDataStore): WarningDataStore
}
