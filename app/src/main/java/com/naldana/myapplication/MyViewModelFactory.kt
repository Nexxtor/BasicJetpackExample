package com.naldana.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.naldana.myapplication.data.PokemonDatabase
import com.naldana.myapplication.repository.PokemonRepository

class MyViewModelFactory(
    private val database: PokemonDatabase,
    private val repository: PokemonRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(database, repository) as T
        }

        throw Exception("Unknown ViewModel Class")
    }
}