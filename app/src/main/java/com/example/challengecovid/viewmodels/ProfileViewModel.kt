package com.example.challengecovid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.challengecovid.database.repository.ChallengeRepository
import com.example.challengecovid.database.repository.UserRepository
import com.example.challengecovid.model.User
import com.example.challengecovid.model.UserChallenge
import kotlinx.coroutines.*

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private var viewModelJob = SupervisorJob()

    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun createNewUser(user: User) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.insertNewUser(user)
            }
        }
    }


    val name = MutableLiveData<String>()

    val allUsers: LiveData<List<User>> = userRepository.getAllUsers()

    fun sendName(text: String) {
        name.value = text
    }
}