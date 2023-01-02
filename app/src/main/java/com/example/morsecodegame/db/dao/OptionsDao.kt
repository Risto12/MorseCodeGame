package com.example.morsecodegame.db.dao

import androidx.room.*
import com.example.morsecodegame.db.entity.OptionsEntity


@Dao
interface OptionsDao {

    @Query("SELECT * FROM OptionsEntity WHERE id=1")
    fun getOptions(): OptionsEntity?

    @Insert
    fun setOptions(options: OptionsEntity)

    @Update
    fun updateOptions(options: OptionsEntity)

    @Delete
    fun deleteOptions(options: OptionsEntity)

}