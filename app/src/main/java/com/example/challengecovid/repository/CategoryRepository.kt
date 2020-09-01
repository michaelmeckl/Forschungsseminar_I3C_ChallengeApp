package com.example.challengecovid.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.ChallengeCategory
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