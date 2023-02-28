package com.example.morsecodegame.morsecode

enum class MorseCodeLetter(val units: List<MorseCodeUnits>) {
    A(listOf(MorseCodeUnits.DI, MorseCodeUnits.DA)),
    B(listOf(MorseCodeUnits.DA, MorseCodeUnits.DI, MorseCodeUnits.DI, MorseCodeUnits.DI)),
    C(listOf(MorseCodeUnits.DA, MorseCodeUnits.DI, MorseCodeUnits.DA, MorseCodeUnits.DI)),
    D(listOf(MorseCodeUnits.DA, MorseCodeUnits.DI, MorseCodeUnits.DI)),
    E(listOf(MorseCodeUnits.DI)),
    F(listOf(MorseCodeUnits.DI, MorseCodeUnits.DI, MorseCodeUnits.DA, MorseCodeUnits.DI)),
    G(listOf(MorseCodeUnits.DA, MorseCodeUnits.DA, MorseCodeUnits.DI)),
    H(listOf(MorseCodeUnits.DI, MorseCodeUnits.DI, MorseCodeUnits.DI, MorseCodeUnits.DI)),
    I(listOf(MorseCodeUnits.DI, MorseCodeUnits.DI)),
    J(listOf(MorseCodeUnits.DI, MorseCodeUnits.DA, MorseCodeUnits.DA, MorseCodeUnits.DA)),
    K(listOf(MorseCodeUnits.DA, MorseCodeUnits.DI, MorseCodeUnits.DA)),
    L(listOf(MorseCodeUnits.DI, MorseCodeUnits.DA, MorseCodeUnits.DI, MorseCodeUnits.DI)),
    M(listOf(MorseCodeUnits.DA, MorseCodeUnits.DA)),
    N(listOf(MorseCodeUnits.DA, MorseCodeUnits.DI)),
    O(listOf(MorseCodeUnits.DA, MorseCodeUnits.DA, MorseCodeUnits.DA)),
    P(listOf(MorseCodeUnits.DI, MorseCodeUnits.DA, MorseCodeUnits.DA, MorseCodeUnits.DI)),
    Q(listOf(MorseCodeUnits.DA, MorseCodeUnits.DA, MorseCodeUnits.DI, MorseCodeUnits.DA)),
    R(listOf(MorseCodeUnits.DI, MorseCodeUnits.DA, MorseCodeUnits.DI)),
    S(listOf(MorseCodeUnits.DI, MorseCodeUnits.DI, MorseCodeUnits.DI)),
    T(listOf(MorseCodeUnits.DA)),
    U(listOf(MorseCodeUnits.DI, MorseCodeUnits.DI, MorseCodeUnits.DA)),
    V(listOf(MorseCodeUnits.DI, MorseCodeUnits.DI, MorseCodeUnits.DI, MorseCodeUnits.DA)),
    W(listOf(MorseCodeUnits.DI, MorseCodeUnits.DA, MorseCodeUnits.DA)),
    X(listOf(MorseCodeUnits.DA, MorseCodeUnits.DI, MorseCodeUnits.DI, MorseCodeUnits.DA)),
    Y(listOf(MorseCodeUnits.DA, MorseCodeUnits.DI, MorseCodeUnits.DA, MorseCodeUnits.DA)),
    Z(listOf(MorseCodeUnits.DA, MorseCodeUnits.DA, MorseCodeUnits.DI, MorseCodeUnits.DI)),
    ZERO(listOf(MorseCodeUnits.DA, MorseCodeUnits.DA, MorseCodeUnits.DA, MorseCodeUnits.DA, MorseCodeUnits.DA)),
    ONE(listOf(MorseCodeUnits.DI, MorseCodeUnits.DA, MorseCodeUnits.DA, MorseCodeUnits.DA, MorseCodeUnits.DA)),
    TWO(listOf(MorseCodeUnits.DI, MorseCodeUnits.DI, MorseCodeUnits.DA, MorseCodeUnits.DA, MorseCodeUnits.DA)),
    THREE(listOf(MorseCodeUnits.DI, MorseCodeUnits.DI, MorseCodeUnits.DI, MorseCodeUnits.DA, MorseCodeUnits.DA)),
    FOUR(listOf(MorseCodeUnits.DI, MorseCodeUnits.DI, MorseCodeUnits.DI, MorseCodeUnits.DI, MorseCodeUnits.DA)),
    FIVE(listOf(MorseCodeUnits.DI, MorseCodeUnits.DI, MorseCodeUnits.DI, MorseCodeUnits.DI, MorseCodeUnits.DI)),
    SIX(listOf(MorseCodeUnits.DA, MorseCodeUnits.DI, MorseCodeUnits.DI, MorseCodeUnits.DI, MorseCodeUnits.DI)),
    SEVEN(listOf(MorseCodeUnits.DA, MorseCodeUnits.DA, MorseCodeUnits.DI, MorseCodeUnits.DI, MorseCodeUnits.DI)),
    EIGHT(listOf(MorseCodeUnits.DA, MorseCodeUnits.DA, MorseCodeUnits.DA, MorseCodeUnits.DI, MorseCodeUnits.DI)),
    NINE(listOf(MorseCodeUnits.DA, MorseCodeUnits.DA, MorseCodeUnits.DA, MorseCodeUnits.DA, MorseCodeUnits.DI)),
    ;

    fun getUnitsWithIntraAndInterUnits(): List<MorseCodeUnits> {
        val letterWithIntraCharacters = mutableListOf<MorseCodeUnits>()
        this.units.forEach {
            if (letterWithIntraCharacters.isNotEmpty()) {
                letterWithIntraCharacters.add(MorseCodeUnits.INTRA_CHARACTER)
            }
            letterWithIntraCharacters.add(it)
        }
        letterWithIntraCharacters.add(MorseCodeUnits.INTER_CHARACTER)
        return letterWithIntraCharacters.toList()
    }

    companion object {

        private fun morseCodeLettersToString(
            prefix: String = "",
            postfix: String = "",
            separator: String = "",
        ): String = values().joinToString(
            prefix = prefix,
            postfix = postfix,
            separator = separator,
        ) {
            when (it) {
                ZERO -> "0"
                ONE -> "1"
                TWO -> "2"
                THREE -> "3"
                FOUR -> "4"
                FIVE -> "5"
                SIX -> "6"
                SEVEN -> "7"
                EIGHT -> "8"
                NINE -> "9"
                else -> it.name
            }
        }

        fun supportedCharacters() = morseCodeLettersToString()

        fun containsNotSupportedCharacters(text: String): Boolean {
            val notSupportedCharacters = morseCodeLettersToString(
                prefix = "[^", // match all other except allowed characters
                postfix = " ]", // This allows spaces to be used
                separator = "",
            )
            return text.contains(notSupportedCharacters.toRegex(setOf(RegexOption.IGNORE_CASE)))
        }
    }
}
