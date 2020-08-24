package com.example.challengecovid.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    val name = MutableLiveData<String>()

    fun sendName(text: String) {
        name.value = text
    }
}