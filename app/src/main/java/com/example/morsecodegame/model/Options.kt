package com.example.morsecodegame.model

import android.os.Parcel
import android.os.Parcelable
import com.example.morsecodegame.db.entity.OptionsEntity
import com.example.morsecodegame.utility.DifficultLevels
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.*

const val OPTIONS_ID = 1

private fun String.toDifficultLevel() = DifficultLevels.values().first { it.name == this }

/***
 * Adapter class to convert OptionsViewModel to OptionsEntity with
 * JSON serialization and Parcelable serialization to pass Options in Intent
 ***/
@Serializable
data class Options(
    var gameTimeInMinutes: Int,
    var difficultLevel: DifficultLevels,
    var numberOfQuestions: Int,
    var wordsPerMinute: Int
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!.toDifficultLevel(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(gameTimeInMinutes)
        parcel.writeString(difficultLevel.name)
        parcel.writeInt(numberOfQuestions)
        parcel.writeInt(wordsPerMinute)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun toOptionsEntity(): OptionsEntity =
        OptionsEntity(id = OPTIONS_ID, options = Json.encodeToString(this))

    companion object CREATOR : Parcelable.Creator<Options> {
        override fun createFromParcel(parcel: Parcel): Options {
            return Options(parcel)
        }

        override fun newArray(size: Int): Array<Options?> {
            return arrayOfNulls(size)
        }
    }
}
