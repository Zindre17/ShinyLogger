package com.aarstrand.zindre.pokechecklist.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PokemonDao {
    @Query("select * from pokemon")
    fun getAllPokemon() : DataSource.Factory<Int, Pokemon>

    @Query("select * from pokemon limit 5")
    fun get5Pokemon(): List<Pokemon>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(pokemon: List<Pokemon>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pokemon: Pokemon)

    @Query("select count(number) from pokemon")
    fun getCount(): Int
}