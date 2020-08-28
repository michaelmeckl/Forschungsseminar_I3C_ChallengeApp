package com.example.challengecovid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.challengecovid.model.User
import com.example.challengecovid.repository.UserRepository

class ProfileViewModel (private val userRepository: UserRepository): ViewModel() {
    val name = MutableLiveData<String>()

    val allUsers: LiveData<List<User>> = userRepository.getAllUsers()

    fun sendName(text: String) {
        name.value = text
    }
}