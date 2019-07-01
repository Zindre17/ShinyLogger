package com.aarstrand.zindre.pokechecklist.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class PokemonRepository(private val pokemonDao: PokemonDao){


    var allPokemon: List<Pokemon> = emptyList()
    var count: Int = 0

    @WorkerThread
    suspend fun get(){
        withContext(IO) {
            allPokemon = pokemonDao.get5Pokemon()
            count = pokemonDao.getCount()
        }
    }

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