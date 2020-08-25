package com.example.challengecovid.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.challengecovid.model.User

@Dao
interface UserDao : BaseDao<User> {

    @Query("SELECT * from user_table WHERE userId = :key")
    fun getUser(key: String): LiveData<User>

    // get the 10 best users ordered by level
    @Query("SELECT * FROM user_table ORDER BY level DESC LIMIT 10")
    fun getBestUsers(): LiveData<List<User>>

    @Query("SELECT * FROM user_table")
    fun getAllUsers(): LiveData<List<User>>

    @Query("UPDATE user_table SET username = :username WHERE userId = :key")
    fun updateUserName(key: String, username: String):  LiveData<User>

    @Query("UPDATE user_table SET userIcon = :userIcon WHERE userId = :key")
    fun updateUserIcon(key: String, userIcon: String):  LiveData<User>

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Insert
    suspend fun insertUser(user: User)



}