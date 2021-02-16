package com.naldana.myapplication.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.naldana.myapplication.data.PokemonDatabase
import com.naldana.myapplication.data.entity.Pokemon
import com.naldana.myapplication.data.entity.RemoteKey
import retrofit2.HttpException
import java.io.IOException

const val PAGE_KEY = "POKEMON"

@OptIn(ExperimentalPagingApi::class)
class PokemonMediator(
    private val database: PokemonDatabase,
    private val API: PokeAPI
) : RemoteMediator<Int, Pokemon>() {

    val remoteKeyDao = database.remoteKeyDao()
    val pokemonDao = database.pokemonDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Pokemon>
    ): MediatorResult {
       return try {
           // Determinar que pagina cargar

           val loadKey: String? = when (loadType) {
               LoadType.REFRESH -> null
               LoadType.PREPEND ->
                   return MediatorResult.Success(endOfPaginationReached = true)
               LoadType.APPEND -> {
                   val remoteKey = database.withTransaction {
                       remoteKeyDao.remoteKeyByQuery(PAGE_KEY)
                   }
                   if (remoteKey.nextKey == null) {
                       return MediatorResult.Success(endOfPaginationReached = true)
                   }
                   remoteKey.nextKey
               }
           }

           // Cargar la pagina
           val response = if (loadKey == null) {
               API.retrofitService.getListPokemon(10)
           } else {
               API.retrofitService.getListPokemonByUrl(loadKey)
           }

           // Obtener los detales de cada pokemon en la pagina
           val pokemons = response.results.map {
               API.retrofitService.getPokemonByUrl(it.url)
               /*val splited = it.url.split("/")
               val index = splited[splited.size - 2]
               Pokemon(index.toInt(), it.name)*/
           }

           database.withTransaction {
               // Si se desea refrescar los datos barramos los viejos
               if (loadType == LoadType.REFRESH) {
                   pokemonDao.deleteAll()
                   // Borramos el registro de la pagina siguiente
                   remoteKeyDao.deleteByQuery(PAGE_KEY)
               }

               // Guardamos que pagina sigue
               remoteKeyDao.insertOrReplace(RemoteKey(PAGE_KEY, response.next))

               // Guardamos los pokemons
               pokemonDao.insertAll(pokemons)

           }

           MediatorResult.Success(
               endOfPaginationReached = response.next.isNullOrBlank()
           )
       } catch (e: IOException) {
           MediatorResult.Error(e)
       } catch (e: HttpException) {
           MediatorResult.Error(e)
       }
    }
}