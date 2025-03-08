package com.example.sub2dicodingeventfun4.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.sub2dicodingeventfun4.data.local.entity.EventEntity
import com.example.sub2dicodingeventfun4.data.local.room.EventDao
import com.example.sub2dicodingeventfun4.data.local.room.EventDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class EventRepository(application: Application) {
    private val mFavoriteDao: EventDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = EventDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllNotes(): LiveData<List<EventEntity>> = mFavoriteDao.getAllNotes()
    fun insert(favorite: EventEntity) {
        executorService.execute { mFavoriteDao.insert(favorite) }
    }

    fun delete(favorite: EventEntity) {
        executorService.execute { mFavoriteDao.delete(favorite) }
    }

    fun update(favorite: EventEntity) {
        executorService.execute { mFavoriteDao.update(favorite) }
    }

    fun getFavoriteEvents(): LiveData<List<EventEntity>> = mFavoriteDao.getFavoriteEvents()
}
