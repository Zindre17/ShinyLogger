package com.aarstrand.zindre.pokechecklist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class Pokemon(
        @PrimaryKey val number: Int,
        val name: String,
        val type1: Int,
        val type2: Int,
        val image: ByteArray,
        val gen: Int,
        val collected: Int
){
}