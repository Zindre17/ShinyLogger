package com.aarstrand.zindre.pokechecklist.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class PokemonRepository(private val pokemonDao: PokemonDao){

    val allPokemon: LiveData<List<Pokemon>> = pokemonDao.getAllPokemon()

    @WorkerThread
    suspend fun insert(pokemon: List<Pokemon>){
        pokemonDao.insertAll(pokemon)
    }
}