package com.example.morsecodegame

import com.example.morsecodegame.morsecode.MorseCodeConverter
import com.example.morsecodegame.morsecode.toMilliseconds
import org.junit.Assert
import org.junit.Test

// TODO change to milliseconds converter
class MorseCodeUnitsToSecondsConverterTest {

    private val wps = 7
    private val diAsMilliseconds = 171L
    private val daAsMilliseconds = 513L
    private val intraCharacterAsMilliseconds = -171L
    private val interCharacterAsMilliseconds = -513L
    private val wordSpaceAsMilliseconds = -1197L

    @Test
    fun test_seconds_per_dit_converter() {
        val expected = 0.240f
        val ditsPerSecond = MorseCodeConverter.secondsPerDit(wordsPerMinute = 5)
        Assert.assertEquals(expected, ditsPerSecond)
    }

    @Test
    fun test_seconds_per_dit_to_milliseconds_as_long() {
        val expected = 171L // rounding from 0.171428 but delay takes only Long so LGTM
        val ditsPerSecond = MorseCodeConverter.secondsPerDit(wordsPerMinute = 7)
        Assert.assertEquals(expected, ditsPerSecond.toMilliseconds().toLong())
    }

    @Test
    fun test_converting_letter_to_morse_code() {
        val letter = "p"
        val expected = listOf(
            diAsMilliseconds,
            intraCharacterAsMilliseconds,
            daAsMilliseconds,
            intraCharacterAsMilliseconds,
            daAsMilliseconds,
            intraCharacterAsMilliseconds,
            diAsMilliseconds,
        )
        val morseCode = MorseCodeConverter.toMorseCode(letter)
        val converted = MorseCodeConverter
            .morseCodeAsMillisecondsRepresentation(morseCode, wps)
        Assert.assertEquals(expected, converted)
    }

    @Test
    fun test_converting_word_to_morse_code() {
        val letter = "cat"
        val expected = listOf(
            daAsMilliseconds,
            intraCharacterAsMilliseconds,
            diAsMilliseconds,
            intraCharacterAsMilliseconds,
            daAsMilliseconds,
            intraCharacterAsMilliseconds,
            diAsMilliseconds,
            interCharacterAsMilliseconds,
            diAsMilliseconds,
            intraCharacterAsMilliseconds,
            daAsMilliseconds,
            interCharacterAsMilliseconds,
            daAsMilliseconds,
        )
        val morseCode = MorseCodeConverter.toMorseCode(letter)
        val converted = MorseCodeConverter
            .morseCodeAsMillisecondsRepresentation(morseCode, wps)
        Assert.assertEquals(expected, converted)
    }

    @Test
    fun test_converting_words_to_morse_code() {
        val letter = "cat nap"
        val expected = listOf(
            daAsMilliseconds, // c
            intraCharacterAsMilliseconds,
            diAsMilliseconds,
            intraCharacterAsMilliseconds,
            daAsMilliseconds,
            intraCharacterAsMilliseconds,
            diAsMilliseconds,
            interCharacterAsMilliseconds,
            diAsMilliseconds, // a
            intraCharacterAsMilliseconds,
            daAsMilliseconds,
            interCharacterAsMilliseconds,
            daAsMilliseconds, // t
            wordSpaceAsMilliseconds,
            daAsMilliseconds, // n
            intraCharacterAsMilliseconds,
            diAsMilliseconds,
            interCharacterAsMilliseconds,
            diAsMilliseconds, // a
            intraCharacterAsMilliseconds,
            daAsMilliseconds,
            interCharacterAsMilliseconds,
            diAsMilliseconds, // p
            intraCharacterAsMilliseconds,
            daAsMilliseconds,
            intraCharacterAsMilliseconds,
            daAsMilliseconds,
            intraCharacterAsMilliseconds,
            diAsMilliseconds,
        )
        val morseCode = MorseCodeConverter.toMorseCode(letter)
        val converted = MorseCodeConverter
            .morseCodeAsMillisecondsRepresentation(morseCode, wps)
        Assert.assertEquals(expected, converted)
    }
}
