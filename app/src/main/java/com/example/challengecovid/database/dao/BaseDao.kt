package com.example.challengecovid.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.challengecovid.model.Challenge

/**
 * Generic DAO interface to reduce some boilerplate code.
 * See https://gist.github.com/florina-muntenescu/1c78858f286d196d545c038a71a3e864
 */
interface BaseDao<T> {

    /**
     * Insert an object in the database.
     * @param obj the object to be inserted.
     */
    @Insert
    suspend fun insert(obj: T)

    /**
     * Insert an array of objects in the database.
     * @param obj the objects to be inserted.
     */
    @Insert
    suspend fun insertAll(obj: List<T>)

    /**
     * Update an object from the database.
     * @param obj the object to be updated
     */
    @Update (onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(obj: T)

    /**
     * Delete an object from the database
     * @param obj the object to be deleted
     */
    @Delete
    suspend fun delete(obj: T)
}