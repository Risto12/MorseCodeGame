package com.example.morsecodegame.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.morsecodegame.model.Options
import com.example.morsecodegame.utility.Question
import com.example.morsecodegame.utility.QuestionGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class GameViewModelData(
    val question: Question,
    val questionNumber: Int,
)

class GameViewModel(private val options: Options) : ViewModel() {

    private val questions = QuestionGenerator.generateQuestions(
        options.numberOfQuestions, options.difficultLevel
    )

    // TODO save this to savedStateHandle with syntax like:
    // questionNumber
    //      set(value) = savedStateHandle
    //      get() = savedStateHandle
    private var questionNumber: Int = 1

    private val _gameViewModelData: MutableStateFlow<GameViewModelData> = MutableStateFlow(
        GameViewModelData(
            question = questions[0],
            questionNumber = questionNumber
        ),

    )
    val gameViewModelData: StateFlow<GameViewModelData> = _gameViewModelData

    fun hasNextQuestion() = _gameViewModelData.value.questionNumber < questions.size

    fun nextQuestion() {
        questionNumber += 1
        _gameViewModelData.update { GameViewModelData(
                question = questions[(questionNumber-1)],
                questionNumber = questionNumber
            )
        }
    }

    fun getWordsPerMinute() = options.wordsPerMinute

    fun getAmountOfQuestions() = options.numberOfQuestions

    companion object {
        class ViewModelFactory(private val options: Options) : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GameViewModel(options) as T
            }
        }
    }
}