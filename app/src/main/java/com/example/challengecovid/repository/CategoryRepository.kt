package com.example.challengecovid.repository

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.challengecovid.App
import com.example.challengecovid.model.ChallengeCategory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import timber.log.Timber


class CategoryRepository {

    // reference to the root category collection in firestore
    private val categoryCollection = FirebaseFirestore.getInstance().collection("categories")


    companion object {
        const val CATEGORY_REPO_TAG = "CATEGORY_REPOSITORY"
    }


    // GET-ALL
    fun getAllCategories(): LiveData<List<ChallengeCategory>> = liveData(Dispatchers.Main) {
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

    //GET
    fun getCategory(id: String): ChallengeCategory? = runBlocking(Dispatchers.Main) {
        val deferredResult = async(Dispatchers.IO) {
            val categorySnapshot = categoryCollection.document(id).get().await()
            return@async categorySnapshot.toObject(ChallengeCategory::class.java)
        }
        deferredResult.await()
    }

    //CREATE
    fun saveNewCategory(category: ChallengeCategory): String {
        val categoryReference = categoryCollection.document(category.categoryId)

        categoryReference.set(category).addOnSuccessListener {
            Toast.makeText(App.instance, "Category saved successfully!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Toast.makeText(App.instance, "Failed to save new category: $e", Toast.LENGTH_SHORT).show()
        }

        return categoryReference.id
    }

    //CREATE-MULTIPLE
    fun saveMultipleCategories(categoryList: List<ChallengeCategory>) {
        //use a batched write to insert all at the same time to prevent possible inconsistencies!
        val batchWrite = FirebaseFirestore.getInstance().batch()

        for (category in categoryList) {
            // create a new reference for this category
            val docRef = categoryCollection.document(category.categoryId)
            // and add it to the WriteBatch
            batchWrite.set(docRef, category)
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

    //DELETE
    fun deleteCategory(category: ChallengeCategory) {
        val categoryRef = categoryCollection.document(category.categoryId)

        categoryRef.delete()
            .addOnSuccessListener { Timber.tag(CATEGORY_REPO_TAG).d("category successfully deleted!") }
            .addOnFailureListener { e -> Timber.tag(CATEGORY_REPO_TAG).d("Error deleting category: $e") }
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