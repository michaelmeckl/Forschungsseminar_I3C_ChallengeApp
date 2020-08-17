package com.example.challengecovid

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Overrides the android application class
 * Instantiated before any other class when the process for the application/package is created.
 * Important stuff that has to be initialized before everything else should go here!
 */
@Suppress("unused")
class ChallengeCovidApplication: Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        init()
    }

    /**
     * Expensive tasks like fetching data from a db or network must be performed on a separate thread
     * so the app start time is not delayed!
     */
    private fun init() {
        applicationScope.launch {

            // if in Debug Mode enable logging with Timber
            if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        }
    }
}