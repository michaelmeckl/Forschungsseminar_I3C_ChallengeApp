package com.example.challengecovid.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.challengecovid.model.Challenge

/**
 * A database that stores Challenges and a global method to get access to the database.
 */
@Database(entities = [Challenge::class], version = 1, exportSchema = false)
abstract class ChallengeDatabase : RoomDatabase() {

    // Connects the database to the DAO.
    abstract val challengeDao: ChallengeDao

    // use a companion object to get static access
    companion object {
        /**
         * INSTANCE will keep a reference to any database returned via getInstance.
         * This will help us avoid repeatedly initializing the database, which is expensive.
         *
         *  The value of a volatile variable will never be cached, and all writes and
         *  reads will be done to and from the main memory. It means that changes made by one
         *  thread to shared data are visible to other threads.
         */
        @Volatile
        private var INSTANCE: ChallengeDatabase? = null

        /**
         * Helper function to get the database.
         *
         * If a database has already been retrieved, the previous database will be returned. Otherwise, create a new database.
         * This function is threadsafe, and callers should cache the result for multiple database calls to avoid overhead.
         *
         * This is an example of a simple Singleton pattern that takes another Singleton as an argument in Kotlin.
         *
         * @param context The application context Singleton, used to get access to the filesystem.
         * @return ChallengeDatabase A singleton instance of the Database
         */
        fun getInstance(context: Context): ChallengeDatabase {
            // Multiple threads can ask for the database at the same time, ensure we only initialize
            // it once by using synchronized. Only one thread may enter a synchronized block at a time.
            synchronized(this) {

                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var instance = INSTANCE

                // If instance is `null` make a new database instance.
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext, ChallengeDatabase::class.java,
                        "challenge_database")
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        // see https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
                        .fallbackToDestructiveMigration()
                        .build()

                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }

                // Return instance; smart cast to be non-null.
                return instance
            }
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}