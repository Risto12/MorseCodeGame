package com.galaxy.morsecodegame.repository

import androidx.datastore.preferences.core.Preferences
import javax.inject.Inject


class WarningRepositoryImpl @Inject constructor(private val dataStore: WarningDataStore) :
    WarningRepository {

    override suspend fun save(
        key: Preferences.Key<Boolean>,
        disable: Boolean
    ) = dataStore.save(key, disable)


    override suspend fun load(key: Preferences.Key<Boolean>): Boolean =
        dataStore.load(key)

}
