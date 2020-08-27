package com.example.challengecovid

import com.example.challengecovid.repository.CategoryRepository
import com.example.challengecovid.repository.ChallengeRepository
import com.example.challengecovid.repository.UserRepository

/**
 * Util-Singleton to provide easy access to the repositories.
 */
object RepositoryController {

    //a local reference to the application context
    //private val application = App.instance
    //private val localDB = ChallengeAppDatabase.getInstance(application, CoroutineScope(Dispatchers.IO))

    fun getChallengeRepository() = ChallengeRepository()

    fun getCategoryRepository() = CategoryRepository()

    fun getUserRepository() = UserRepository()

}