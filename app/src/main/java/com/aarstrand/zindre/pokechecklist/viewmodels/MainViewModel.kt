package com.aarstrand.zindre.pokechecklist.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aarstrand.zindre.pokechecklist.R

class MainViewModel: ViewModel(){

    var launchEvent = MutableLiveData<Launch>()

    val b1Text: String = "Shinydex"
    val b1Image: Int = R.drawable.pokedex
    val b1Launch: Launch = Launch.DEX

    val b2Text: String = "Hunt"
    val b2Image: Int = R.drawable.hunt
    val b2Launch: Launch = Launch.HUNT

    val b3Text: String = "Collection"
    val b3Image: Int = R.drawable.collection
    val b3Launch: Launch = Launch.COLL

    val b4Text: String = "Progress"
    val b4Image: Int = 0
    val b4Launch: Launch = Launch.PROG

    fun onClick(item : Launch){
        launchEvent.value = item
    }

}

enum class Launch{
    NONE,
    DEX,
    HUNT,
    COLL,
    PROG
}