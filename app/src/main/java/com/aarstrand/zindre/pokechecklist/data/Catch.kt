package com.aarstrand.zindre.pokechecklist.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity (
        tableName = "catches",
        foreignKeys = [ForeignKey(entity = Pokemon::class, parentColumns = ["number"], childColumns = ["number"])]
)
data class Catch(
        val number: Int,
        val ball: Int,
        val game: Int,
        val attempts: Int,
        val method: Int,
        val shinyCharm: Boolean
){
    @PrimaryKey (autoGenerate = true) var id: Long = 0


}