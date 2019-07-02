package com.aarstrand.zindre.pokechecklist.tools

import android.content.Context
import com.aarstrand.zindre.pokechecklist.data.AppDatabase
import com.aarstrand.zindre.pokechecklist.data.CatchRepository
import com.aarstrand.zindre.pokechecklist.data.PokemonRepository

object Injector{

    fun getPokemonRepository(context: Context): PokemonRepository{
        return PokemonRepository.getInstance(
                AppDatabase.getInstance(context.applicationContext).pokemonDao()
        )
    }

    fun getCatchRepository(context: Context): CatchRepository{
        return CatchRepository.getInstance(
                AppDatabase.getInstance(context.applicationContext).catchDao()
        )
    }
}