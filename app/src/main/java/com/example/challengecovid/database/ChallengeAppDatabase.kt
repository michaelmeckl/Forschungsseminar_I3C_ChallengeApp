package com.example.challengecovid.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.challengecovid.database.ChallengeAppDatabase.Companion.DB_VERSION
import com.example.challengecovid.database.dao.*
import com.example.challengecovid.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@Database(
    entities = [Challenge::class, ChallengeCategory::class, UserChallenge::class, User::class, ChallengeUserCrossRef::class],
    version = DB_VERSION,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ChallengeAppDatabase : RoomDatabase() {

    // Connect the database to the DAOs
    abstract fun challengeDao(): ChallengeDao
    abstract fun userChallengeDao(): UserChallengeDao
    abstract fun categoryDao(): CategoryDao
    abstract fun userDao(): UserDao
    abstract fun relationshipsDao(): RelationshipsDao

    // use a companion object to get static access to the db instance (Singleton)
    companion object {
        const val DB_VERSION = 4
        private const val DB_NAME = "challenge_database.sqlite"

        /**
         * INSTANCE will keep a reference to any database returned via getInstance to prevent repeatedly initializing
         * the database (which is expensive)
         * The value of a volatile variable will never be cached, and all writes and reads will be done to and from
         * the main memory. It means that changes made by one thread to shared data are visible to other threads.
         */
        @Volatile
        private var INSTANCE: ChallengeAppDatabase? = null

        /**
         * If a database has already been retrieved, the previous database will be returned. Otherwise, create a new
         * database. This function is threadsafe and an example implementation of a Singleton-Pattern.
         *
         * @param context The application context Singleton, used to get access to the filesystem.
         * @return ChallengeDatabase A singleton instance of the Database
         */
        fun getInstance(context: Context, scope: CoroutineScope): ChallengeAppDatabase {
            // Multiple threads can ask for the database at the same time, ensure we only initialize
            // it once by using synchronized. Only one thread may enter a synchronized block at a time.
            synchronized(this) {

                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var instance = INSTANCE
                if (instance == null) {
                    // If instance is `null` make a new database instance and assign INSTANCE to the newly created database.
                    instance = buildDatabase(scope, context).also { INSTANCE = it }
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

        private fun buildDatabase(scope: CoroutineScope, context: Context): ChallengeAppDatabase {
            return Room.databaseBuilder(context.applicationContext, ChallengeAppDatabase::class.java, DB_NAME)
                .addCallback(ChallengeDatabaseCallback(scope))
                //.createFromAsset(DB_NAME)     // alternative way to prepopulate db from a db asset in the assets folder
                .fallbackToDestructiveMigration()   // Wipes and rebuilds db instead of migrating
                .build()
        }
    }


    private class ChallengeDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

        // INFO: wird nur beim ersten Mal aufgerufen (zum Testen App daten löschen oder neu installieren!)
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Timber.tag("DB_DEBUG").d("in onCreate DB")

            // Populate the database on creation with initial challenge data
            INSTANCE?.let { database ->
                scope.launch {
                    prepopulateDatabase(database.categoryDao(), database.challengeDao())
                }
            }
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            Timber.tag("DB_DEBUG").d("in onOpen DB")
        }

        /**
         * Pre-populate the database with categories and (app) challenges.
         */
        private suspend fun prepopulateDatabase(categoryDao: CategoryDao, challengeDao: ChallengeDao) {
            val categories = Data.getChallengeCategories()
            categoryDao.insertAll(categories)

            val challenges = Data.getChallenges()
            challengeDao.insertAll(challenges)
        }
    }
}