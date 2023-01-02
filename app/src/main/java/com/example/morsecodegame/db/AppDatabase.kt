package com.example.morsecodegame.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.morsecodegame.db.dao.OptionsDao
import com.example.morsecodegame.db.entity.OptionsEntity
import com.example.morsecodegame.model.Options
import com.example.morsecodegame.utility.DifficultLevels
import com.example.morsecodegame.utility.Learning

const val DATABASE_NAME = "morseCodeGame.db"

@Database(entities = [OptionsEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun optionsDao(): OptionsDao

    companion object {
        private const val PRE_POPULATED_DATA = "morseCodeGame.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        @Deprecated("Coupled with deprecated insertDefaultOptions")
        @Learning
        private var defaultOptionsCheckDone: Boolean = false

        @Deprecated("In favor of create from asset")
        @Learning
        fun insertDefaultOptions() {
            if (defaultOptionsCheckDone) return else defaultOptionsCheckDone = true
            val options = INSTANCE!!.optionsDao()

            if (options.getOptions() != null) return
            val defaultOptions = Options(
                gameTimeInMinutes = 5,
                difficultLevel = DifficultLevels.EASY,
                numberOfQuestions = 10,
                wordsPerMinute = 10
            )
            options.setOptions(defaultOptions.toOptionsEntity())
        }

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
