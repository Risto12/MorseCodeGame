package com.example.morsecodegame.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.morsecodegame.model.Options
import com.example.morsecodegame.repository.OptionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@HiltViewModel
class OptionsViewModel @Inject constructor(
    private val optionsRepository: OptionsRepository
) : ViewModel() {

    @Volatile
    private lateinit var optionsViewModelData: Options

    fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            optionsRepository.get(1).collect {
                optionsViewModelData = it!!.toOptions()
            }
        }
    }

    fun getOptions() = optionsViewModelData.copy()
}
