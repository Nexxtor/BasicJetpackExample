package com.naldana.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.naldana.myapplication.data.PokemonDatabase
import com.naldana.myapplication.data.entity.Pokemon
import com.naldana.myapplication.network.PokeAPI
import com.naldana.myapplication.network.PokemonMediator
import com.naldana.myapplication.repository.PokemonRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    database: PokemonDatabase,
    private val pokemonRepo: PokemonRepository
) : ViewModel() {


    @OptIn(ExperimentalPagingApi::class)
    val pager = Pager(
        config = PagingConfig(pageSize = 10),
        remoteMediator = PokemonMediator(database, PokeAPI)
    ) {
        pokemonRepo.pagingSource()
    }

    private var _pokemonInfo = MutableLiveData<Pokemon>()
    val pokemonInfo: LiveData<Pokemon>
        get() = _pokemonInfo

    fun insert(pokemon: Pokemon) {
        viewModelScope.launch {
            pokemonRepo.insert(pokemon)
        }
    }

    fun getPokemon(query: String) {
        viewModelScope.launch {
            try {
                val pokemon = pokemonRepo.getPokemon(query)
                _pokemonInfo.value = pokemon
            } catch (e: Exception) {
                Timber.d(e)
            }
        }
    }
}