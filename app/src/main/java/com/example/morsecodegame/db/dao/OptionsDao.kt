package com.example.morsecodegame.db.dao

import androidx.room.*
import com.example.morsecodegame.db.entity.OptionsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OptionsDao {
    @Query("SELECT * FROM OptionsEntity WHERE id=1")
    fun getOptions(): Flow<OptionsEntity?>

    @Insert
    fun createOptions(options: OptionsEntity)

    @Update
    fun updateOptions(options: OptionsEntity)

    @Delete
    fun deleteOptions(options: OptionsEntity)
}
