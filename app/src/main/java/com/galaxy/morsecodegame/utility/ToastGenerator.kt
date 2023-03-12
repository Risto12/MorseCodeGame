package com.galaxy.morsecodegame.utility

import android.content.Context
import android.widget.Toast

object ToastGenerator {
    fun showLongText(context: Context, text: String) =
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}
