package com.galaxy.morsecodegame.repository

import kotlinx.coroutines.flow.Flow

interface Repository<T> {
    fun create(entity: T)
    fun update(entity: T)
    fun delete(entity: T)
    fun get(id: Int): Flow<T?>
}
