package com.example.challengecovid.repository

import androidx.lifecycle.LiveData
import com.example.challengecovid.database.ChallengeDatabase
import com.example.challengecovid.model.Challenge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Used as an abstraction layer between data source(s) and the frontend (client business logic)
 */
class ChallengeRepository(private val database: ChallengeDatabase) {

    private val challengeDao = database.challengeDao()

    suspend fun insertNewChallenge(challenge: Challenge) {
        challengeDao.insert(challenge)
        Timber.i("new challenge inserted in repository")
    }

    suspend fun insertChallenges(challenges: List<Challenge>) {
        challengeDao.insertAll(challenges)
        Timber.i("list of challenges inserted in repository")
    }

    suspend fun updateChallenge(challenge: Challenge) {
        challengeDao.update(challenge)
        Timber.i("challenge updated in repository: $challenge")
    }

    suspend fun deleteChallenge(id: String) {
        Timber.i("delete challenge in repository: $id")
        return challengeDao.delete(id)
    }

    suspend fun deleteAllChallenges() {
        challengeDao.clear()
        Timber.i("deleted all challenges in repository")
    }

    fun getChallenge(id: String): LiveData<Challenge> {
        Timber.i("get challenge in repository: $id")
        return challengeDao.get(id)
    }

    fun getAllChallenges(): LiveData<List<Challenge>> {
        Timber.i("get all challenges in repository called")
        return challengeDao.getAllChallenges()
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
            //TODO: fetch new challenges from internet and insert them
            /*
            val challenges = TODO()
            challengeDao.insertAll(challenges.asDatabaseModel())
            */
        }
    }
}