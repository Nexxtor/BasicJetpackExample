package com.naldana.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.naldana.myapplication.data.dao.PokemonDao
import com.naldana.myapplication.data.dao.RemoteKeyDao
import com.naldana.myapplication.data.entity.Pokemon
import com.naldana.myapplication.data.entity.RemoteKey

@Database(entities = [Pokemon::class, RemoteKey::class], version = 1, exportSchema = false)
abstract class PokemonDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao
    abstract fun remoteKeyDao(): RemoteKeyDao

    companion object {
        @Volatile
        private var INSTANCE: PokemonDatabase? = null

        fun getDatabase(context: Context): PokemonDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PokemonDatabase::class.java,
                    "check_in_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}