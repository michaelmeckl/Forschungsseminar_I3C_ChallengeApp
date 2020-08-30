package com.example.challengecovid.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.challengecovid.App
import com.example.challengecovid.Constants
import com.example.challengecovid.model.User
import com.example.challengecovid.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class ProfileViewModel(private val userRepository: UserRepository, application: Application) :
    AndroidViewModel(application) {

    val name = MutableLiveData<String>()

    private var currentUserId: String = ""
    private lateinit var currentUser: MutableLiveData<User>

    object CurrentUser {
        val app = App.instance
        private val sharedPrefs = app.getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
        private val currentUserId = sharedPrefs?.getString(Constants.PREFS_USER_ID, "") ?: ""

        private val currentUser: User = User(currentUserId)

        fun getCurrentUser(): User {
            return currentUser
        }
    }

    init {
        if(currentUserId == "") {
            val app = App.instance
            val sharedPrefs = app.getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
            currentUserId = sharedPrefs?.getString(Constants.PREFS_USER_ID, "") ?: ""

            Timber.d("userId: $currentUserId")
            if (currentUserId == "") {
                Toast.makeText(app, "Provided user id is not correct! Please restart!", Toast.LENGTH_LONG).show()
            } else {
                viewModelScope.launch {
                    currentUser = userRepository.getUser(currentUserId)
                    Timber.d("hallo")
                }
            }
        }
    }



/*
    fun getCurrentUser(): User? {
        if(this::currentUser.isInitialized) {
            return currentUser.value
        }
        return null
    }


 */


    fun observeImage(): LiveData<String> = liveData(Dispatchers.IO) {
        while (true) {
            val icon = currentUser.value?.userIcon
            if (icon != null) {
                emit(icon)
            }
            delay(3000)
        }
    }

    /*
    fun insertNewUser(user: User): String? {
        var id: String? = null
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                id = userRepository.saveNewUser(user)
            }
        }
        return id
    }*/

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
        currentUser.value = user    //TODO: this would be redundant if livedata is used for the currentuser instead!
        withContext(Dispatchers.IO) {
            userRepository.updateUser(user)
        }
    }

    fun updateUserName(username: String) = viewModelScope.launch {
        currentUser.value?.username = username
        userRepository.updateUserName(username, currentUserId)
    }

    fun updateUserIcon(userIcon: String) = viewModelScope.launch {
        currentUser.value?.userIcon = userIcon
        userRepository.upDateUserIcon(userIcon,currentUserId)
    }


}