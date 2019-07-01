package com.aarstrand.zindre.pokechecklist.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.aarstrand.zindre.pokechecklist.R
import com.aarstrand.zindre.pokechecklist.data.AppDatabase
import com.aarstrand.zindre.pokechecklist.data.Pokemon
import com.aarstrand.zindre.pokechecklist.data.PokemonDao
import com.aarstrand.zindre.pokechecklist.data.PokemonRepository
import com.aarstrand.zindre.pokechecklist.db.PokeCheckListDbHelper
import org.json.JSONArray
import java.io.ByteArrayOutputStream

class PokedexCreator(context: Context, params: WorkerParameters): CoroutineWorker(context, params){

    override suspend fun doWork(): Result {
        if(pokemonRepository.count < 100) {
            return try {
                println(AppDatabase.getInstance(applicationContext).query("select * from pokemon",null).count)
                createAndFillInDB(applicationContext)
                println(pokemonRepository.count)
                println(AppDatabase.getInstance(applicationContext).query("select * from pokemon",null).count)

                Result.success()
            } catch (error: Error) {
                Result.failure()
            }
        }
        return Result.success()
    }
    private val offsetList: Array<String>
    private val genSwitchList: Array<String>
    private var pokemonArray: JSONArray? = null
    private val dbHelper: PokeCheckListDbHelper = PokeCheckListDbHelper.getInstance(context)
    private val pokemonRepository: PokemonRepository = PokemonRepository(AppDatabase.getInstance(context).pokemonDao())
    init {
        println(pokemonRepository.count)

        try {
            pokemonArray = JSONArray(context.resources.getString(R.string.pokemons))
        } catch (e: Exception) {
        }

        genSwitchList = context.getString(R.string.genswitch).trim { it <= ' ' }.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        offsetList = context.getString(R.string.image_skip_list).trim { it <= ' ' }.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    }



    private fun switchSpriteSheet(context: Context, gen: Int): Bitmap? {
        when (gen) {
            1 -> return BitmapFactory.decodeResource(context.resources, R.drawable.gen1)
            2 -> return BitmapFactory.decodeResource(context.resources, R.drawable.gen2)
            3 -> return BitmapFactory.decodeResource(context.resources, R.drawable.gen3)
            4 -> return BitmapFactory.decodeResource(context.resources, R.drawable.gen4)
            5 -> return BitmapFactory.decodeResource(context.resources, R.drawable.gen5)
            6 -> return BitmapFactory.decodeResource(context.resources, R.drawable.gen6)
        }
        return null
    }

    private fun getByteArrayImage(row: Int, col: Int, bitmap: Bitmap): ByteArray {

        val outputStream = ByteArrayOutputStream()
        val size = bitmap.width / 10
        val left = col * size
        val top = row * size
        val pokemonThumbnail = Bitmap.createBitmap(bitmap, left, top, size, size)
        pokemonThumbnail.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
        return outputStream.toByteArray()
    }

    private suspend fun createAndFillInDB(context: Context) {

        var spriteSheet: Bitmap? = BitmapFactory.decodeResource(
                context.applicationContext.resources,
                R.drawable.gen1)

        var row = 0
        var col = 0

        var genSwitchListPos = 0
        var offsetListPos = 0


        var nextOffset = Integer.parseInt(offsetList[offsetListPos].split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0])
        var genSwitchPoint = Integer.parseInt(genSwitchList[genSwitchListPos])

        val size = pokemonArray!!.length()

        for (dexNumber in 1..size) {
            if (dexNumber == genSwitchPoint) {
                col = 0
                row = 0
                genSwitchListPos++
                genSwitchPoint = Integer.parseInt(genSwitchList[genSwitchListPos])
                spriteSheet = switchSpriteSheet(context.applicationContext, genSwitchListPos + 1)

            } else if (dexNumber == nextOffset) {

                col += Integer.parseInt(offsetList[offsetListPos].split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1])
                offsetListPos++
                nextOffset = Integer.parseInt(offsetList[offsetListPos].split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0])
            }
            if (col > 9) {
                row += Math.floor((col / 10).toDouble()).toInt()
                col = col % 10
            }
            try {
                pokemonRepository.insert(
                        Pokemon(
                                dexNumber,
                                pokemonArray!!.getString(dexNumber-1),
                                0,0,
                                getByteArrayImage(row,col, spriteSheet!!),genSwitchListPos +1))
                /*
                dbHelper.insertPokemon(
                        pokemonArray?.getString(dexNumber - 1),
                        dexNumber,
                        genSwitchListPos + 1,
                        getByteArrayImage(row, col, spriteSheet!!)
                )
                */
                //println("inserted pokemon")
            } catch (e: Exception) {
                e.printStackTrace()
            }

            col++
        }
    }
}