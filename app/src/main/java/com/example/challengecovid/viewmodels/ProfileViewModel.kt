package com.example.challengecovid.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.challengecovid.App
import com.example.challengecovid.Constants
import com.example.challengecovid.model.User
import com.example.challengecovid.repository.UserRepository
import kotlinx.coroutines.*

class ProfileViewModel(private val userRepository: UserRepository, application: Application) :
    AndroidViewModel(application) {

    val name = MutableLiveData<String>()

    private var currentUserId: String = ""
    var currentUser = MutableLiveData<User>()

    init {
        val app = App.instance
        val sharedPrefs = app.getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
        currentUserId = sharedPrefs?.getString(Constants.PREFS_USER_ID, "") ?: ""

        if (currentUserId == "") {
            Toast.makeText(app, "Provided user id is not correct!", Toast.LENGTH_LONG).show()
        }

        //currentUser.value = getUser(currentUserId)  //TODO

        viewModelScope.launch {
            currentUser.value = userRepository.getUser(currentUserId)
        }
    }

    fun insertNewUser(user: User) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.saveNewUser(user)
            }
        }
    }

    fun getUser(userID: String): User? {
        return runBlocking {
            val result = async(Dispatchers.IO) {
                userRepository.getUser(userID)
            }

            result.await()
        }
    }

    fun updateUser(user: User) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            userRepository.updateUser(user)
        }
    }
}