package com.example.challengecovid.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.challengecovid.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

class UserRepository {

    // reference to the root user collection in firestore
    private val userCollection = FirebaseFirestore.getInstance().collection("users")

    private lateinit var snapshotListener: ListenerRegistration


    companion object {
        const val USER_REPO_TAG = "USER_REPOSITORY"
    }


    // GET-ALL
    /*
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
    }*/

    fun updateUserLevel(userLevel: Int, id: String){
        val userReF = userCollection.document(id)

        userReF
            .update("level",userLevel)
            .addOnSuccessListener { Timber.tag(USER_REPO_TAG).d("Userlevel successfully updated!") }
            .addOnFailureListener { e -> Timber.tag(USER_REPO_TAG).d("Error updating Userlevel: $e") }
    }

    fun updateUserPoints(userPoints: Int, id: String){
        val userReF = userCollection.document(id)

        userReF
            .update("points",userPoints)
            .addOnSuccessListener { Timber.tag(USER_REPO_TAG).d("Userpoints successfully updated!") }
            .addOnFailureListener { e -> Timber.tag(USER_REPO_TAG).d("Error updating Userpoints: $e") }
    }


    fun getAllChallengesForUser(userId: String): LiveData<List<BaseChallenge>> = liveData(Dispatchers.IO) {
        Timber.tag("FIREBASE userId in repo").d(userId)
        while (true) {
            val challengesForUser = fetchChallengesForUser(userId)
            challengesForUser?.let { emit(it) }
            delay(1000)     // refresh for new data every second
        }
    }

    /**
     * Returns all active challenges for a user specified with the userId.
     * @Hidden specifies if hidden challenges (i.e. system challenges that have been removed) should be returned too
     */
    suspend fun getAllChallengesForUserOnce(userId: String, hidden: Boolean = false): List<BaseChallenge>? {
        return if (hidden) {
            getActiveAndHiddenChallengesForUser(userId)
        } else {
            fetchChallengesForUser(userId)
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
                        val hiddenState = snapshot.get("hidden")
                        if (hiddenState == true) {
                            // don't add this element when it is set to hidden
                            continue
                        }
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

    suspend fun getActiveAndHiddenChallengesForUser(userId: String): List<BaseChallenge>? {
        return try {
            val activeAndHiddenChallenges = mutableListOf<BaseChallenge>()
            val docSnapshots = userCollection.document(userId)
                .collection("activeChallenges")
                .get().await().documents

            if (docSnapshots.isNotEmpty()) {
                for (snapshot in docSnapshots)
                    if (snapshot.get("type") == ChallengeType.USER_CHALLENGE) {
                        snapshot.toObject(UserChallenge::class.java)?.let {
                            activeAndHiddenChallenges.add(it)
                        }
                    } else {
                        snapshot.toObject(Challenge::class.java)?.let {
                            activeAndHiddenChallenges.add(it)
                        }
                    }
            }

            activeAndHiddenChallenges
        } catch (e: Exception) {
            Timber.tag(USER_REPO_TAG).d(e)
            null
        }
    }

    suspend fun getChallengeParticipants(challengeId: String): List<User>? {
        return try {
            val participants: MutableList<User> = mutableListOf()
            val allUsers = userCollection.get().await().toObjects<User>()

            for (user in allUsers) {
                val activeChallenges = userCollection.document(user.userId)
                    .collection("activeChallenges")
                    .get().await()
                    .toObjects<UserChallenge>()

                activeChallenges.forEach { challenge ->
                    if (challenge.challengeId == challengeId) {
                        //the challenge is in this users' active challenges so add him to the participants of this challenge
                        participants.add(user)
                        return@forEach
                    }
                }
            }

            participants
        } catch (e: Exception) {
            Timber.tag(USER_REPO_TAG).d(e)
            null
        }
    }

    //GET
    suspend fun getUserOnce(id: String): User? {
        val userSnapshot = userCollection.document(id).get().await()
        return userSnapshot.toObject(User::class.java)
    }

    //GET
    fun getUser(id: String): MutableLiveData<User> = liveData(Dispatchers.IO) {
        while (true) {
            val userSnapshot = userCollection.document(id).get().await()
            userSnapshot.toObject(User::class.java)?.let { emit(it) }
            delay(1000)
        }
    } as MutableLiveData<User>

    //CREATE
    suspend fun saveNewUser(user: User): String = withContext(Dispatchers.IO) {
        val userReference = userCollection.document(user.userId)

        userReference.set(user).addOnSuccessListener {
            Timber.d("User saved successfully!")
        }.addOnFailureListener { e ->
            Timber.d("Failed to save new user: $e")
        }

        userReference.id
    }

    fun addActiveChallenge(challenge: BaseChallenge, userId: String) {
        userCollection.document(userId)
            .collection("activeChallenges")
            .document(challenge.challengeId)
            .set(challenge)
            .addOnSuccessListener {
                Timber.d("Challenge erfolgreich angenommen!")
            }.addOnFailureListener { e ->
                Timber.d("Fehler beim Annehmen der Challenge: $e")
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


    fun updateActiveChallenge(challenge: BaseChallenge, userId: String) {
        val ref = userCollection
            .document(userId)
            .collection("activeChallenges")
            .document(challenge.challengeId)

        ref.set(challenge, SetOptions.merge())
            .addOnSuccessListener {
                Timber.tag(USER_REPO_TAG).d("Successfully updated active challenge!")
            }
            .addOnFailureListener { e -> Timber.tag(USER_REPO_TAG).d("Error updating active challenge: $e") }
    }

    fun updateUserName(name: String, id: String) {
        val userRef = userCollection.document(id)

        userRef
            .update("username", name)
            .addOnSuccessListener { Timber.tag(USER_REPO_TAG).d("Username successfully updated!") }
            .addOnFailureListener { e -> Timber.tag(USER_REPO_TAG).d("Error updating username: $e") }
    }

    fun upDateUserIcon(userIcon: String, id: String) {
        val userReF = userCollection.document(id)

        userReF
            .update("userIcon", userIcon)
            .addOnSuccessListener { Timber.tag(USER_REPO_TAG).d("Usericon successfully updated!") }
            .addOnFailureListener { e -> Timber.tag(USER_REPO_TAG).d("Error updating username: $e") }
    }

    //DELETE
    fun deleteUser(user: User) {
        val userRef = userCollection.document(user.userId)

        userRef.delete()
            .addOnSuccessListener { Timber.tag(USER_REPO_TAG).d("User successfully deleted!") }
            .addOnFailureListener { e -> Timber.tag(USER_REPO_TAG).d("Error deleting user: $e") }
    }

    fun removeActiveChallenge(challengeId: String, userId: String) {
        val challengeRef = userCollection.document(userId)
            .collection("activeChallenges")
            .document(challengeId)

        //userRef.update("activeChallenges", FieldValue.arrayRemove(challenge))
        challengeRef.delete()
            .addOnSuccessListener { Timber.tag(USER_REPO_TAG).d("Challenge successfully deleted from array!") }
            .addOnFailureListener { e -> Timber.tag(USER_REPO_TAG).d("Error deleting challenge from array: $e") }
    }

    fun hideActiveChallenge(challenge: Challenge, userId: String) {
        val challengeRef = userCollection.document(userId)
            .collection("activeChallenges")
            .document(challenge.challengeId)

        challengeRef.update("hidden", true)
            .addOnSuccessListener { Timber.tag(USER_REPO_TAG).d("Challenge successfully hidden!") }
            .addOnFailureListener { e -> Timber.tag(USER_REPO_TAG).d("Error hiding challenge: $e") }
    }

    /*
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

                //Oder so:
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