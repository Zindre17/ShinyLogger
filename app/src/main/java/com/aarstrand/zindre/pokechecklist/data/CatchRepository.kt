package com.aarstrand.zindre.pokechecklist.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class CatchRepository(private val catchDao: CatchDao){
    val collection: LiveData<List<Catch>> = catchDao.getCatches()

    @WorkerThread
    suspend fun insert(catch: Catch){
        catchDao.insert(catch)
    }
}