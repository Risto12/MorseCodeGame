package com.galaxy.morsecodegame

import com.galaxy.morsecodegame.utility.DifficultLevels
import com.galaxy.morsecodegame.utility.QuestionGenerator
import org.junit.Assert
import org.junit.Test

class QuestionGeneratorTest {

    @Test
    fun test_correct_number_of_question_is_generated() {
        Assert.assertEquals(
            5,
            QuestionGenerator.generateQuestions(5, DifficultLevels.EASY).size
        )
        Assert.assertEquals(
            2,
            QuestionGenerator.generateQuestions(2, DifficultLevels.MEDIUM).size
        )
        Assert.assertEquals(
            2,
            QuestionGenerator.generateQuestions(2, DifficultLevels.HARD).size
        )
    }

    @Test
    fun test_that_possible_answers_has_no_duplicates() {
        val question =
            QuestionGenerator.generateQuestions(1, DifficultLevels.HARD)
                .first()
                .possibleAnswers
        // making sure that I don't accidentally change the type of the list.
        // but more importantly just playing a round with kotlin functionalities...
        // TODO play around with this later
        Assert.assertEquals(LinkedHashSet::class.javaObjectType, question::class.javaObjectType)
    }

    @Test
    fun test_that_correct_answer_is_in_possible_answers() {
        QuestionGenerator.generateQuestions(4, DifficultLevels.HARD).forEach {
            Assert.assertTrue(it.answer in it.possibleAnswers)
        }
    }
}
