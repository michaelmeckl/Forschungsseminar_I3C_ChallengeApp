package com.example.challengecovid.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View.*
import androidx.appcompat.app.AppCompatActivity
import com.example.challengecovid.R
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.*


//TODO: find a better splash screen logo?
//TODO: perform something useful in here? for example fetching data from the internet and save it in the local db
class SplashActivity : AppCompatActivity() {

    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // set some nice animations
        val objectAnimator1: ObjectAnimator = ObjectAnimator.ofFloat(splash_title, TRANSLATION_Y, 100f)
        val objectAnimator2: ObjectAnimator = ObjectAnimator.ofFloat(splash_text, ALPHA, 0F, 1F)
        val objectAnimator3: ObjectAnimator = ObjectAnimator.ofFloat(splash_logo, ALPHA, 0F, 1F)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(objectAnimator1, objectAnimator2, objectAnimator3)
        animatorSet.duration = 800
        animatorSet.start()

        showSplashScreen()
    }

    private fun showSplashScreen() {
        /*
        // show splashy screen
        //TODO: splashy leaks memory!
        Splashy(this)
            .setLogo(R.drawable.humaaans_icon)
            .setLogoScaleType(ImageView.ScaleType.CENTER_CROP)
            .setTitle(R.string.app_name)
            .setTitleSize(30F)
            .setSubTitle(R.string.splash_screen_load)
            .showProgress(true)
            .setProgressColor(R.color.colorAccentDark)
            .setBackgroundColor(R.color.colorPrimary)
            .setAnimation(Splashy.Animation.SLIDE_IN_LEFT_RIGHT, 1000)
            .setFullScreen(true)
            .setDuration(3000)
            //.setInfiniteDuration(true)   // if a fixed duration is not useful; can be removed with Splashy.hide() later
            .show()

        // wait for completion of splash screen
        Splashy.onComplete(object : Splashy.OnComplete {
            override fun onComplete() {
                // navigate to Main Activity when finished
                startMain()
            }

        })
        */

        job = CoroutineScope(Dispatchers.Default).launch {
            // wait for 2 seconds
            delay(2000)
            // then navigate to Main Activity
            startMain()
        }
    }

    private fun startMain() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)

        // close this activity so the user can't navigate back to it!
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()    // cleanup coroutineScope
    }

}