package com.galaxy.morsecodegame.viewModel

import androidx.compose.runtime.Stable
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galaxy.morsecodegame.repository.WarningRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
data class WarningPopup(
    val isDisabled: Boolean,
    val isClicked: Boolean
) {
    fun showPopup(): Boolean = !isClicked && !isDisabled
}

@HiltViewModel
class WarningViewModel @Inject constructor(private val popupWarningRepository: WarningRepository): ViewModel() {

    private val _warningStatus = MutableStateFlow(WarningPopup(
        isDisabled = true,
        isClicked = false
    )
    )
    val warningStatus: StateFlow<WarningPopup> = _warningStatus

    init {
        viewModelScope.launch {
            isWarningDisabled(mainWarning)
        }
    }

    fun closeWindowPopup() {
        _warningStatus.update {
            it.copy(
                isClicked = true,
            )
        }
    }

    fun disableWarning() {
        viewModelScope.launch {
            popupWarningRepository.save(mainWarning)
        }
    }

    private suspend fun isWarningDisabled(key: Preferences.Key<Boolean>) {
        val status = popupWarningRepository.load(key)
        _warningStatus.update {
            it.copy(
                isDisabled = status
            )
        }
    }

    companion object {
        val mainWarning = booleanPreferencesKey("main_popup_warning")
    }
}