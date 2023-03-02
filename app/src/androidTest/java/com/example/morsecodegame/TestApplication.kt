package com.example.morsecodegame

import com.example.morsecodegame.di.DaggerTestOptionsComponent


class MorseCodeTestApplication: MorseCodeGameApplication() {
    override fun initializeOptionsComponent(): OptionsComponent {
        return DaggerTestOptionsComponent.create()
    }
}
