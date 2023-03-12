package com.galaxy.morsecodegame.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.galaxy.morsecodegame.db.dao.OptionsDao
import com.galaxy.morsecodegame.db.entity.OptionsEntity

const val DATABASE_NAME = "morseCodeGame.db"

@Database(entities = [OptionsEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun optionsDao(): OptionsDao

    companion object {
        private const val PRE_POPULATED_DATA = "morseCodeGame.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getOptionsDao(): OptionsDao = INSTANCE!!.optionsDao()

        fun setDatabase(context: Context) {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                            .createFromAsset(PRE_POPULATED_DATA)
                            .build()
                }
            }
        }
    }
}
