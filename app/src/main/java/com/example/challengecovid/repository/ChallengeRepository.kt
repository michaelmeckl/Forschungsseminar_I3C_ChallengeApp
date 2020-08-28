package com.example.challengecovid.repository

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.challengecovid.App
import com.example.challengecovid.model.BaseChallenge
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.UserChallenge
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import timber.log.Timber

/**
 * Used as an abstraction layer between data source(s) and the frontend (client business logic)
 */
class ChallengeRepository {

    // reference to the root default challenge collection in firestore
    private val challengeCollection =
        FirebaseFirestore.getInstance().collection("challenges")  //TODO: eigtl. in categories

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
    fun getAllChallenges(): LiveData<List<Challenge>> = liveData(Dispatchers.Main) {
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
    }

    //GET
    fun getChallenge(id: String): Challenge? = runBlocking(Dispatchers.Main) {
        // use an async coroutine builder to defer the work to the IO-Thread and return a value to this scope
        val deferredResult = async(Dispatchers.IO) {
            val challengeSnapshot = challengeCollection.document(id).get().await()
            return@async challengeSnapshot.toObject(Challenge::class.java)
        }
        // return the completed result
        deferredResult.await()
    }


    //CREATE
    fun saveNewChallenge(challenge: Challenge): String {
        //val challengeReference = challengeCollection.document()   // create a new document with an auto-generated id
        val challengeReference = challengeCollection.document(challenge.challengeId)

        //NOTE: use set(challenge, SetOptions.merge()) to only update the parts that changed!
        challengeReference.set(challenge).addOnSuccessListener {
            Toast.makeText(App.instance, "Challenge saved successfully!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Toast.makeText(App.instance, "Failed to save new challenge: $e", Toast.LENGTH_SHORT).show()
        }

        return challengeReference.id
    }

    //CREATE-MULTIPLE
    fun saveMultipleChallenges(challengeList: List<Challenge>) {
        //use a batched write to insert all at the same time to prevent possible inconsistencies!
        val batchWrite = FirebaseFirestore.getInstance().batch()

        for (challenge in challengeList) {
            // create a new reference for this challenge
            val docRef = challengeCollection.document(challenge.challengeId)
            // and add it to the WriteBatch
            batchWrite.set(docRef, challenge)
        }

        // commit the batch (i.e. write all to the db)
        batchWrite.commit().addOnSuccessListener {
            Timber.tag(CHALLENGE_REPO_TAG).d("Challenge Batch inserted successfully!")
        }.addOnFailureListener { e ->
            Timber.tag(CHALLENGE_REPO_TAG).d("Failed to insert challenge batch: $e!")
        }
    }

    //UPDATE
    fun updateChallenge(challenge: Challenge) {
        val oldChallengeRef = challengeCollection.document(challenge.challengeId)

        oldChallengeRef
            //.update("description", challenge.description, "title", challenge.title)
            .set(challenge)     //using set(data, SetOptions.merge()) to only update the parts that changed!
            .addOnSuccessListener { Timber.tag(CHALLENGE_REPO_TAG).d("Challenge successfully updated!") }
            .addOnFailureListener { e -> Timber.tag(CHALLENGE_REPO_TAG).d("Error updating challenge: $e") }
    }

    //DELETE
    /*
    fun deleteChallenge(challenge: Challenge) {
        val challengeRef = challengeCollection.document(challenge.challengeId)

        challengeRef.delete()
            .addOnSuccessListener { Timber.tag(CHALLENGE_REPO_TAG).d("Challenge successfully deleted!") }
            .addOnFailureListener { e -> Timber.tag(CHALLENGE_REPO_TAG).d("Error deleting Challenge: $e") }
    }

     */


    /**
     * ################################################
     *              User Challenges
     * ################################################
     */

    // GET-ALL
    //TODO: is there a better way than infinity loop with delay?
    fun getPublicUserChallenges(): LiveData<List<UserChallenge>> = liveData(Dispatchers.IO) {
        // `while(true)` is fine because the `delay` below will cooperate in
        // cancellation if LiveData is not actively observed anymore
        while (true) {
            val allChallenges = fetchPublicChallengesFromFirebase()
            allChallenges?.let { emit(it) }
            delay(2000)     // refresh for new data every 2 seconds
        }
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

    /*
    fun getAllChallengesForUser(userId: String): LiveData<List<BaseChallenge>> = liveData {
        while (true) {
            val userChallengesForUser = fetchUserChallengesForUser(userId)
            val systemChallengesForUser = fetchSystemChallengesForUser(userId)
            val allChallenges = ArrayList<BaseChallenge>()
            userChallengesForUser?.let { allChallenges.addAll(it) }
            systemChallengesForUser?.let { allChallenges.addAll(it) }
            emit(allChallenges)
            delay(1000)     // refresh for new data every second
        }
    }

    private suspend fun fetchUserChallengesForUser(userId: String): List<UserChallenge>? {
        return try {
            val challengeList: MutableList<UserChallenge> = ArrayList()
            val docSnapshots = userChallengeCollection
                .whereEqualTo("creatorId", userId)   // get the user challenges for this user
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

    private suspend fun fetchSystemChallengesForUser(userId: String): List<Challenge>? {
        return try {
            val challengeList: MutableList<Challenge> = ArrayList()
            val docSnapshots = challengeCollection
                //TODO.whereEqualTo("creatorId", userId)   // get the system challenges for this user
                .orderBy("acceptedDate", Query.Direction.DESCENDING)
                .get().await().documents

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
    }

     */

    //GET
    fun getUserChallenge(id: String): UserChallenge? = runBlocking(Dispatchers.Main) {
        val deferredResult = async(Dispatchers.IO) {
            val challengeSnapshot = userChallengeCollection.document(id).get().await()
            return@async challengeSnapshot.toObject(UserChallenge::class.java)
        }
        deferredResult.await()
    }

    //CREATE
    fun saveNewUserChallenge(userChallenge: UserChallenge): String {
        val challengeReference = userChallengeCollection.document(userChallenge.challengeId)

        challengeReference.set(userChallenge).addOnSuccessListener {
            Toast.makeText(App.instance, "User Challenge saved successfully!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Toast.makeText(App.instance, "Failed to save new user challenge: $e", Toast.LENGTH_SHORT).show()
        }

        return challengeReference.id
    }

    //CREATE-MULTIPLE
    fun saveMultipleUserChallenges(userChallengeList: List<UserChallenge>) {
        //use a batched write to insert all at the same time to prevent possible inconsistencies!
        val batchWrite = FirebaseFirestore.getInstance().batch()

        for (challenge in userChallengeList) {
            // create a new reference for this challenge
            val docRef = userChallengeCollection.document(challenge.challengeId)
            // and add it to the WriteBatch
            batchWrite.set(docRef, challenge)
        }

        // commit the batch (i.e. write all to the db)
        batchWrite.commit().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.tag(CHALLENGE_REPO_TAG).d("Writing user Challenge Batch was successful!")
            } else {
                Timber.tag(CHALLENGE_REPO_TAG).d("Failed to write user challenge batch!")
            }
        }
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
    fun updatePublicStatus(challengeId: String, publicStatus: Boolean) {
        val challengeRef = userChallengeCollection.document(challengeId)

        challengeRef
            .update("isPublic", publicStatus)
            .addOnSuccessListener { Timber.tag(CHALLENGE_REPO_TAG).d("User Challenge successfully published!") }
            .addOnFailureListener { e -> Timber.tag(CHALLENGE_REPO_TAG).d("Error publishing user challenge: $e") }
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