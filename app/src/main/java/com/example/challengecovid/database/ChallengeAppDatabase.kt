package com.example.challengecovid.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.challengecovid.R
import com.example.challengecovid.database.ChallengeAppDatabase.Companion.DB_VERSION
import com.example.challengecovid.database.dao.*
import com.example.challengecovid.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@Database(
    entities = [Challenge::class, ChallengeCategory::class, UserChallenge::class, User::class, ChallengeUserCrossRef::class],
    version = DB_VERSION,
    exportSchema = true
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
        const val DB_VERSION = 2
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
                //.createFromAsset(DB_NAME)   //TODO: does work but always needs to be in sync with current schema!!
                .addCallback(ChallengeDatabaseCallback(scope, context))
                .fallbackToDestructiveMigration()   // Wipes and rebuilds db instead of migrating
                .build()
        }

        fun destroyDatabase() {
            INSTANCE = null
        }
    }


    private class ChallengeDatabaseCallback(
        private val scope: CoroutineScope,
        private val context: Context
    ) : RoomDatabase.Callback() {

        // INFO: wird nur beim ersten Mal aufgerufen (zum Testen App daten löschen oder neu installieren!)
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Timber.tag("DB_DEBUG").d("in onCreate DB")

            // Populate the database on creation with initial challenge data
            INSTANCE?.let { database ->
                scope.launch {
                    prepopulateDatabase(database.categoryDao(), database.challengeDao(), context)
                }
            }
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            Timber.tag("DB_DEBUG").d("in onOpen DB")
        }

        private suspend fun prepopulateDatabase(
            categoryDao: CategoryDao,
            challengeDao: ChallengeDao,
            context: Context
        ) {
            //challengeDao.clear()
            //categoryDao.clear()   // implement me

            //TODO: wir brauchen dringend bessere Icons als die Standards aus Android Studio!

            //TODO: vllt als externe Json-Datei? Sollte auf jeden Fall nicht hardcoded hier sein!!!!!
            val healthyCategory = ChallengeCategory(
                title = "Gesunder Lebensstil",   //TODO: als string ressourcen auslagern!
                description = "Diese Kategorie enthält Challenges, die einen gesunden Lebensstil zum Ziel haben.",
                iconPath = R.drawable.ic_star
            )
            val sportCategory = ChallengeCategory(
                title = "Sport",
                description = "Diese Kategorie enthält Challenges, die Bewegung und körperliche Aktivitäten fördern.",
                iconPath = R.drawable.ic_done
            )
            val relaxCategory = ChallengeCategory(
                title = "Entspannen",
                description = "Diese Kategorie enthält Challenges, die für etwas Ruhe und Entspannung im Alltag hilfreich sind.",
                iconPath = R.drawable.ic_statistics
            )

            categoryDao.insertAll(listOf(healthyCategory, sportCategory, relaxCategory))

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