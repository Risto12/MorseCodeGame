package com.example.morsecodegame.model

enum class WaitType(val text: String) {
    /***
    Splash screen types for time taking operations e.g. saving to database or fetching
    information from the web.
     ***/
    LOAD("Loading..."),
    SAVE("Saving...")
}
