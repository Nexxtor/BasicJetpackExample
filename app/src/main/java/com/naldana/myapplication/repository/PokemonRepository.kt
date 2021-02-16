package com.naldana.myapplication.repository

import com.naldana.myapplication.data.dao.PokemonDao
import com.naldana.myapplication.data.entity.Pokemon
import com.naldana.myapplication.network.PokeAPI

class PokemonRepository(private val pokemonDao: PokemonDao) {
    private val API = PokeAPI.retrofitService

    suspend fun insert(pokemon: Pokemon) = pokemonDao.insert(pokemon)
    suspend fun getPokemon(query: String) = API.getPokemon(query)
    fun pagingSource() = pokemonDao.getSource()
}