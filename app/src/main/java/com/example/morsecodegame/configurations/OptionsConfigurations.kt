package com.example.morsecodegame.configurations

import android.os.Parcelable
import android.util.Log
import androidx.compose.runtime.Stable
import com.example.morsecodegame.utility.getConfigurationGeneratorPropertiesAsMap
import java.util.*
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
@Stable
data class OptionsConfigurations @Inject constructor(
    val gameTimeMin: Float,
    val gameTimeMax: Float,
    val wordsPerMinuteMin: Float,
    val wordsPerMinuteMax: Float,
    val numberOfQuestionsMin: Float,
    val numberOfQuestionsMax: Float
) : Parcelable {

    companion object {
        val MorseCodeLetterFactoryConfigurationsGenerator =
            object : ConfigurationGenerator<OptionsConfigurations> {
                override val keyPrefix = "options"
                override fun generate(properties: Properties): OptionsConfigurations {
                    val gameTimeMinKey = "game_time_min"
                    val gameTimeMaxKey = "game_time_max"
                    val wordsPerMinuteMinKey = "words_per_minute_min"
                    val wordsPerMinuteMaxKey = "words_per_minute_max"
                    val numberOfQuestionsMinKey = "number_of_questions_min"
                    val numberOfQuestionsMaxKey = "number_of_questions_max"

                    val configurations = properties.getConfigurationGeneratorPropertiesAsMap(
                        keyPrefix,
                        gameTimeMinKey,
                        gameTimeMaxKey,
                        wordsPerMinuteMinKey,
                        wordsPerMinuteMaxKey,
                        numberOfQuestionsMinKey,
                        numberOfQuestionsMaxKey
                    ).mapValues {
                        it.value?.let { property ->
                            try {
                                property.toFloat()
                            } catch (e: NumberFormatException) {
                                Log.w(
                                    "Morse code configurations builder",
                                    "Can't cast value:$property to float for key: ${it.key}"
                                )
                                null
                            }
                        }
                    }

                    return OptionsConfigurations(
                        gameTimeMin = configurations[gameTimeMinKey] ?: 1f,
                        gameTimeMax = configurations[gameTimeMaxKey] ?: 10f,
                        wordsPerMinuteMin = configurations[wordsPerMinuteMinKey] ?: 1f,
                        wordsPerMinuteMax = configurations[wordsPerMinuteMaxKey] ?: 15f,
                        numberOfQuestionsMin = configurations[numberOfQuestionsMinKey] ?: 4f,
                        numberOfQuestionsMax = configurations[numberOfQuestionsMaxKey] ?: 8f
                    )
                }
            }
    }
}
