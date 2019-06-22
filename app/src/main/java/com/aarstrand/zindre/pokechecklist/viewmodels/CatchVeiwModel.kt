package com.aarstrand.zindre.pokechecklist.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.aarstrand.zindre.pokechecklist.data.AppDatabase
import com.aarstrand.zindre.pokechecklist.data.Catch
import com.aarstrand.zindre.pokechecklist.data.CatchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CatchVeiwModel(application: Application) : AndroidViewModel(application){

    val collection: LiveData<List<Catch>>
    private val repository: CatchRepository

    init{
        val catchDao = AppDatabase.getInstance(application).catchDao()
        repository = CatchRepository(catchDao)
        collection = repository.collection
    }

    fun insert(catch: Catch) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(catch)
    }
}