package com.example.sub2dicodingeventfun4.data

import androidx.lifecycle.LiveData
import com.example.sub2dicodingeventfun4.data.local.entity.EventEntity
import com.example.sub2dicodingeventfun4.data.local.room.EventDao
import com.example.sub2dicodingeventfun4.data.remote.response.Event

class EventRepository(private val favoriteDao: EventDao) {
    val allFavorites: LiveData<List<EventEntity>> = favoriteDao.getAllFavorites()

    suspend fun insert(event: Event) {
        val favoriteEvent = EventEntity(
            id = event.id,
            name = event.name,
            description = event.description,
            beginTime = event.beginTime,
            endTime = event.endTime,
            quota = event.quota,
            registrants = event.registrants,
            mediaCover = event.mediaCover,
            link = event.link,
            ownerName = event.ownerName,
            category = event.category,
            cityName = event.cityName,
            imageLogo = event.imageLogo,
            summary = event.summary
        )
        favoriteDao.insert(favoriteEvent)
    }

    suspend fun delete(eventId: Int) {
        favoriteDao.delete(eventId)
    }

    suspend fun isEventFavorite(eventId: Int): Boolean {
        return favoriteDao.isEventFavorite(eventId) > 0
    }
}