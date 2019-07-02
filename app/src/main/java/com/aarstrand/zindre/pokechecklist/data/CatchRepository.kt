package com.aarstrand.zindre.pokechecklist.data


import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class CatchRepository(private val catchDao: CatchDao){

    companion object{
        @Volatile private var instance: CatchRepository? = null

        fun getInstance(catchDao: CatchDao) =
            instance?: synchronized(this){
                instance ?: CatchRepository(catchDao).also {instance = it}
            }

    }

    val collection: LiveData<List<Catch>> = catchDao.getCatches()

    @WorkerThread
    suspend fun insert(catch: Catch){
        catchDao.insert(catch)
    }
}