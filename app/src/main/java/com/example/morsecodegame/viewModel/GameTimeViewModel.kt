package com.example.morsecodegame.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.RoundingMode
import kotlin.time.Duration


class GameTimeViewModel : ViewModel() {
    /***
     * ViewModel to keep track of the game time. How the game time is initialized is a bit
     * experimental. Better approach might have been to pass _time as constructor parameter and
     * create view model factory to initialize this.
     */
    private val timeNotSet = -1L

    private var _time: Long = timeNotSet
    private val _timeLeft = MutableStateFlow(calculateTimeLeft())
    val timeLeft: StateFlow<String> = _timeLeft

    fun setTime(timeInMinutes: Duration) {
        _time = timeInMinutes.inWholeSeconds
    }

    fun decreaseOneSecond() {
        if (_time > 0) {
            _time -= 1
            _timeLeft.update { calculateTimeLeft() }
        }
    }

    private fun timeNotSet() : Boolean = _time < 0

    fun hasTimeLeft() : Boolean = _time > 0

    private fun calculateTimeLeft(): String {
        if (timeNotSet()) return "0:00"
        val seconds = _time % 60
        val minutes = getMinutes()
        val timeLeft = if (seconds < 10) {
            "$minutes:0$seconds"
        } else {
            "$minutes:$seconds"
        }
        return timeLeft
    }

    private fun getMinutes(): Int {
        return try {
            (_time / 60).toBigDecimal().setScale(0, RoundingMode.DOWN).toInt()
        } catch (e: ArithmeticException) {
            Log.d("GameTimeViewModel", "Arithmetic exception when time: $_time")
            throw e
        }
    }
}