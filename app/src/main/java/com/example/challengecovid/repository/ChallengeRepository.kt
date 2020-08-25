package com.example.challengecovid.repository

import androidx.lifecycle.LiveData
import com.example.challengecovid.database.ChallengeAppDatabase
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.UserChallenge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Used as an abstraction layer between data source(s) and the frontend (client business logic)
 */
class ChallengeRepository(database: ChallengeAppDatabase) {

    private val challengeDao = database.challengeDao()
    private val userChallengeDao = database.userChallengeDao()

    /**
     * ################################################
     *                  App Challenges
     * ################################################
     */

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

    fun getChallenge(id: String): LiveData<Challenge> {
        Timber.i("get challenge in repository: $id")
        return challengeDao.getChallenge(id)
    }

    fun getAllChallenges(): LiveData<List<Challenge>> {
        Timber.i("get all challenges in repository")
        return challengeDao.getAllChallenges()
    }

    /**
     * ################################################
     *              User Challenges
     * ################################################
     */

    suspend fun insertUserChallenge(userChallenge: UserChallenge) {
        userChallengeDao.insert(userChallenge)
        Timber.i("new user challenge inserted in repository")
    }

    suspend fun insertUserChallenges(userChallenges: List<UserChallenge>) {
        userChallengeDao.insertAll(userChallenges)
        Timber.i("list of user challenges inserted in repository")
    }

    suspend fun updateUserChallenge(userChallenge: UserChallenge) {
        userChallengeDao.update(userChallenge)
        Timber.i("user challenge updated in repository: $userChallenge")
    }

    // only allow delete for user challenges, not for the app challenges!
    suspend fun deleteUserChallenge(userChallenge: UserChallenge) {
        Timber.i("delete challenge in repository: $userChallenge")
        return userChallengeDao.delete(userChallenge)
    }

    suspend fun deleteAllUserChallenges() {
        userChallengeDao.clear()
        Timber.i("deleted all user created challenges in repository")
    }

    fun getUserChallenge(id: String): LiveData<UserChallenge> {
        Timber.i("get user created challenge in repository: $id")
        return userChallengeDao.getUserChallenge(id)
    }

    fun getAllUserChallenges(): LiveData<List<UserChallenge>> {
        Timber.i("get all user created challenges in repository")
        return userChallengeDao.getAllUserChallenges()
    }

    /**
     * ################################################
     *                      Misc
     * ################################################
     */

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