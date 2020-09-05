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
import androidx.appcompat.app.AppCompatActivity
import com.example.challengecovid.Data
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.Utils
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.*
import timber.log.Timber


class SplashActivity : AppCompatActivity() {

    private lateinit var job: Job
    private var firstRun = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Timber.tag("FIREBASE").d("in onCreate in splash activity")
        animateSplashScreen()

        firstRun = Utils.checkFirstRun(this@SplashActivity)

        // if this is the first start prepopulate the firestore db
        if (firstRun) initDatabase()

        handleIncomingCloudMessages()

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

    private fun initDatabase() {
        val categoryRepo = RepositoryController.getCategoryRepository()
        val challengeRepo = RepositoryController.getChallengeRepository()

        categoryRepo.saveMultipleCategories(Data.getChallengeCategories())
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
            // show the splash screen for 1 1/2 seconds
            delay(1500)

            if (firstRun) {
                startCharacterCreation()
            } else {
                startMain()
            }
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

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()    // cleanup coroutineScope
    }

    companion object {
        const val PREFS_VERSION_CODE_KEY = "version_code"
    }
}