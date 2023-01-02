package com.example.morsecodegame.configurations

import android.os.Parcelable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import com.example.morsecodegame.utility.Learning
import com.example.morsecodegame.utility.getConfigurationBuilderProperties
import kotlinx.parcelize.Parcelize
import java.util.*


@Parcelize
@Stable
data class MorseCodeLettersInfoTextConfiguration(
    val overview: String,
    val wordsPerMinute: String,
    val example: String
) : Parcelable {

    @Deprecated("In favor of Parcelize")
    @Learning("Working example of saver that is needed in rememberSaveable")
    fun toMapSaver(): Saver<MorseCodeLettersInfoTextConfiguration, Any> {
        val keyOverView = "overView"
        val keyWordsPerMinute = "wordsPerMinute"
        val keyExample = "example"
        return mapSaver(
            save = { mapOf(
                keyOverView to overview,
                keyWordsPerMinute to wordsPerMinute,
                keyExample to example
            ) },
            restore = {
                MorseCodeLettersInfoTextConfiguration(
                    it[keyOverView] as String,
                    it[keyWordsPerMinute] as String,
                    it[keyExample] as String
                )
            }
        )
    }

    companion object {
        val MorseCodeLetterFactoryConfigurationsBuilder =
            object : ConfigurationBuilder<MorseCodeLettersInfoTextConfiguration> {
                override val keyPrefix = "morse_letters"
                override fun build(properties: Properties): MorseCodeLettersInfoTextConfiguration {
                    val (overview, wordsPerMinute, example) =
                        properties.getConfigurationBuilderProperties(keyPrefix,
                            "overview",
                            "words_per_minute",
                            "example"
                        )
                    return MorseCodeLettersInfoTextConfiguration(
                        overview = overview,
                        wordsPerMinute = wordsPerMinute,
                        example = example
                    )
            }
        }
    }
}