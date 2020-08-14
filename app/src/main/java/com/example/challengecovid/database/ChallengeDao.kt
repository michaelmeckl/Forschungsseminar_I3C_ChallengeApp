package com.example.challengecovid.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.challengecovid.model.Challenge


/**
 * Defines methods for using the Challenge class with Room.
 */
@Dao
interface ChallengeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE) // when a challenge already exists, ignore the new one
    fun insert(challenge: Challenge)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(challenges: List<Challenge>)

    /**
     * When updating a row with a value already set in a column, replaces the old value with the new one.
     */
    @Update
    fun update(challenge: Challenge)

    /**
     * Deletes all values from the table. This does not delete the table, only its contents.
     */
    @Query("DELETE FROM challenge_table")
    fun clear()

    /**
     * Selects and returns the row that matches the supplied id / key.
     */
    @Query("SELECT * from challenge_table WHERE challengeId = :key")
    fun get(key: Int): Challenge

    /**
     * Selects and returns all rows in the table, sorted by id in descending order.
     */
    @Query("SELECT * FROM challenge_table ORDER BY challengeId DESC")
    fun getAllChallenges(): LiveData<List<Challenge>>
}