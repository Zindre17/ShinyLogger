package com.aarstrand.zindre.pokechecklist.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.aarstrand.zindre.pokechecklist.data.Pokemon
import com.aarstrand.zindre.pokechecklist.data.PokemonRepository
import com.aarstrand.zindre.pokechecklist.tools.Injector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class RegisterViewModel(application: Application) : AndroidViewModel(application){

    private val repository: PokemonRepository = Injector.getPokemonRepository(application)

    val pokemon = MutableLiveData<Pokemon>()

    val encounters = MutableLiveData<String>()

    val buttonLabel = "Register"

    fun setNumber(int: Int){
        runBlocking{
            pokemon.value = withContext(Dispatchers.Default) { repository.getPokemon(int) }
        }

    }

    fun setEncounters(int: Int){
        encounters.value = int.toString()
    }
}