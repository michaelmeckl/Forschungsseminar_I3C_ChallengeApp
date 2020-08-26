package com.example.challengecovid.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.challengecovid.model.UserChallenge

@Dao
interface UserChallengeDao : BaseDao<UserChallenge> {

    // deletes all values from the table. This does not delete the table, only its contents.
    @Query("DELETE FROM user_challenge_table")
    suspend fun clear()

    @Query("SELECT * from user_challenge_table WHERE challengeId = :key")
    fun getUserChallenge(key: String): LiveData<UserChallenge>

    @Query("SELECT * FROM user_challenge_table ORDER BY createdAt DESC")
    fun getAllUserChallenges(): LiveData<List<UserChallenge>>

    @Query("UPDATE user_challenge_table SET completed = 1 WHERE challengeId = :key")
    fun setChallengeCompleted(key: String)

}