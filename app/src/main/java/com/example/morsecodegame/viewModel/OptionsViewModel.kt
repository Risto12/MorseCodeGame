package com.example.morsecodegame.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.morsecodegame.db.AppDatabase
import com.example.morsecodegame.db.dao.OptionsDao
import com.example.morsecodegame.model.Options
import com.example.morsecodegame.utility.DifficultLevels
import com.example.morsecodegame.utility.Learning
import com.example.morsecodegame.utility.ToastGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.reflect.KMutableProperty0


interface PersistData {
    suspend fun save()
    suspend fun load()
}


class OptionsViewModel: ViewModel(), PersistData {

    private val db = AppDatabase.getOptionsDao()

    private val _optionsViewModelData: MutableStateFlow<Options> = MutableStateFlow(Options(
        0, DifficultLevels.EASY, 10, 6
    ))

    val optionsViewModelData: StateFlow<Options> = _optionsViewModelData

    init {
        viewModelScope.launch(Dispatchers.IO) { load() }
    }

    override suspend fun save() = db.updateOptions(optionsViewModelData.value.toOptionsEntity())

    override suspend fun load() {
        db.getOptions().collect { optionsEntity ->
            _optionsViewModelData.update { optionsEntity!!.toOptions() }
        }
    }

    @Deprecated("In favor of composable function to handle this")
    @Learning("Rarely throws property not found exception. Can be debugged if bored...")
    fun <T>updateConfiguration(
        context: Context,
        property: KMutableProperty0<*>,
        value: T? = null
    ) {
        val options = optionsViewModelData.value
        try {
            val updatedOptions: Options = when (property) {
                options::gameTimeInMinutes -> options.copy(gameTimeInMinutes = value!! as Int)
                options::wordsPerMinute -> options.copy(wordsPerMinute = value!! as Int)
                options::difficultLevel -> options.copy(difficultLevel = value!! as DifficultLevels)
                options::numberOfQuestions -> options.copy(numberOfQuestions = value!! as Int)
                else -> error("Not implemented property ${property.name}")
            }
            _optionsViewModelData.update { updatedOptions }
        } catch(e: Exception) {
            // Bad fix but I want to see how the slider will behave when this happens
            ToastGenerator.showLongText(
                context,
                "There seemed be slight issue during configuration change. Please make sure " +
                        "That the slider is in correct place"
            )
            Log.e(
                "Options view model",
                "Exception during update configuration", e
            )
        }
    }
}
