package com.example.challengecovid.database.dao

import androidx.room.Dao
import com.example.challengecovid.model.ChallengeCategory

@Dao
interface CategoryDao : BaseDao<ChallengeCategory> {

    /*
    @Query("SELECT * from challenge_category_table WHERE categoryId = :key")
    fun getCategory(key: String): LiveData<ChallengeCategory>

    @Query("SELECT * FROM challenge_category_table")
    fun getAllCategories(): LiveData<List<ChallengeCategory>>

     */

}