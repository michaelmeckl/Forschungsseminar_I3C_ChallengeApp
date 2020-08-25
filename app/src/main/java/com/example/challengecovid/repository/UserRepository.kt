package com.example.challengecovid.repository

import androidx.lifecycle.LiveData
import com.example.challengecovid.database.ChallengeAppDatabase
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.User
import com.example.challengecovid.model.UserChallenge
import timber.log.Timber

class UserRepository (database: ChallengeAppDatabase) {

    private val userDao = database.userDao()

    suspend fun insertNewUser(user: User) {
        userDao.insert(user)
        Timber.i("new user inserted in repository")
    }

    suspend fun insertUsers(users: List<User>) {
        userDao.insertAll(users)
        Timber.i("list of users inserted in repository")
    }

    suspend fun updateUser(user: User) {
        userDao.update(user)
        Timber.i("user updated in repository: $user")
    }

    suspend fun deleteUser(user: User) {
        Timber.i("deleted user in repository")
        return userDao.delete(user)
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