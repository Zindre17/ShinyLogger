package com.aarstrand.zindre.pokechecklist.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aarstrand.zindre.pokechecklist.R

class MainViewModel: ViewModel(){

    var launchEvent = MutableLiveData<Launch>()

    var b1Text: String = "Shinydex"
    var b1Image: Int = R.drawable.pokedex

    var b2Text: String = "Hunt"
    var b2Image: Int = R.drawable.hunt

    var b3Text: String = "Collection"
    var b3Image: Int = R.drawable.collection

    var b4Text: String = "Progress"
    var b4Image: Int = 0

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