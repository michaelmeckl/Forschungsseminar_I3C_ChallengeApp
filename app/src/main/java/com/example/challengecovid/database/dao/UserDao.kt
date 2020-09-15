package com.example.challengecovid.database.dao

import androidx.room.Dao
import com.example.challengecovid.model.User

@Dao
interface UserDao : BaseDao<User> {

    /*
    @Query("SELECT * from user_table WHERE userId = :key")
    fun getUser(key: String): LiveData<User>

    // get the 10 best users ordered by level
    @Query("SELECT * FROM user_table ORDER BY level DESC LIMIT 10")
    fun getBestUsers(): LiveData<List<User>>

    @Query("SELECT * FROM user_table")
    fun getAllUsers(): LiveData<List<User>>

     */

}