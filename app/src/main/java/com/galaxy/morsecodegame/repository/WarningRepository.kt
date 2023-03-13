package com.galaxy.morsecodegame.repository

import androidx.datastore.preferences.core.Preferences

/**
 * Popup preferences datastore purpose is to persist has user
 * toggled the "don't show again" switch for a dialog
 */
interface WarningRepository {
    suspend fun save(key: Preferences.Key<Boolean>, disable: Boolean)
    suspend fun load(key: Preferences.Key<Boolean>): Boolean
}