package com.aarstrand.zindre.pokechecklist.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class PokemonRepository(private val pokemonDao: PokemonDao){

    companion object {
        @Volatile private var instance: PokemonRepository? = null

        fun getInstance(pokemonDao: PokemonDao) =
                instance ?: synchronized(this){
                    instance ?: PokemonRepository(pokemonDao).also {instance = it}
                }
    }

    @WorkerThread
    fun getCount() = pokemonDao.getCount()

    @WorkerThread
    fun getAllPokemon() = pokemonDao.getAllPokemon()

    @WorkerThread
    suspend fun insert(pokemon: List<Pokemon>){
        withContext(IO){
            pokemonDao.insertAll(pokemon)
        }
    }

    suspend fun insert(pokemon: Pokemon){
        withContext(IO){
            try{
                pokemonDao.insert(pokemon)

            }catch (error:Error){

            }
        }
    }
}