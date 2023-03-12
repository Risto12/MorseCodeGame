package com.galaxy.morsecodegame

import com.galaxy.morsecodegame.morsecode.MorseCodeConverter
import com.galaxy.morsecodegame.morsecode.MorseCodeLetter
import com.galaxy.morsecodegame.morsecode.MorseCodeUnits
import org.junit.Assert
import org.junit.Test

class MorseCodeTest {

    @Test
    fun test_converting_letter_to_morse_code() {
        val letter = "p"
        val expected = listOf(
            MorseCodeUnits.DI,
            MorseCodeUnits.INTRA_CHARACTER,
            MorseCodeUnits.DA,
            MorseCodeUnits.INTRA_CHARACTER,
            MorseCodeUnits.DA,
            MorseCodeUnits.INTRA_CHARACTER,
            MorseCodeUnits.DI
        )
        val morseCode = MorseCodeConverter.toMorseCode(letter)
        Assert.assertEquals(expected, morseCode)
    }

    @Test
    fun test_converting_word_to_morse_code() {
        val word = "cat"
        val expected = listOf(
            MorseCodeUnits.DA, // c
            MorseCodeUnits.INTRA_CHARACTER,
            MorseCodeUnits.DI,
            MorseCodeUnits.INTRA_CHARACTER,
            MorseCodeUnits.DA,
            MorseCodeUnits.INTRA_CHARACTER,
            MorseCodeUnits.DI,
            MorseCodeUnits.INTER_CHARACTER,
            MorseCodeUnits.DI, // a
            MorseCodeUnits.INTRA_CHARACTER,
            MorseCodeUnits.DA,
            MorseCodeUnits.INTER_CHARACTER,
            MorseCodeUnits.DA // t
        )
        val morseCode = MorseCodeConverter.toMorseCode(word)
        Assert.assertEquals(expected, morseCode)
    }

    @Test
    fun test_converting_words_to_morse_code() {
        val words = "cat nap"
        val expected = listOf(
            MorseCodeUnits.DA, // c
            MorseCodeUnits.INTRA_CHARACTER,
            MorseCodeUnits.DI,
            MorseCodeUnits.INTRA_CHARACTER,
            MorseCodeUnits.DA,
            MorseCodeUnits.INTRA_CHARACTER,
            MorseCodeUnits.DI,
            MorseCodeUnits.INTER_CHARACTER,
            MorseCodeUnits.DI, // a
            MorseCodeUnits.INTRA_CHARACTER,
            MorseCodeUnits.DA,
            MorseCodeUnits.INTER_CHARACTER,
            MorseCodeUnits.DA, // t
            MorseCodeUnits.WORD_SPACE,
            MorseCodeUnits.DA, // n
            MorseCodeUnits.INTRA_CHARACTER,
            MorseCodeUnits.DI,
            MorseCodeUnits.INTER_CHARACTER,
            MorseCodeUnits.DI, // a
            MorseCodeUnits.INTRA_CHARACTER,
            MorseCodeUnits.DA,
            MorseCodeUnits.INTER_CHARACTER,
            MorseCodeUnits.DI, // p
            MorseCodeUnits.INTRA_CHARACTER,
            MorseCodeUnits.DA,
            MorseCodeUnits.INTRA_CHARACTER,
            MorseCodeUnits.DA,
            MorseCodeUnits.INTRA_CHARACTER,
            MorseCodeUnits.DI
        )
        val morseCode = MorseCodeConverter.toMorseCode(words)
        Assert.assertEquals(expected, morseCode)
    }

    @Test
    fun test_supported_characters() {
        val supported = "abcdefghijklmnopqrstuvwxyz0123456789"
        val result = MorseCodeLetter.Companion.containsNotSupportedCharacters(supported)
        Assert.assertFalse(result)
    }

    @Test
    fun test_empty_space_is_in_supported_characters() {
        val supported = "hello world"
        val result = MorseCodeLetter.Companion.containsNotSupportedCharacters(supported)
        Assert.assertFalse(result)
    }

    @Test
    fun test_not_supported_characters() {
        val notSupported = "?!:)(/&%#" // many more
        val result = MorseCodeLetter.Companion.containsNotSupportedCharacters(notSupported)
        Assert.assertTrue(result)
    }
}
