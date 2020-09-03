package com.example.challengecovid.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.ChallengeCategory
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber


class CategoryRepository {

    // reference to the root category collection in firestore
    private val categoryCollection = FirebaseFirestore.getInstance().collection("categories")


    companion object {
        const val CATEGORY_REPO_TAG = "CATEGORY_REPOSITORY"
    }


    // GET-ALL
    fun getAllCategories(): LiveData<List<ChallengeCategory>> = liveData(Dispatchers.IO) {
        /*while (true) {
            val allCategories = fetchCategoriesFromFirebase()
            allCategories?.let { emit(it) }
            delay(5_000)     // refresh for new data every 5 seconds
        }*/
        val allCategories = fetchCategoriesFromFirebase()
        allCategories?.let { emit(it) }
    }

    private suspend fun fetchCategoriesFromFirebase(): List<ChallengeCategory>? {
        return try {
            val categoryList: MutableList<ChallengeCategory> = ArrayList()
            val docSnapshots = categoryCollection.get().await().documents

            if (docSnapshots.isNotEmpty()) {
                for (snapshot in docSnapshots)
                    snapshot.toObject(ChallengeCategory::class.java)?.let {
                        categoryList.add(it)
                    }
            }

            categoryList
        } catch (e: Exception) {
            Timber.tag(CATEGORY_REPO_TAG).d(e)
            null
        }
    }

    suspend fun fetchChallengesForCategory(categoryId: String): List<Challenge>? {
        return try {
            val categorySnapshot = categoryCollection.document(categoryId).get().await()

            val category = categorySnapshot.toObject<ChallengeCategory>()
            category?.containedChallenges
        } catch (e: Exception) {
            Timber.tag(CATEGORY_REPO_TAG).d(e)
            null
        }
    }

    //GET
    suspend fun getCategory(id: String): ChallengeCategory? {
        val categorySnapshot = categoryCollection.document(id).get().await()
        return categorySnapshot.toObject(ChallengeCategory::class.java)
    }

    //CREATE-MULTIPLE
    fun saveMultipleCategories(categoryList: List<ChallengeCategory>) {
        //use a batched write to insert all at the same time to prevent possible inconsistencies!
        val batchWrite = FirebaseFirestore.getInstance().batch()

        for (category in categoryList) {
            // create a new reference for this category
            val docRef = categoryCollection.document(category.categoryId)
            // and add it to the WriteBatch
            batchWrite.set(docRef, category, SetOptions.merge())
        }

        // commit the batch (i.e. write all to the db)
        batchWrite.commit().addOnSuccessListener {
            Timber.tag(CATEGORY_REPO_TAG).d("category Batch inserted successfully!")
        }.addOnFailureListener { e ->
            Timber.tag(CATEGORY_REPO_TAG).d("Failed to insert category batch: $e!")
        }
    }

    //UPDATE
    fun updateCategory(category: ChallengeCategory) {
        val oldCategoryRef = categoryCollection.document(category.categoryId)

        oldCategoryRef
            .set(category)
            .addOnSuccessListener { Timber.tag(CATEGORY_REPO_TAG).d("category successfully updated!") }
            .addOnFailureListener { e -> Timber.tag(CATEGORY_REPO_TAG).d("Error updating category: $e") }
    }


    // TODO: gilt das jetzt für alle Nutzer ?? Vermutlich ...
    //  TESTEN wenn alle diesen Branch haben!

    //TODO: wird das eigentlich wieder überschrieben wenn sich ein neuen Nutzer das erste mal anmeldet (da ja setoptions.merge) ???
    // -> ja deshalb nur in user challenges

    //TODO: abgesehen davon ist die challenge jetzt bei active Challenges des nutzers aber mit anderem accepted eintrag lol
    suspend fun changeChallengeActiveStatus(categoryId: String, activeChallenge: Challenge, status: Boolean) =
        withContext(Dispatchers.IO) {
            try {
                val categorySnapshot = categoryCollection.document(categoryId).get().await()

                val category = categorySnapshot.toObject<ChallengeCategory>()
                val challenges = category?.containedChallenges ?: return@withContext

                for (challenge in challenges) {
                    if (challenge == activeChallenge) {
                        //TODO: gibts dafür ne schönere möglichkeit? (andererseits wird so auch gleich mit sortiert ...)
                        categoryCollection.document(categoryId)
                            .update("containedChallenges", FieldValue.arrayRemove(challenge))
                        challenge.accepted = status
                        categoryCollection.document(categoryId)
                            .update("containedChallenges", FieldValue.arrayUnion(challenge))
                    }
                }

            } catch (e: Exception) {
                Timber.tag(CATEGORY_REPO_TAG).d(e)
            }
        }


    /**
     * ################################################
     *                  for Room
     * ################################################
     */
    /*
    private val categoryDao = database.categoryDao()

    suspend fun insertChallengeCategories(categories: List<ChallengeCategory>) {
        categoryDao.insertAll(categories)
    }

    suspend fun updateChallengeCategory(category: ChallengeCategory) {
        categoryDao.update(category)
    }

    fun getChallengeCategory(id: String): LiveData<ChallengeCategory> {
        return categoryDao.getCategory(id)
    }

    fun getAllChallengeCategories(): LiveData<List<ChallengeCategory>> {
        return categoryDao.getAllCategories()
    }
    */

}