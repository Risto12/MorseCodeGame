package com.galaxy.morsecodegame.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.galaxy.morsecodegame.db.entity.OptionsEntity
import com.galaxy.morsecodegame.utility.DifficultLevels
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

const val OPTIONS_ID = 1

private fun String.toDifficultLevel() = DifficultLevels.values().first { it.name == this }

/***
 * Adapter class to convert OptionsViewModel to OptionsEntity with
 * JSON serialization and Parcelable serialization to pass Options in Intent
 ***/
@Keep
@Serializable
data class Options(
    var gameTimeInMinutes: Int,
    var difficultLevel: DifficultLevels,
    var numberOfQuestions: Int,
    var wordsPerMinute: Int
) : Parcelable {
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
