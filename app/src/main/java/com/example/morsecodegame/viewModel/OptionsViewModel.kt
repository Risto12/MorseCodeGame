package com.example.morsecodegame.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.morsecodegame.db.AppDatabase
import com.example.morsecodegame.model.Options
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// This can be named this way for now because migrating to dagger and will name this differently soon
class OptionsViewModel : ViewModel() {

    // TODO When creating tests consider lifting this as DI to make tests easier and delegate it to dagger.
    private val db = AppDatabase.getOptionsDao()

    @Volatile
    private lateinit var optionsViewModelData: Options

    fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            db.getOptions().collect {
                optionsViewModelData = it!!.toOptions()
            }
        }
    }

    fun getOptions() = optionsViewModelData.copy()
}
