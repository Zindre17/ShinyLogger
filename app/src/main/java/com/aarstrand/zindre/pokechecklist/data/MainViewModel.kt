package com.aarstrand.zindre.pokechecklist.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel(){

    var launchEvent = MutableLiveData<Launch>()
    var button1 = "Shinydex"
    var button2 = "Hunt"
    var button3 = "Collection"
    var button4 = "Progress"

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