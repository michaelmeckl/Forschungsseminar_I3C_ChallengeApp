package com.example.challengecovid.repository

import android.widget.Toast
import com.example.challengecovid.App
import com.example.challengecovid.database.ChallengeAppDatabase
import com.example.challengecovid.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class UserRepository {

    // reference to the firestore db
    private val firestore = FirebaseFirestore.getInstance()

    fun saveNewUser(newUser: User): String {
        val userReference = firestore.collection("users").document()
        userReference.set(newUser).addOnSuccessListener {
            Toast.makeText(App.instance, "User inserted successfully!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Toast.makeText(App.instance, "Failed to insert new user: $e", Toast.LENGTH_SHORT).show()
        }

        return userReference.id
    }

    fun addUser(user: User) {
        firestore.collection("users").add(user).addOnSuccessListener { documentReference ->
            Timber.tag("FIREBASE_INSERT_USER").d(
                "DocumentSnapshot set with ID: ${documentReference.id} at path ${documentReference.path}; ${documentReference.get()}"
            )
        }
            .addOnFailureListener { e ->
                Timber.tag("FIREBASE_INSERT_USER").d(
                    "Error adding document $e"
                )
            }

        fun fetchAllUsers() {
            firestore.collection("users")
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


        /*
        /**
         * ################################################
         *                  for Room
         * ################################################
         */
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