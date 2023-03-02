package com.example.morsecodegame.repository

import com.example.morsecodegame.db.AppDatabase
import com.example.morsecodegame.db.entity.OptionsEntity
import com.example.morsecodegame.model.Options
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

class OptionsRepositoryImpl : OptionsRepository {

    private val repository = AppDatabase.getOptionsDao()

    override fun create(entity: OptionsEntity) {
        // Database will be auto populated when app starts
        throw Exception("Not implemented")
    }

    override fun update(entity: OptionsEntity) {
        repository.updateOptions(entity)
    }

    override fun delete(entity: OptionsEntity) {
        // Will never de deleted
        throw Exception("Not implemented")
    }

    override fun get(id: Int): Flow<OptionsEntity?> {
        return repository.getOptions()
    }

}
