package com.example.challengecovid.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.challengecovid.R
import com.rbddevs.splashy.Splashy
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)

        // Call immediately after any setContentView() for quick launch
        setSplashy()
    }

    private fun setSplashy() {
        // show splashy screen
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
    }

    private fun startMain() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)

        // close this activity so the user can't navigate back to it!
        finish()
    }

}