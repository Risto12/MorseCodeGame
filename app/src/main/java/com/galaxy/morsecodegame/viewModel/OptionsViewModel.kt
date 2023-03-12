package com.galaxy.morsecodegame.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galaxy.morsecodegame.model.Options
import com.galaxy.morsecodegame.repository.OptionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
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
