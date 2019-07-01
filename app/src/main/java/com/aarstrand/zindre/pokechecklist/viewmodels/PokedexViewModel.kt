package com.aarstrand.zindre.pokechecklist.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.aarstrand.zindre.pokechecklist.adapters.DexAdapter
import com.aarstrand.zindre.pokechecklist.data.AppDatabase
import com.aarstrand.zindre.pokechecklist.data.Pokemon
import com.aarstrand.zindre.pokechecklist.data.PokemonRepository
import kotlinx.coroutines.launch

class PokedexViewModel(application: Application) : AndroidViewModel(application){

    private val repository: PokemonRepository
    private var allPokemon: List<Pokemon>
    val adapter: DexAdapter

    init{
        val pokemonDao = AppDatabase.getInstance(application).pokemonDao()
        repository = PokemonRepository(pokemonDao)
        viewModelScope.launch {
            repository.get()
            allPokemon = repository.allPokemon;
            update()
        }
        allPokemon = repository.allPokemon
        adapter = DexAdapter(application)
        update()
    }

    private fun update(){
        adapter.setPokemon(allPokemon)
    }

}