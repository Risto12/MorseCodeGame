package com.example.morsecodegame.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

// Todo this has similarities with gameViewModel so abstract class could be made
class FlashViewModel: ViewModel() {

    private val _isFlashing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isFlashing: StateFlow<Boolean> = _isFlashing

    fun flashingOn() = _isFlashing.update { true }

    fun flashingOff() = _isFlashing.update { false }
}