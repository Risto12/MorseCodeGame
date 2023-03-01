package com.example.morsecodegame.di.components

import com.example.morsecodegame.MainActivity
import dagger.Component

@Component
interface MainActivityComponent {
    fun inject(activity: MainActivity)
}