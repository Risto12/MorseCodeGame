package com.galaxy.morsecodegame.repository

import androidx.datastore.preferences.core.Preferences

/**
 * This wrapper is needed because creating hilt provider for tests that would create test data store
 * created issues
 * https://stackoverflow.com/questions/70847060/there-are-multiple-datastores-active-for-the-same-file-in-hiltandroidtest
 * This was the easiest and best solution to solve this.
 */
interface WarningDataStore {
    suspend fun save(key: Preferences.Key<Boolean>, disable: Boolean)
    suspend fun load(key: Preferences.Key<Boolean>): Boolean
}
