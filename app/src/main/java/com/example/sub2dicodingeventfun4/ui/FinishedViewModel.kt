package com.example.sub2dicodingeventfun4.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sub2dicodingeventfun4.ApiConfig
import com.example.sub2dicodingeventfun4.Event
import com.example.sub2dicodingeventfun4.EventResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel: ViewModel() {

    // Begin
    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> get() = _events

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun fetchEvents() {
        _isLoading.value = true
        _errorMessage.value = null

        val client = ApiConfig.getApiService().getEvents(0)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.listEvents.isNullOrEmpty()) {
                        _events.value = emptyList()
                    } else {
                        _events.value = responseBody?.listEvents
                    }
                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                    Log.e("EventViewModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
                Log.e("EventViewModel", "onFailure: ${t.message}")
            }
        })
    }
}