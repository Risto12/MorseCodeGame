package com.galaxy.morsecodegame

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.galaxy.morsecodegame.db.WarningDataStoreImpl
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

// runBlockingTest -> runTest
// TestCoroutineDispatcher -> StandardTestDispatcher
// TestCouroutineScope -> TestScope
class WarningDataStoreTest {

    private val testKey = booleanPreferencesKey("testKey")

    private fun getWarningStoreAndDataStoreMock(): Pair<DataStore<Preferences>, WarningDataStoreImpl> {
        val dataStore: DataStore<Preferences> = mock()
        val warningDataStore = WarningDataStoreImpl(dataStore)
        return Pair(dataStore, warningDataStore)
    }

    @Test
    fun testLoadingFunctionality() = runBlocking {
        for (v in listOf(null, false, true)) {
            val (dataStore, warningDataStore) = getWarningStoreAndDataStoreMock()
            val preferences: Preferences = mock()
            whenever(preferences[testKey]).thenReturn(v)
            whenever(dataStore.data).thenReturn(flowOf(preferences))
            val result = warningDataStore.load(testKey)
            Assert.assertEquals(v ?: false, result)
        }
    }

    @Test
    fun testSavePreferenceIsCalled() = runBlocking<Unit> {
        val (dataStore, warningDataStore) = getWarningStoreAndDataStoreMock()
        warningDataStore.save(testKey, true)
        val preferences: Preferences = mock()
        whenever(dataStore.edit { }).thenReturn(preferences)
        verify(preferences, times(1))
    }
}
