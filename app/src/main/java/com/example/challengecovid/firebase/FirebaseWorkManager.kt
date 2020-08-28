package com.example.challengecovid.firebase

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.coroutineScope
import timber.log.Timber

class FirebaseWorkManager(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        Timber.tag("WORK_MANAGER").d("Performing long running task in scheduled job")
        // TODO(developer): add long running task here.
        Result.success()
    }
}