package com.example.challengecovid.viewmodels

import android.app.Application
import android.widget.Toast
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
import timber.log.Timber

class ProfileViewModel(private val userRepository: UserRepository, private val app: Application) :
    AndroidViewModel(app) {

    //TODO: should be a singleLiveData actually
    //private val _currentUserId: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    //val currentUserId: LiveData<String> = _currentUserId

    val currentUser: MutableLiveData<User> by lazy {
        //TODO: hier ist noch die alte user id ?????? überall sonst die richtige, aber das bedeutet das der falsche user in firebase geupdated wird hier
        val sharedPrefs = app.getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
        val userId = sharedPrefs?.getString(Constants.PREFS_USER_ID, "") ?: ""

        //TODO
        Timber.d(userId)

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

                // store the generated userId in the shared prefs to be able to access this user later
                val sharedPrefs = app.getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
                sharedPrefs.edit().putString(Constants.PREFS_USER_ID, uId).apply()
                //TODO
                Timber.d(uId)

                //_currentUserId.postValue(uId)    //postValue because asynchronous context
                currentUser.postValue(user)
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
        withContext(Dispatchers.IO) {
            userRepository.updateUser(user)
        }
    }

    fun updateUserName(username: String) = viewModelScope.launch {
        val id = currentUser.value?.userId ?: return@launch
        userRepository.updateUserName(username, id)
    }

    fun updateUserIcon(userIcon: String) = viewModelScope.launch {
        val id = currentUser.value?.userId ?: return@launch
        //val id = currentUserId.value
        userRepository.upDateUserIcon(userIcon, id)
    }


}