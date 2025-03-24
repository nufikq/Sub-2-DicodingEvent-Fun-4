package com.example.sub2dicodingeventfun4

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sub2dicodingeventfun4.data.EventRepository
import com.example.sub2dicodingeventfun4.data.local.room.EventDatabase
import com.example.sub2dicodingeventfun4.data.remote.response.Event
import com.example.sub2dicodingeventfun4.data.remote.response.EventDetailResponse
import com.example.sub2dicodingeventfun4.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> get() = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private val repository: EventRepository

    init {
        val favoriteDao = EventDatabase.getDatabase(application).favoriteDao()
        repository = EventRepository(favoriteDao)
    }

    fun fetchEventDetail(eventId: Int) {
        _isLoading.value = true
        _errorMessage.value = null

        val client = ApiConfig.getApiService().getDetailEvent(eventId)
        client.enqueue(object : Callback<EventDetailResponse> {
            override fun onResponse(call: Call<EventDetailResponse>, response: Response<EventDetailResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _event.value = response.body()?.event
                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
            }
        })
    }

    fun toggleFavorite() {
        _event.value?.let { event ->
            viewModelScope.launch {
                if (_isFavorite.value == true) {
                    repository.delete(event.id)
                    _isFavorite.value = false
                } else {
                    repository.insert(event)
                    _isFavorite.value = true
                }
            }
        }
    }

    private fun checkFavoriteStatus(eventId: Int) {
        viewModelScope.launch {
            _isFavorite.value = repository.isEventFavorite(eventId)
        }
    }
}