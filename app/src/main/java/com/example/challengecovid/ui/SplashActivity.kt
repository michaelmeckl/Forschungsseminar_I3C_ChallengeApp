package com.example.challengecovid.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.challengecovid.R
import com.example.challengecovid.Utils
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.*


//TODO: find a better splash screen logo?
class SplashActivity : AppCompatActivity() {

    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // set some nice animations
        val objectAnimator1: ObjectAnimator = ObjectAnimator.ofFloat(splash_title, TRANSLATION_Y, 100f)
        val objectAnimator2: ObjectAnimator = ObjectAnimator.ofFloat(splash_logo, ALPHA, 0F, 1F)
        val objectAnimator3: ObjectAnimator = ObjectAnimator.ofFloat(splash_text, TRANSLATION_Y, -100f)
        //TODO: was anderes als Laden ... anzeigen? z.B. irgendwas wie "Bekämpfe Covid durch Zusammenhalt!" oder so...

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(objectAnimator1, objectAnimator2, objectAnimator3)
        animatorSet.duration = 600
        animatorSet.start()

        //TODO: loadNewChallengeData()
        showSplashScreen()
    }

    private fun showSplashScreen() {
        job = CoroutineScope(Dispatchers.Default).launch {
            // wait for 2 seconds
            delay(2000)
            // then navigate to Main Activity
            startMain()
        }
    }

    //TODO: die Charakterauswahl starten, falls zum ersten mal angemeldet und dann erst zu Main; sonst gleich zu main!
    // vgl. conditional navigation 
    private fun startMain() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)

        // close this activity so the user can't navigate back to it!
        finish()
    }

    private fun loadNewChallengeData() {

        this.let {
            if (Utils.isNetworkConnected(it)) {
                fetchNewData()
            } else {
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
            .setTitle("No Internet Connection")     //TODO: als string ressource!
            .setMessage("No new data can be loaded! Please check your internet connection and try again.")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.ok) { _, _ -> }     // TODO: dialog.dismiss() and retry ?
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()    // cleanup coroutineScope
    }

}