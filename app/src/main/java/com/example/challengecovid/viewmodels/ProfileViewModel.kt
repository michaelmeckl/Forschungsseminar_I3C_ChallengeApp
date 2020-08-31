package com.example.challengecovid.viewmodels

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.challengecovid.Constants
import com.example.challengecovid.model.User
import com.example.challengecovid.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(private val userRepository: UserRepository, application: Application) :
    AndroidViewModel(application) {

    //TODO: should be a singleLiveData actually
    private val _currentUserId: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val currentUserId: LiveData<String> = _currentUserId

    val currentUser: MutableLiveData<User> by lazy {
        val sharedPrefs = application.getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
        val userId = sharedPrefs?.getString(Constants.PREFS_USER_ID, "") ?: ""

        /*
        Timber.d("userId: $userId")
        if (userId == "") {
            Toast.makeText(App.instance, "Provided user id is not correct! Please restart!", Toast.LENGTH_LONG).show()
        }*/

        userRepository.getUser(userId)
    }

    fun saveNewUser(user: User) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val uId = userRepository.saveNewUser(user)
                _currentUserId.postValue(uId)    //postValue because asynchronous context
                //currentUser.postValue(user)
            }
        }
    }

    /*
    fun getUser(userID: String): User? {
        return runBlocking {
            val result = async(Dispatchers.IO) {
                userRepository.getUser(userID)
            }

            result.await()
        }
    }*/

    fun updateUser(user: User) = viewModelScope.launch {
        //currentUser.value = user
        withContext(Dispatchers.IO) {
            userRepository.updateUser(user)
        }
    }

    fun updateUserName(username: String) = viewModelScope.launch {
        //val us = currentUser.value
        //us?.username = username
        val id = currentUser.value?.userId ?: return@launch
        userRepository.updateUserName(username, id)
    }

    fun updateUserIcon(userIcon: String) = viewModelScope.launch {
        //val us = currentUser.value
        //us?.userIcon = userIcon
        val id = currentUser.value?.userId ?: return@launch
        //val id = currentUserId.value
        userRepository.upDateUserIcon(userIcon, id)
    }


}