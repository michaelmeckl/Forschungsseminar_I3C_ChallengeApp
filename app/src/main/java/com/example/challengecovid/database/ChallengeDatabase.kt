package com.example.challengecovid.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.challengecovid.R
import com.example.challengecovid.database.ChallengeDatabase.Companion.DB_VERSION
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.Difficulty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Challenge::class], version = DB_VERSION, exportSchema = true)
@TypeConverters(Converters::class)
abstract class ChallengeDatabase : RoomDatabase() {

    // Connect the database to the DAO.
    abstract fun challengeDao(): ChallengeDao

    // use a companion object to get static access to the db instance (Singleton)
    companion object {
        const val DB_VERSION = 5
        private const val DB_NAME = "challenge_database.sqlite"

        /**
         * INSTANCE will keep a reference to any database returned via getInstance to prevent repeatedly initializing
         * the database (which is expensive)
         * The value of a volatile variable will never be cached, and all writes and reads will be done to and from
         * the main memory. It means that changes made by one thread to shared data are visible to other threads.
         */
        @Volatile private var INSTANCE: ChallengeDatabase? = null

        /**
         * If a database has already been retrieved, the previous database will be returned. Otherwise, create a new
         * database. This function is threadsafe and an example implementation of a Singleton-Pattern.
         *
         * @param context The application context Singleton, used to get access to the filesystem.
         * @return ChallengeDatabase A singleton instance of the Database
         */
        fun getInstance(context: Context /*TODO:, scope: CoroutineScope*/): ChallengeDatabase {
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
        /*
        // short inline version of the method above
        fun getInstance(context: Context): ChallengeDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        */

        private fun buildDatabase(context: Context): ChallengeDatabase {
            return Room.databaseBuilder(context.applicationContext, ChallengeDatabase::class.java, DB_NAME)
                // Wipes and rebuilds instead of migrating.
                // see https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
                .createFromAsset(DB_NAME)   //TODO: test this out!
                //.addCallback(ChallengeDatabaseCallback(scope, context))    //TODO
                .fallbackToDestructiveMigration()
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
                Difficulty.SCHWER,
                false,
                "TODO1",
                10f,
                null
            )
            val challenge2 = Challenge(
                "Gesünder leben",
                "An apple a day, keeps the doctor away!",
                Difficulty.LEICHT,
                false,
                "TODO2",
                21f,
                R.drawable.ic_graph
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