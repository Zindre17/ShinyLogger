package com.aarstrand.zindre.pokechecklist.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.aarstrand.zindre.pokechecklist.R
import com.aarstrand.zindre.pokechecklist.adapters.DexAdapter
import com.aarstrand.zindre.pokechecklist.data.AppDatabase
import com.aarstrand.zindre.pokechecklist.data.Pokemon
import com.aarstrand.zindre.pokechecklist.data.PokemonRepository
import com.aarstrand.zindre.pokechecklist.ui.DexActivity
import kotlinx.android.synthetic.main.pokemon_list_item.view.*

class PokedexViewModel(application: Application) : AndroidViewModel(application){

    private val repository: PokemonRepository
    val allPokemon: LiveData<List<Pokemon>>
    val adapter: DexAdapter

    init{
        val pokemonDao = AppDatabase.getInstance(application).pokemonDao()
        repository = PokemonRepository(pokemonDao)
        allPokemon = repository.allPokemon
        adapter = DexAdapter(application)
        adapter.setPokemon(
            arrayListOf(
                Pokemon(1,"bulb", 1,1, "hei", 1),
                Pokemon(2, "ivy", 2,2,"hei", 1)
            )
        )
    }


}