package com.example.challengecovid.repository

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.challengecovid.App
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.ChallengeType
import com.example.challengecovid.model.UserChallenge
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import timber.log.Timber

/**
 * Used as an abstraction layer between data source(s) and the frontend (client business logic)
 */
class ChallengeRepository {

    // reference to the root default challenge collection in firestore
    private val challengeCollection = FirebaseFirestore.getInstance().collection("challenges")

    // reference to the root userChallenge collection in firestore
    private val userChallengeCollection = FirebaseFirestore.getInstance().collection("userChallenges")


    companion object {
        const val CHALLENGE_REPO_TAG = "CHALLENGE_REPOSITORY"
    }

    /**
     * ################################################
     *                  App Challenges
     * ################################################
     */

    // GET-ALL
    /*
    fun getAllChallenges(): LiveData<List<Challenge>> = liveData(Dispatchers.IO) {
        while (true) {
            val allChallenges = fetchChallengesFromFirebase()
            allChallenges?.let { emit(it) }
            delay(3000)     // refresh for new data every 3 seconds
        }
    }

    private suspend fun fetchChallengesFromFirebase(): List<Challenge>? {
        return try {
            val challengeList: MutableList<Challenge> = ArrayList()
            val docSnapshots = challengeCollection.get().await().documents

            if (docSnapshots.isNotEmpty()) {
                for (snapshot in docSnapshots)
                    snapshot.toObject(Challenge::class.java)?.let {
                        challengeList.add(it)
                    }
            }

            challengeList
        } catch (e: Exception) {
            Timber.tag(CHALLENGE_REPO_TAG).d(e)
            null
        }
    }*/

    //GET
    suspend fun getChallenge(id: String): Challenge? {
        val challengeSnapshot = challengeCollection.document(id).get().await()
        return challengeSnapshot.toObject(Challenge::class.java)
    }

    //CREATE-MULTIPLE
    fun saveMultipleChallenges(challengeList: List<Challenge>) {
        //use a batched write to insert all at the same time to prevent possible inconsistencies!
        val batchWrite = FirebaseFirestore.getInstance().batch()

        for (challenge in challengeList) {
            // create a new reference for this challenge
            val docRef = challengeCollection.document(challenge.challengeId)
            // and add it to the WriteBatch
            batchWrite.set(docRef, challenge, SetOptions.merge())
        }

        // commit the batch (i.e. write all to the db)
        batchWrite.commit().addOnSuccessListener {
            Timber.tag(CHALLENGE_REPO_TAG).d("Challenge Batch inserted successfully!")
        }.addOnFailureListener { e ->
            Timber.tag(CHALLENGE_REPO_TAG).d("Failed to insert challenge batch: $e!")
        }
    }


    /**
     * ################################################
     *                     Both
     * ################################################
     */

    //UPDATE
    fun updateCompletionStatus(id: String, challengeType: ChallengeType, completed: Boolean) {
        val challengeRef: DocumentReference = if(challengeType == ChallengeType.SYSTEM_CHALLENGE) {
            challengeCollection.document(id)
        } else  {
            userChallengeCollection.document(id)
        }

        challengeRef
            .update("completed", completed)
            .addOnSuccessListener { Timber.tag(CHALLENGE_REPO_TAG).d("Challenge successfully marked as completed!") }
            .addOnFailureListener { e -> Timber.tag(CHALLENGE_REPO_TAG).d("Error updating challenge: $e") }
    }

    /**
     * ################################################
     *              User Challenges
     * ################################################
     */

    //TODO: im moment sind die doppelt drin! hier wäre eigentlich eine collection group query nötig!
    // -> sonst inkonsistenzen!
    // -> sollten die user challenges lieber alle als subcollection bei den users sein, um duplikate zu vermeiden??

    // GET-ALL
    fun getPublicUserChallenges(): LiveData<List<UserChallenge>> = liveData(Dispatchers.IO) {
        // `while(true)` is fine because the `delay` below will cooperate in
        // cancellation if LiveData is not actively observed anymore
        /*
        while (true) {
            val allChallenges = fetchPublicChallengesFromFirebase()
            allChallenges?.let { emit(it) }
            delay(10_000)     // refresh for new data every 2 seconds
        }*/
        val allChallenges = fetchPublicChallengesFromFirebase()
        allChallenges?.let { emit(it) }
    }

    private suspend fun fetchPublicChallengesFromFirebase(): List<UserChallenge>? {
        return try {
            val challengeList: MutableList<UserChallenge> = ArrayList()
            val docSnapshots = userChallengeCollection
                .whereEqualTo("isPublic", true)   // get the user challenges that are public
                .orderBy("createdAt", Query.Direction.DESCENDING)  // order them by creation date with the newest first
                .get().await().documents    // wait for completion and convert them to document snapshots

            if (docSnapshots.isNotEmpty()) {
                for (snapshot in docSnapshots)
                    snapshot.toObject(UserChallenge::class.java)?.let {
                        challengeList.add(it)
                    }
            }

            challengeList
        } catch (e: Exception) {
            Timber.tag(CHALLENGE_REPO_TAG).d(e)
            null
        }
    }

    //GET
    suspend fun getUserChallenge(id: String): UserChallenge? {
        val challengeSnapshot = userChallengeCollection.document(id).get().await()
        return challengeSnapshot.toObject(UserChallenge::class.java)
    }

    //CREATE
    fun saveNewUserChallenge(userChallenge: UserChallenge): String {
        //val challengeReference = challengeCollection.document()   // create a new document with an auto-generated id
        val challengeReference = userChallengeCollection.document(userChallenge.challengeId)

        //NOTE: use set(challenge, SetOptions.merge()) to only update the parts that changed!
        challengeReference.set(userChallenge).addOnSuccessListener {
            Toast.makeText(App.instance, "User Challenge saved successfully!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Toast.makeText(App.instance, "Failed to save new user challenge: $e", Toast.LENGTH_SHORT).show()
        }

        return challengeReference.id
    }

    //UPDATE
    fun updateUserChallenge(userChallenge: UserChallenge) {
        val oldChallengeRef = userChallengeCollection.document(userChallenge.challengeId)

        oldChallengeRef
            .set(userChallenge)
            .addOnSuccessListener { Timber.tag(CHALLENGE_REPO_TAG).d("User Challenge successfully updated!") }
            .addOnFailureListener { e -> Timber.tag(CHALLENGE_REPO_TAG).d("Error updating user challenge: $e") }
    }

    //UPDATE
    fun updatePublicStatus(challengeId: String, publicStatus: Boolean): Boolean {
        val challengeRef = userChallengeCollection.document(challengeId)
        // TODO: Ich wünschte das würde so gehen :(
        var isSuccess= false

        challengeRef
            .update("isPublic", publicStatus)
            .addOnSuccessListener { Timber.tag(CHALLENGE_REPO_TAG).d("User Challenge successfully published!") ; isSuccess = true}
            .addOnFailureListener { e -> Timber.tag(CHALLENGE_REPO_TAG).d("Error publishing user challenge: $e") ; isSuccess = false}
        return isSuccess
    }

    //DELETE
    fun deleteUserChallenge(userChallenge: UserChallenge) {
        val challengeRef = userChallengeCollection.document(userChallenge.challengeId)

        challengeRef.delete()
            .addOnSuccessListener { Timber.tag(CHALLENGE_REPO_TAG).d("User Challenge successfully deleted!") }
            .addOnFailureListener { e -> Timber.tag(CHALLENGE_REPO_TAG).d("Error deleting User Challenge: $e") }
    }


    /**
     * ################################################
     *                  for Room
     * ################################################
     */
    /*
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
            // fetch new challenges from internet and insert them
            /*
            val challenges = emptyList()
            challengeDao.insertAll(challenges.asDatabaseModel())
            */
        }
    }
    */
}