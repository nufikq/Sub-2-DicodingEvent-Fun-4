package com.example.sub2dicodingeventfun4.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sub2dicodingeventfun4.data.EventRepository
import com.example.sub2dicodingeventfun4.data.local.entity.EventEntity
import com.example.sub2dicodingeventfun4.data.local.room.EventDatabase
import com.example.sub2dicodingeventfun4.data.remote.response.Event
import com.example.sub2dicodingeventfun4.data.remote.response.EventResponse
import com.example.sub2dicodingeventfun4.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: EventRepository
    val allFavorites: LiveData<List<EventEntity>>

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> get() = _events

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    init {
        val favoriteDao = EventDatabase.getDatabase(application).favoriteDao()
        repository = EventRepository(favoriteDao)
        allFavorites = repository.allFavorites
    }

    fun fetchFavoriteEvents() {
        _isLoading.value = true
        _errorMessage.value = null

        // Use the repository data but convert EventEntity objects to Event objects
        allFavorites.observeForever { favoriteEvents ->
            _isLoading.value = false
            if (favoriteEvents != null) {
                // Map EventEntity to Event objects
                val events = favoriteEvents.map { entity ->
                    Event(
                        id = entity.id,
                        name = entity.name,
                        description = entity.description,
                        beginTime = entity.beginTime,
                        endTime = entity.endTime,
                        quota = entity.quota,
                        registrants = entity.registrants,
                        mediaCover = entity.mediaCover,
                        link = entity.link,
                        ownerName = entity.ownerName,
                        category = entity.category,
                        cityName = entity.cityName,
                        imageLogo = entity.imageLogo,
                        summary = entity.summary
                    )
                }
                _events.value = events
            } else {
                _errorMessage.value = "No favorite events found"
            }
            // Remove the observer to prevent memory leaks
            allFavorites.removeObserver { }
        }
    }

    /*fun fetchFavoriteEvents() {
        _isLoading.value = true
        allFavorites.observeForever { favoriteEvents ->
            _isLoading.value = false
            if (favoriteEvents != null) {
                _events.value = favoriteEvents
            } else {
                _errorMessage.value = "Failed to fetch favorite events"
            }
        }
    }*/
}