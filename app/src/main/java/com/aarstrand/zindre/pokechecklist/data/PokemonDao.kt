package com.aarstrand.zindre.pokechecklist.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*

@Dao
interface PokemonDao {

    @Query("select * from pokemon where number = :number")
    fun getPokemon(number: Int): Pokemon

    @Query("select * from pokemon")
    fun getAllPokemon() : DataSource.Factory<Int, Pokemon>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(pokemon: List<Pokemon>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pokemon: Pokemon)

    @Query("select count(number) from pokemon")
    fun getCount(): Int

    @Query("update pokemon set collected = collected+1 where number = :number")
    fun caught(number: Int)

    @Query("update pokemon set collected = collected-1 where number = :number")
    fun released(number: Int)
}