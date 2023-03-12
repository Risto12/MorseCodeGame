package com.galaxy.morsecodegame.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.galaxy.morsecodegame.model.Options
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Entity
data class OptionsEntity(@PrimaryKey val id: Int, @ColumnInfo val options: String) {
    fun toOptions(): Options = Json.decodeFromString(this.options)
}
