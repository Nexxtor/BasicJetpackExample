package com.naldana.myapplication

import android.app.Application
import com.naldana.myapplication.data.PokemonDatabase
import com.naldana.myapplication.repository.PokemonRepository
import timber.log.Timber

class MyApplication : Application() {
    val database by lazy { PokemonDatabase.getDatabase(this) }
    val pokemonRepository by lazy { PokemonRepository(database.pokemonDao()) }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}