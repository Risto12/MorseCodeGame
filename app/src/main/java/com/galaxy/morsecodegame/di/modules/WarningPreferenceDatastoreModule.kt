package com.galaxy.morsecodegame.di.modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

const val POPUP_DATASTORE = "popup_datastore"

@Module
@InstallIn(SingletonComponent::class)
class WarningPreferencesDatastoreModule {

    @Provides
    @Singleton
    fun warningDatastorePreferences(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { appContext.preferencesDataStoreFile(POPUP_DATASTORE) }
        )
    }
}
