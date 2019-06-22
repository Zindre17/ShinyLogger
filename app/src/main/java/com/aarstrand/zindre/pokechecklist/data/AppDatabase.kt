package com.aarstrand.zindre.pokechecklist.data

import android.content.Context
import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database (entities = [Pokemon::class, Catch::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun pokemonDao(): PokemonDao
    abstract fun catchDao(): CatchDao

    companion object{
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this){
                val i = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "shiny_database"
                ).build()
                instance = i
                return i
            }
        }
    }
}