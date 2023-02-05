package com.example.morsecodegame.morsecode

// PARIS" is the standard word when calculating words per minute
const val PARIS_IN_DITS = 50
const val MINUTE_IN_SECONDS = 60f

fun Float.toMilliseconds(): Float = this*1000

fun Char.toMorseCodeStringConversion(): String {
    return this.toString().let {
        if(it.contains("\\d".toRegex())) {
            when(it) {
                "0" -> MorseCodeLetter.ZERO
                "1" -> MorseCodeLetter.ONE
                "2" -> MorseCodeLetter.TWO
                "3" -> MorseCodeLetter.THREE
                "4" -> MorseCodeLetter.FOUR
                "5" -> MorseCodeLetter.FIVE
                "6" -> MorseCodeLetter.SIX
                "7" -> MorseCodeLetter.SEVEN
                "8" -> MorseCodeLetter.EIGHT
                "9" -> MorseCodeLetter.NINE
                else -> error("Unknown number character")
            }.name
        } else {
            it
        }
    }.uppercase()
}

object MorseCodeConverter {

    private const val WAIT = -1

    fun morseCodeAsMillisecondsRepresentation(
        morseCode: List<MorseCodeUnits>,
        wordsPerMinute: Int
    ): List<Long> {
        /***
         * Converts Morse code units to milliseconds representation
         * to indicate the frequency of signals(light/sound).
         * The units that indicates wait in morse code (intra character, inter character
         * word space) are minus numbers to distinguish them.
         */

        return morseCode.map { morseCodeUnit ->
            val multiplier = when(morseCodeUnit) {
                MorseCodeUnits.DI -> MorseCodeUnits.DI.multiplier
                MorseCodeUnits.DA -> MorseCodeUnits.DA.multiplier
                MorseCodeUnits.INTRA_CHARACTER -> MorseCodeUnits.INTRA_CHARACTER.multiplier*WAIT
                MorseCodeUnits.INTER_CHARACTER -> MorseCodeUnits.INTER_CHARACTER.multiplier*WAIT
                MorseCodeUnits.WORD_SPACE -> MorseCodeUnits.WORD_SPACE.multiplier*WAIT
            }
            return@map multiplier*secondsPerDit(wordsPerMinute).toMilliseconds().toLong()
        }
    }

    fun toMorseCode(text: String): List<MorseCodeUnits>{
        val separatedWords = text
            .trim()
            .split(" ")
        return if (separatedWords.size > 1) {
            wordsToMorseCode(separatedWords)
        } else {
            wordToMorseCode(separatedWords.first()) // TODO there is still illegal character in here when using hard difficult level
        }
    }

    private fun wordToMorseCode(word: String): List<MorseCodeUnits> {
        /***
         * Converts string to morse code representation
         */
        val morseCode = mutableListOf<MorseCodeUnits>()
        val morseCodeLetterValues = MorseCodeLetter.values()
        word.forEach { character ->
            val letter = morseCodeLetterValues.first {
                it.name == character.toMorseCodeStringConversion()
            }
            letter.getUnitsWithIntraAndInterUnits().forEach {
                morseCode.add(it)
            }
        }
        morseCode.removeLast()
        return morseCode.toList()
    }

    private fun wordsToMorseCode(words: List<String>): List<MorseCodeUnits> {
        /***
         * Converts string with words separated by space to morse code representation
         */
        val morseCode = mutableListOf<MorseCodeUnits>()
        val separatedWordsAsMorse = words.map { wordToMorseCode(it) }

        separatedWordsAsMorse.forEach { morseCodeWord ->
            // Add all morse coded words to morseCode list
            morseCodeWord.forEach { morseCode.add(it) }
            morseCode.add(MorseCodeUnits.WORD_SPACE)
        }

        morseCode.removeLast()
        return morseCode.toList()
    }

    fun secondsPerDit(wordsPerMinute: Int): Float = MINUTE_IN_SECONDS/(PARIS_IN_DITS*wordsPerMinute)

}