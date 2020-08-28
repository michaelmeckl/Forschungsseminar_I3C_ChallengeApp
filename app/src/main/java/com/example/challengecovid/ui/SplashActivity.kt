package com.example.challengecovid.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View.ALPHA
import android.view.View.TRANSLATION_Y
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.challengecovid.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.*
import timber.log.Timber


class SplashActivity : AppCompatActivity() {

    private lateinit var job: Job
    private var firstRun: Boolean = false

    //TODO: check if this user is logged in the first time this day and should get a new daily challenge!
    // alternativ vllt über firebase in app messaging gut umsetzbar!
    private var firstTimeThisDay: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Timber.tag("FIREBASE").d("in onCreate in splash activity")

        checkFirstRun()
        animateSplashScreen()

        // if this is the first start prepopulate the firestore db
        if (firstRun) initDatabase()

        handleIncomingCloudMessages()

        //TODO: loadNewChallengeData()
        showSplashScreen()
    }

    private fun animateSplashScreen() {
        // set some nice animations
        val objectAnimator1: ObjectAnimator = ObjectAnimator.ofFloat(splash_title, TRANSLATION_Y, 100f)
        val objectAnimator2: ObjectAnimator = ObjectAnimator.ofFloat(splash_logo, ALPHA, 0F, 1F)
        val objectAnimator3: ObjectAnimator = ObjectAnimator.ofFloat(splash_text, TRANSLATION_Y, -100f)
        //TODO: was anderes als Laden ... anzeigen? z.B. irgendwas wie "Bekämpfe Covid durch Zusammenhalt!" oder so...

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(objectAnimator1, objectAnimator2, objectAnimator3)
        animatorSet.duration = 600
        animatorSet.start()
    }

    private fun checkFirstRun() {
        // Get current version code
        val currentVersionCode: Int = BuildConfig.VERSION_CODE

        // Get saved version code
        val prefs = getSharedPreferences(Constants.SHARED_PREFS_NAME, MODE_PRIVATE)
        val savedVersionCode = prefs.getInt(PREFS_VERSION_CODE_KEY, -1)

        // Check for first run or upgrade
        when {
            currentVersionCode == savedVersionCode -> {
                // This is just a normal run
                firstRun = false
                return
            }
            savedVersionCode == -1 -> {
                // This is a new install (or the user cleared the shared preferences)
                firstRun = true
            }
            /*
            currentVersionCode > savedVersionCode -> {
                // This is an upgrade; show infos about what has changed since the last version
            }*/
        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREFS_VERSION_CODE_KEY, currentVersionCode).apply()
    }

    private fun initDatabase() {
        val categoryRepo = RepositoryController.getCategoryRepository()
        val challengeRepo = RepositoryController.getChallengeRepository()

        categoryRepo.saveMultipleCategories(Data.getChallengeCategories())

        //TODO: ist das notwendig?? die system challenges gehören doch eh alle zu den kategorien oder?
        challengeRepo.saveMultipleChallenges(Data.getDailyChallenges())
    }

    //TODO:
    private fun handleIncomingCloudMessages() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW
                )
            )
        }

        // Handle possible data accompanying notification message.
        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.get(key)
                Timber.tag("FIREBASE_CLOUD_MESSAGE").d("Key: $key Value: $value")
            }
        }
    }

    private fun showSplashScreen() {
        job = CoroutineScope(Dispatchers.Default).launch {
            // wait for 2 seconds
            delay(2000)

            //TODO: revert this later!!!
            startCharacterCreation()
            /*
            if (firstRun) {
                startCharacterCreation()
            } else {
                startMain()
            }
            */
        }
    }

    private fun startCharacterCreation() {
        val intent = Intent(this@SplashActivity, CharacterCreationActivity::class.java)
        startActivity(intent)

        // close this activity so the user can't navigate back to it!
        finish()
    }

    // navigate direct to Main Activity
    private fun startMain() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)

        finish()
    }

    private fun loadNewChallengeData() {

        this.let {
            if (Utils.isNetworkConnected(it)) {
                fetchNewData()
            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    R.string.no_internet + R.string.no_internet_warning,
                    Snackbar.LENGTH_LONG
                ).show()

                //showConnectionAlert()
            }
        }
    }

    private fun fetchNewData() {
        //TODO get new challenges from internet and save them in the room db!
    }

    // show alert dialog when no internet connection
    private fun showConnectionAlert() {
        AlertDialog.Builder(this)
            .setTitle(R.string.no_internet)
            .setMessage(R.string.no_internet_warning)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()    // cleanup coroutineScope
    }

    companion object {
        const val PREFS_VERSION_CODE_KEY = "version_code"
    }
}