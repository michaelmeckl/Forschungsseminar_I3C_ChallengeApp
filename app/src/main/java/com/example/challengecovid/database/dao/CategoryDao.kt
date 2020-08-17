package com.example.challengecovid.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.challengecovid.model.ChallengeCategory

@Dao
interface CategoryDao : BaseDao<ChallengeCategory> {

    @Query("SELECT * from challenge_category_table WHERE categoryId = :key")
    suspend fun getCategory(key: String): ChallengeCategory

    @Query("SELECT * FROM challenge_category_table")
    suspend fun getAllCategories(): List<ChallengeCategory>    // no live data necessary because they are never changed

}