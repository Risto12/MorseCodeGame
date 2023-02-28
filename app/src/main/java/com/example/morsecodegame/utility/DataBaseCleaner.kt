@file:Suppress("unused")

package com.example.morsecodegame.utility

import com.example.morsecodegame.db.AppDatabase
import com.example.morsecodegame.model.Options

object DataBaseCleaner {
    /***
     * Function for development when I'm too lazy to clean emulator db manually
     */
    fun save(options: Options) =
        AppDatabase.getOptionsDao().updateOptions(options.toOptionsEntity())
}
