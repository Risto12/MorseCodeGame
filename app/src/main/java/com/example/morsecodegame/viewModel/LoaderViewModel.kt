package com.example.morsecodegame.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LoaderViewModel: ViewModel() {
    private val _isLoadingDone: MutableStateFlow<Boolean> = MutableStateFlow<Boolean>(false)
    val isLoadingDone: StateFlow<Boolean> = _isLoadingDone

    fun setLoadingDone() = _isLoadingDone.update { true }
}