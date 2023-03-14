package com.galaxy.morsecodegame.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.galaxy.morsecodegame.di.modules.OptionsRepositoryModule
import com.galaxy.morsecodegame.di.modules.WarningPreferencesDatastoreModule
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.test.TestScope
import javax.inject.Singleton


const val TEST_DATASTORE = "test_datastore"

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [WarningPreferencesDatastoreModule::class]
)
@Module
object TestWarningPreferencesDatastoreModule {
    @Provides
    @Singleton
    fun testWarningDatastorePreferences(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { appContext.preferencesDataStoreFile(TEST_DATASTORE) },
        )
    }
}

