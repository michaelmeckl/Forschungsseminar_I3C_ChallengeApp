package com.example.challengecovid.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.challengecovid.database.ChallengeDatabase
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.ChallengeUI
import com.example.challengecovid.model.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Used as an abstraction layer between data source(s) and the frontend (client business logic)
 */
class ChallengeRepository(private val database: ChallengeDatabase) {
    val challenges: LiveData<List<ChallengeUI>> = Transformations.map(database.challengeDao().getAllChallenges()) {
        it.asDomainModel()
    }

    fun getChallenge(id: Int): LiveData<Challenge> {
        return database.challengeDao().get(id)
    }

    /**
     * Refresh the challenges stored in the offline cache (Room).
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     */
    suspend fun refreshChallenges() {
        withContext(Dispatchers.IO) {
            Timber.d("refresh challenges is called")
            //TODO: insert new challenges
            /*
            val challenges = TODO()
            database.challengeDao().insertAll(challenges.asDatabaseModel())
            */
        }
    }
}