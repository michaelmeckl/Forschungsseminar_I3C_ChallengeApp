package com.example.challengecovid.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View.ALPHA
import android.view.View.TRANSLATION_Y
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.challengecovid.BuildConfig
import com.example.challengecovid.R
import com.example.challengecovid.Utils
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.*


//TODO: find a better splash screen logo?
class SplashActivity : AppCompatActivity() {

    private lateinit var job: Job
    private var firstRun: Boolean = false
    private var firstTimeThisDay: Boolean =
        false     //TODO: check if this user is logged in the first time this day and should get a new daily challenge!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        checkFirstRun()
        animateSplashScreen()

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
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
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

    private fun showSplashScreen() {
        job = CoroutineScope(Dispatchers.Default).launch {
            // wait for 2 seconds
            delay(2000)

            if (firstRun) {
                startCharacterSelection()
            } else {
                startMain()
            }
        }
    }

    // TODO navigate to Character Selection
    private fun startCharacterSelection() {

        val intent = Intent(this@SplashActivity, CharacterSelectActivity::class.java)
        startActivity(intent)

        // close this activity so the user can't navigate back to it!
        finish()

    }

    // navigate to Main Activity
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
                //TODO: oder vllt lieber nur einen Toast damit nicht zu aufdringlich?
                showConnectionAlert()
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
            .setPositiveButton(android.R.string.ok) { _, _ -> }     // TODO: dialog.dismiss() and retry ?
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()    // cleanup coroutineScope
    }

    companion object {
        const val PREFS_NAME = "challengeCovidSharedPrefs"
        const val PREFS_VERSION_CODE_KEY = "version_code"
    }
}