@file:Suppress("unused")

package com.galaxy.morsecodegame.utility

import com.galaxy.morsecodegame.db.AppDatabase
import com.galaxy.morsecodegame.model.Options

object DataBaseCleaner {
    /***
     * Function for development when I'm too lazy to clean emulator db manually
     */
    fun save(options: Options) =
        AppDatabase.getOptionsDao().updateOptions(options.toOptionsEntity())
}
