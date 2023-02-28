package com.example.morsecodegame.utility

enum class DifficultLevels {
    EASY, MEDIUM, HARD
}

data class Question(val answer: String, val possibleAnswers: Set<String>) {
    fun isAnswerCorrect(answer: String): Boolean = answer.lowercase() == this.answer.lowercase()
}

object QuestionGenerator {
    private val easy: List<String> = "abcdefghijklmnopqrstuvwxyz0123456789".map { it.toString() }

    private val medium = listOf(
        "cat",
        "dog",
        "ship",
        "ufo",
        "hello",
        "banana",
        "hi",
        "moon",
        "planet",
        "usa",
        "red",
        "yellow",
        "apple",
        "mouse",
        "light",
        "city",
        "electricity",
        "chemistry",
        "jupiter",
        "mars",
        "orange",
        "house",
        "apollo",
        "zeus",
        "hermes",
        "sos",
    )

    private val hard = listOf(
        "hello world",
        "fallout boy",
        "is it you",
        "apollo",
        "secret message",
        "nikola tesla",
        "i need apples",
        "not yet implemented",
        "bye bye",
        "send me some money",
        "all hail king of the losers",
        "food please",
        "gold please",
        "you are so handsome",
        "smart guy",
        "cats will rule over the dogs",
        "fine voyage fine ship",
        "notify all interested in poker",
        "what hath God wrought", // yes it's not spelling mistake
        "rats",
        "dining with you tonight in spirit heart with you always",
        "no sickness all well",
    )

    fun generateQuestions(count: Int, difficulty: DifficultLevels): List<Question> {
        /***
         * Generates list of question data classes. Each question has one correct answer
         * and 3 wrongs. Questions with same correct answers may occur multiple times.
         */
        val questionsList = when (difficulty) {
            DifficultLevels.EASY -> easy
            DifficultLevels.MEDIUM -> medium
            DifficultLevels.HARD -> hard
        }

        val generated = mutableListOf<Question>()

        (0 until count).forEach { _ ->
            val questions = mutableSetOf<String>()
            while (questions.size < 4) {
                questions.add(questionsList.random())
            }
            generated.add(
                Question(answer = questions.random(), possibleAnswers = questions),
            )
        }

        return generated.toList()
    }
}
