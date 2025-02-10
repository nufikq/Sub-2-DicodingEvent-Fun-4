package com.example.sub2dicodingeventfun4.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Dicoding Event App\n\nYou can explore\n upcoming events\nand finished events"
    }
    val text: LiveData<String> = _text
}