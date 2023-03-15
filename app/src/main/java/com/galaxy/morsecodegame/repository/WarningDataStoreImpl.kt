package com.galaxy.morsecodegame.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class WarningDataStoreImpl @Inject constructor(private val dataStore: DataStore<Preferences>) : WarningDataStore {
    override suspend fun save(key: Preferences.Key<Boolean>, disable: Boolean) {
        dataStore.edit {
            it[key] = disable
        }
    }

    override suspend fun load(key: Preferences.Key<Boolean>): Boolean {
        val pref = dataStore.data.first()
        return pref[key] ?: false
    }
}
