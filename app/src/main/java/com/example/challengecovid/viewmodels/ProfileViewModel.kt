package com.example.challengecovid.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.challengecovid.Constants
import com.example.challengecovid.R
import com.example.challengecovid.model.User
import com.example.challengecovid.repository.UserRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class ProfileViewModel(private val userRepository: UserRepository, private val app: Application) :
    AndroidViewModel(app) {

    // by lazy ensures that this is only run once, the first time the current user is requested (and not when they viewmodel is initialized)
    val currentUser: MutableLiveData<User> by lazy {
        val sharedPrefs = app.getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
        val userId = sharedPrefs?.getString(Constants.PREFS_USER_ID, "") ?: ""

        Timber.d(userId)

        if (userId == "") {
            Toast.makeText(app, app.getString(R.string.wrong_user_id_error), Toast.LENGTH_LONG).show()
            // return an empty user so the app won't break and the toast is actually shown to the user
            return@lazy MutableLiveData<User>()
        }

        userRepository.getUser(userId)
    }

    suspend fun saveNewUser(user: User) {
        val uId = userRepository.saveNewUser(user)

        // store the generated userId in the shared prefs to be able to access this user later
        val sharedPrefs = app.getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
        sharedPrefs.edit().putString(Constants.PREFS_USER_ID, uId).apply()

        //currentUser.postValue(user)    //postValue because asynchronous context
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

    fun updateUserName(username: String) = viewModelScope.launch {
        val id = currentUser.value?.userId ?: return@launch
        userRepository.updateUserName(username, id)
    }

    fun updateUserIcon(userIcon: String) = viewModelScope.launch {
        val id = currentUser.value?.userId ?: return@launch
        userRepository.upDateUserIcon(userIcon, id)
    }


}