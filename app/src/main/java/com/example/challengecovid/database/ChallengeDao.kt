package com.example.challengecovid.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.challengecovid.model.Challenge

@Dao
interface ChallengeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE) // when a challenge already exists, ignore the new one
    suspend fun insert(challenge: Challenge)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(challenges: List<Challenge>)

    @Update
    suspend fun update(challenge: Challenge)

    @Query("DELETE FROM challenge_table WHERE challengeId = :id")
    suspend fun delete(id: String)

    /**
     * Deletes all values from the table. This does not delete the table, only its contents.
     */
    @Query("DELETE FROM challenge_table")
    suspend fun clear()

    /**
     * Selects and returns the row that matches the supplied id / key.
     */
    @Query("SELECT * from challenge_table WHERE challengeId = :key")
    fun get(key: String): LiveData<Challenge>   // no suspend for LiveData!!

    /**
     * Selects and returns all rows in the table, sorted by id in descending order.
     */
    @Query("SELECT * FROM challenge_table ORDER BY challengeId DESC")
    fun getAllChallenges(): LiveData<List<Challenge>>
}