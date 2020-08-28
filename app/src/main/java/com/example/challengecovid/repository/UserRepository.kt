package com.example.challengecovid.repository

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.challengecovid.App
import com.example.challengecovid.model.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class UserRepository {

    // reference to the root user collection in firestore
    private val userCollection = FirebaseFirestore.getInstance().collection("users")

    private lateinit var snapshotListener: ListenerRegistration


    companion object {
        const val USER_REPO_TAG = "USER_REPOSITORY"
    }


    // GET-ALL
    fun getAllUsers(): LiveData<List<User>> = liveData(Dispatchers.IO) {
        while (true) {
            val allUsers = fetchUsersFromFirebase()
            allUsers?.let { emit(it) }
            delay(3000)     // refresh for new data every 3 seconds
        }
    }

    private suspend fun fetchUsersFromFirebase(): List<User>? {
        return try {
            val userList = mutableListOf<User>()
            val docSnapshots = userCollection.get().await().documents

            if (docSnapshots.isNotEmpty()) {
                for (snapshot in docSnapshots)
                    snapshot.toObject(User::class.java)?.let {
                        userList.add(it)
                    }
            }

            userList
        } catch (e: Exception) {
            Timber.tag(USER_REPO_TAG).d(e)
            null
        }
    }

    fun getAllChallengesForUser(userId: String): LiveData<List<BaseChallenge>> = liveData(Dispatchers.Main) {
        Timber.tag("FIREBASE userId in repo").d(userId)
        while (true) {
            val challengesForUser = fetchChallengesForUser(userId)
            challengesForUser?.let { emit(it) }
            delay(1000)     // refresh for new data every second
        }
    }

    private suspend fun fetchChallengesForUser(userId: String): List<BaseChallenge>? {
        return try {
            val challengeList = mutableListOf<BaseChallenge>()
            val docSnapshots = userCollection.document(userId)
                .collection("activeChallenges")
                .get().await().documents

            if (docSnapshots.isNotEmpty()) {
                for (snapshot in docSnapshots)
                    if (snapshot.get("type") == ChallengeType.USER_CHALLENGE) {
                        snapshot.toObject(UserChallenge::class.java)?.let {
                            challengeList.add(it)
                        }
                    } else {
                        snapshot.toObject(Challenge::class.java)?.let {
                            challengeList.add(it)
                        }
                    }
            }

            challengeList
        } catch (e: Exception) {
            Timber.tag(USER_REPO_TAG).d(e)
            null
        }
    }

    //GET
    suspend fun getUser(id: String): User? {
        val userSnapshot = userCollection.document(id).get().await()
        return userSnapshot.toObject(User::class.java)
    }

    //CREATE
    fun saveNewUser(user: User): String {
        val userReference = userCollection.document(user.userId)

        userReference.set(user).addOnSuccessListener {
            Toast.makeText(App.instance, "User saved successfully!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Toast.makeText(App.instance, "Failed to save new user: $e", Toast.LENGTH_SHORT).show()
        }

        return userReference.id
    }

    fun addActiveChallenge(challenge: BaseChallenge, userId: String) {
        userCollection.document(userId)
            .collection("activeChallenges")
            .document(challenge.challengeId)
            .set(challenge)
            .addOnSuccessListener {
                Toast.makeText(App.instance, "Added to active challenges!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { e ->
                Toast.makeText(App.instance, "Failed to add to active challenges: $e", Toast.LENGTH_SHORT).show()
            }
    }

    //CREATE-MULTIPLE
    fun saveMultipleUsers(userList: List<User>) {
        //use a batched write to insert all at the same time to prevent possible inconsistencies!
        val batchWrite = FirebaseFirestore.getInstance().batch()

        for (user in userList) {
            // create a new reference for this user
            val docRef = userCollection.document(user.userId)
            // and add it to the WriteBatch
            batchWrite.set(docRef, user)
        }

        // commit the batch (i.e. write all to the db)
        batchWrite.commit().addOnSuccessListener {
            Timber.tag(USER_REPO_TAG).d("User Batch inserted successfully!")
        }.addOnFailureListener { e ->
            Timber.tag(USER_REPO_TAG).d("Failed to insert user batch: $e!")
        }
    }

    //UPDATE
    fun updateUser(user: User) {
        val oldUserRef = userCollection.document(user.userId)

        oldUserRef
            .set(user)
            .addOnSuccessListener { Timber.tag(USER_REPO_TAG).d("User successfully updated!") }
            .addOnFailureListener { e -> Timber.tag(USER_REPO_TAG).d("Error updating user: $e") }
    }

    //DELETE
    fun deleteUser(user: User) {
        val userRef = userCollection.document(user.userId)

        userRef.delete()
            .addOnSuccessListener { Timber.tag(USER_REPO_TAG).d("User successfully deleted!") }
            .addOnFailureListener { e -> Timber.tag(USER_REPO_TAG).d("Error deleting user: $e") }
    }

    fun removeActiveChallenge(challenge: BaseChallenge, userId: String) {
        val challengeRef = userCollection.document(userId)
            .collection("activeChallenges")
            .document(challenge.challengeId)

        //userRef.update("activeChallenges", FieldValue.arrayRemove(challenge))
        challengeRef.delete()
            .addOnSuccessListener { Timber.tag(USER_REPO_TAG).d("Challenge successfully deleted from array!") }
            .addOnFailureListener { e -> Timber.tag(USER_REPO_TAG).d("Error deleting challenge from array: $e") }
    }

    /*
    //TODO:
    fun getUsersWithMinLevel(level: Int) {
        db.collection("cities")
            .whereEqualTo("capital", true)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }
    */

    /*
    //TODO:
    fun getBestUsers() {
        userCollection
            .orderBy("level", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        Timber.tag("FIREBASE_ALL_USERS").d("${document.id} => ${document.data}")
                    }
                } else {
                    Timber.tag("FIREBASE_ALL_USERS").d(" Error getting documents: ${task.exception}")
                }
            }
    }
     */

    //listen for realtime updates
    //TODO:
    fun listenOnUpdates() {
        val listener = userCollection.whereEqualTo("state", "CA")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Timber.tag("FIREBASE_ALL_USERS").d("Listen failed: $e")
                    return@addSnapshotListener
                }

                val source = if (snapshots != null && snapshots.metadata.hasPendingWrites())
                    "Local"
                else
                    "Server"


                val cities = ArrayList<String>()
                for (doc in snapshots!!) {
                    doc.getString("name")?.let {
                        cities.add(it)
                    }
                }

                //TODO Oder so:
                /*
                for (dc in snapshots.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> Log.d("FIREBASE_LISTENER", "New city: ${dc.document.data}")
                        DocumentChange.Type.MODIFIED -> Log.d("FIREBASE_LISTENER", "Modified city: ${dc.document.data}")
                        DocumentChange.Type.REMOVED -> Log.d("FIREBASE_LISTENER", "Removed city: ${dc.document.data}")
                    }
                }
                */
            }
    }


    //call this in the onStop lifecycle methods to save bandwith!
    fun detachListener() {
        // Stop listening to changes
        snapshotListener.remove()
    }


    /**
     * ################################################
     *                  for Room
     * ################################################
     */
    /*
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
    */
}