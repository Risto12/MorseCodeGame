package com.example.morsecodegame.morsecode

enum class MorseCodeUnits(val multiplier: Int) {
    DI(1),
    DA(3),
    INTRA_CHARACTER(1), // the gap between dit and dah within a character
    INTER_CHARACTER(3), // the gap between the characters of a word
    WORD_SPACE(7), // the gap between two words
}
