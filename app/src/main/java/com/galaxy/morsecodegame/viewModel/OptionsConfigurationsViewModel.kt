package com.galaxy.morsecodegame.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galaxy.morsecodegame.model.Options
import com.galaxy.morsecodegame.repository.OptionsRepository
import com.galaxy.morsecodegame.utility.DifficultLevels
import com.galaxy.morsecodegame.utility.Learning
import com.galaxy.morsecodegame.utility.ToastGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.reflect.KMutableProperty0
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class OptionsConfigurationsViewModel @Inject constructor(
    private val optionsRepository: OptionsRepository
) : ViewModel() {

    private val _optionsViewModelData: MutableStateFlow<Options> = MutableStateFlow(
        Options(
            0,
            DifficultLevels.EASY,
            10,
            6
        )
    )

    val optionsViewModelData: StateFlow<Options> = _optionsViewModelData

    init {
        viewModelScope.launch(Dispatchers.IO) { load() }
    }

    suspend fun save() = coroutineScope {
        optionsRepository.update(optionsViewModelData.value.toOptionsEntity())
    }

    private suspend fun load() {
        optionsRepository.get(1).collectLatest { optionsEntity ->
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
        } catch (e: Exception) {
            // Bad fix but I want to see how the slider will behave when this happens
            ToastGenerator.showLongText(
                context,
                "There seemed be slight issue during configuration change. Please make sure " +
                    "That the slider is in correct place"
            )
            Log.e(
                "Options view model",
                "Exception during update configuration",
                e
            )
        }
    }
}
