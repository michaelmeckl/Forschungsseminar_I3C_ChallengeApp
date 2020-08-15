package com.example.challengecovid.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.challengecovid.R
import com.example.challengecovid.model.Challenge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * A database that stores Challenges and a global method to get access to the database.
 */
@Database(entities = [Challenge::class], version = 2, exportSchema = true)
abstract class ChallengeDatabase : RoomDatabase() {

    // Connects the database to the DAO.
    abstract fun challengeDao(): ChallengeDao

    // use a companion object to get static access
    companion object {

        //TODO: use this instead?
        /*
        @Volatile private var INSTANCE: DataDatabase? = null

        fun getInstance(context: Context): DataDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }
        */


        /**
         * INSTANCE will keep a reference to any database returned via getInstance.
         * This will help us avoid repeatedly initializing the database, which is expensive.
         *
         *  The value of a volatile variable will never be cached, and all writes and
         *  reads will be done to and from the main memory. It means that changes made by one
         *  thread to shared data are visible to other threads.
         */
        @Volatile private var INSTANCE: ChallengeDatabase? = null

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
        fun getInstance(context: Context /*, scope: CoroutineScope*/): ChallengeDatabase {
            // Multiple threads can ask for the database at the same time, ensure we only initialize
            // it once by using synchronized. Only one thread may enter a synchronized block at a time.
            synchronized(this) {

                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var instance = INSTANCE

                if (instance == null) {
                    // If instance is `null` make a new database instance and assign INSTANCE to the newly created database.
                    instance = buildDatabase(context).also { INSTANCE = it }
                }

                // Return instance; smart cast to be non-null.
                return instance
            }
        }

        private fun buildDatabase(context: Context): ChallengeDatabase {
            return Room.databaseBuilder(context.applicationContext, ChallengeDatabase::class.java, "challenge_database")
                // Wipes and rebuilds instead of migrating.
                // see https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
                .fallbackToDestructiveMigration()
                //.addCallback(ChallengeDatabaseCallback(scope, context))    //TODO
                .build()
        }

        fun destroyDatabase(){
            INSTANCE = null
        }
    }


    private class ChallengeDatabaseCallback(
        private val scope: CoroutineScope,
        private val context: Context
    ) : RoomDatabase.Callback() {

        // Populate the database on creation with initial challenge data
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    prepopulateDatabase(database.challengeDao(), context)
                }
            }
        }

        private suspend fun prepopulateDatabase(challengeDao: ChallengeDao, context: Context) {
            // Delete all content here.
            challengeDao.clear()    //TODO: be careful to not delete anything important like user generated content!


            // TODO: Add some challenges at the start!
            val challenge1 = Challenge(
                "Mehr Sport",
                "Jeden Tag 10 Liegestütze und 15 Push Ups!",
                null,
                3,
                "medium",
                5f,
                230976
            )
            val challenge2 = Challenge(
                "Gesünder leben",
                "An apple a day, keeps the doctor away!",
                R.drawable.test,
                10,
                "high",
                3f,
                2853053
            )

            challengeDao.insert(challenge1)
            challengeDao.insert(challenge2)

            /*
            //TODO: pre populate the database from an external file on the hard disk?
            val resources = context.resources
            val jsonString = resources.openRawResource(R.raw.players).bufferedReader().use {
                it.readText()
            }
            val typeToken = object : TypeToken<List<Player>>() {}.type
            val tennisPlayers = Gson().fromJson<List<Player>>(jsonString, typeToken)

            playerDao.insertAllPlayers(tennisPlayers)

             */

        }
    }
}