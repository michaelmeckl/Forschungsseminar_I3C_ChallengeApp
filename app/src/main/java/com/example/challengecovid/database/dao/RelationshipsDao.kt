package com.example.challengecovid.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.challengecovid.model.ChallengesForUser
import com.example.challengecovid.model.ChallengesInCategory
import com.example.challengecovid.model.UserWithChallenges
import com.example.challengecovid.model.UsersForChallenge

@Dao
interface RelationshipsDao {

    /*

    // This needs to be a transaction as there are 2 separate queries that are performed under the hood.
    // @Transaction performs them both atomically to prevent any possible inconsistencies.
    @Transaction
    @Query("SELECT * FROM user_table")
    suspend fun getUsersWithCreatedChallenges(): List<UserWithChallenges>

    @Transaction
    @Query("SELECT * FROM challenge_category_table")
    suspend fun getChallengesForCategory(): List<ChallengesInCategory>

    @Transaction
    @Query("SELECT * FROM user_table")
    suspend fun getChallengesForUser(): List<ChallengesForUser>

    // the other way round:
    @Transaction
    @Query("SELECT * FROM challenge_table")
    suspend fun getUsersForChallenge(): List<UsersForChallenge>

     */

}