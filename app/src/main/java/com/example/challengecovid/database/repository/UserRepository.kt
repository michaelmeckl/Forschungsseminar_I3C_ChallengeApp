package com.example.challengecovid.database.repository

import androidx.lifecycle.LiveData
import com.example.challengecovid.database.ChallengeAppDatabase
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.User
import timber.log.Timber

class UserRepository (private val database: ChallengeAppDatabase) {

    //TODO
    private val userDao = database.userDao()

    /**
     * ################################################
     *                  App Users
     * ################################################
     */

    suspend fun insertNewChallenge(user: User) {
        userDao.insert(user)
        Timber.i("new user inserted in repository")
    }

    suspend fun insertUsers(users: List<User>) {
        userDao.insertAll(users)
        Timber.i("list of users inserted in repository")
    }

    suspend fun updateChallenge(user: User) {
        userDao.update(user)
        Timber.i("user updated in repository: $user")
    }

    fun getUser(id: String): LiveData<User> {
        Timber.i("get user in repository: $id")
        return userDao.getUser(id)
    }

    fun getAllUsers(): LiveData<List<User>> {
        Timber.i("get all Users in repository")
        return userDao.getAllUsers()
    }

}