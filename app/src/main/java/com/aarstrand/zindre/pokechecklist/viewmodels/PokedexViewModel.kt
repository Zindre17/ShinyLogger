package com.aarstrand.zindre.pokechecklist.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.aarstrand.zindre.pokechecklist.data.Pokemon
import com.aarstrand.zindre.pokechecklist.data.PokemonRepository
import com.aarstrand.zindre.pokechecklist.tools.Injector

class PokedexViewModel(application: Application) : AndroidViewModel(application){

    private val repository: PokemonRepository = Injector.getPokemonRepository(application)

    val pokemon: LiveData<PagedList<Pokemon>> =
            repository.getAllPokemon().toLiveData(pageSize = 10)


    init {

    }

}