package com.example.challengecovid

import android.app.Application
import timber.log.Timber

/**
 * Overrides the android application class
 * Instantiated before any other class when the process for the application/package is created.
 * Important stuff that has to be initialized before everything else should go here!
 */
@Suppress("unused")
class ChallengeCovidApplication: Application() {

    // Important: more expensive tasks like fetching data from a db or network must be performed on a separate thread
    // so the app start time is not delayed!
    override fun onCreate() {
        super.onCreate()

        // if in Debug Mode enable logging with Timber
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } /* else {
            Timber.plant(CrashReportingTree())
        }*/

    }
}