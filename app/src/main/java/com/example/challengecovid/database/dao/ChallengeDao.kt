package com.example.challengecovid.database.dao

import androidx.room.Dao
import com.example.challengecovid.model.Challenge

@Dao
interface ChallengeDao : BaseDao<Challenge> {

    /*
    @Query("SELECT * from challenge_table WHERE challengeId = :key")
    fun getChallenge(key: String): LiveData<Challenge>   // no suspend for LiveData!!

    /**
     * Selects and returns all matching rows in the table, sorted by timestamp in descending order.
     */
    @Query("SELECT * FROM challenge_table ORDER BY createdAt DESC")
    fun getAllChallenges(): LiveData<List<Challenge>>

     */

}