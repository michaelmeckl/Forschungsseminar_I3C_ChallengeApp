package com.example.challengecovid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.challengecovid.database.repository.ChallengeRepository
import com.example.challengecovid.database.repository.UserRepository
import com.example.challengecovid.model.User
import com.example.challengecovid.model.UserChallenge
import kotlinx.coroutines.*

class ProfileViewModel (private val userRepository: UserRepository): ViewModel() {
    val name = MutableLiveData<String>()

    private var viewModelJob = SupervisorJob()

    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var currentUser = MutableLiveData<User>()

    val allUsers: LiveData<List<User>> = userRepository.getAllUsers()

    private var _showSnackbarEvent = MutableLiveData<Boolean?>()

    val showSnackBarEvent: LiveData<Boolean?>
        get() = _showSnackbarEvent

    fun sendName(text: String) {
        name.value = text
    }

    fun insertNewUser(user: User){
        uiScope.launch {
            withContext(Dispatchers.IO){
                userRepository.insertNewUser(user)
            }
            _showSnackbarEvent.value = true
        }
    }

    fun getUser(userID: String){
        uiScope.launch {
            withContext(Dispatchers.IO){
                userRepository.getUser(userID)
            }
            _showSnackbarEvent.value = true
        }
    }

    private fun initializeUser(userID: String) {
        uiScope.launch {
            currentUser.value = getUserFromDatabase(userID)
        }
    }

    private suspend fun getUserFromDatabase(userID: String): User? {
        return withContext(Dispatchers.IO) {
            userRepository.getUser(userID).value
        }
    }

    fun updateUser(user: User) = uiScope.launch {
        withContext(Dispatchers.IO) {
            userRepository.updateUser(user)
        }
        _showSnackbarEvent.value = true
    }

    fun removeUser(user: User) = uiScope.launch {
        withContext(Dispatchers.IO) {
            userRepository.deleteUser(user)
        }
        _showSnackbarEvent.value = true
    }
/*
    fun updateUserName(userID: String, userName: String) = uiScope.launch {
        withContext(Dispatchers.IO){
            userRepository.updateUserName(userID,userName)
        }
        _showSnackbarEvent.value = true
    }

    fun updateUserIcon(userID: String, userIcon: String) = uiScope.launch {
        withContext(Dispatchers.IO){
            userRepository.updateUserIcon(userID,userIcon)
        }
        _showSnackbarEvent.value = true
    }
*/
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}