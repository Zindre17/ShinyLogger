package com.aarstrand.zindre.pokechecklist.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CatchDao {
    @Query("select * from catches")
    fun getCatches(): LiveData<List<Catch>>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(catch: Catch)
}