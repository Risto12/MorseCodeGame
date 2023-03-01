package com.example.morsecodegame

import com.example.morsecodegame.di.DaggerTestOptionsComponent
import com.example.morsecodegame.di.components.OptionsComponent


class MorseCodeTestApplication: MorseCodeGameApplication() {
    override fun initializeOptionsComponent(): OptionsComponent {
        return DaggerTestOptionsComponent.create()
    }
}
