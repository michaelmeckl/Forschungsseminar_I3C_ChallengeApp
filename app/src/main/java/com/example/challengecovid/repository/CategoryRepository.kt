package com.example.challengecovid.repository

import androidx.lifecycle.LiveData
import com.example.challengecovid.model.ChallengeCategory

class CategoryRepository {

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