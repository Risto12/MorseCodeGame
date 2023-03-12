package com.galaxy.morsecodegame.utility

import com.galaxy.morsecodegame.morsecode.MorseCodeConverter
import kotlin.math.abs
import kotlinx.coroutines.delay

object MorseCodeTimer {
    // TODO should be named better ...
    suspend fun onOffTimer(text: String, wordsPerMinute: Int, invoke: (onOff: Boolean) -> Unit) {
        /**
         * Converts text to morse code units in milliseconds. Then converts them to boolean value
         * to indicate should light be on or off and then passes it to lambda and delays the thread
         * when user passed lambda is finished.
         */
        val morseCode = MorseCodeConverter.toMorseCode(text)
        MorseCodeConverter.morseCodeAsMillisecondsRepresentation(
            morseCode,
            wordsPerMinute
        ).forEach { morseDelay ->
            // If morseDelay is not minus number it indicates that the light should be on
            invoke(morseDelay > 0)
            delay(abs(morseDelay))
        }
    }
}
