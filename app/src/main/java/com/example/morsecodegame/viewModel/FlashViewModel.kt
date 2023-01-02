package com.example.morsecodegame.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.morsecodegame.model.Options
import com.example.morsecodegame.morsecode.MorseCodeConverter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Todo this has similarities with gameViewModel so abstract class could be made
class FlashViewModel: ViewModel() {

    private val _isFlashing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isFlashing: StateFlow<Boolean> = _isFlashing

    fun flashingOn() = _isFlashing.update { true }

    fun flashingOff() = _isFlashing.update { false }
}