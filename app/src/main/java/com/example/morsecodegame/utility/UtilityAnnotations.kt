package com.example.morsecodegame.utility

/***
 * Annotates class as a learning example e.g. in case function has deprecated annotation but is
 * still kept even when no code is using it anymore.
 *
 * Default value example means this is just another way of doing it instead now used method.
 * Usually deprecated annotation is pointing at the now used method.
 */
annotation class Learning(val message: String = "example")
