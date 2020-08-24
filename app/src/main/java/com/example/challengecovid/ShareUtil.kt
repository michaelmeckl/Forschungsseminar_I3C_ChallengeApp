package com.example.challengecovid

import android.app.Activity
import android.content.Intent
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat.startActivity

// Helper function for calling a share functionality.
// Should be used when user presses a share button/menu item.
fun createShareIntent(context: Activity) {

    //val contenturi = Uri.parse("android.resource://" + context.packageName + "/drawable/" + "test")

    val shareIntent = ShareCompat.IntentBuilder.from(context)
        .setChooserTitle("Share via")
        //.setStream(contenturi)
        //.setType("image/*")
        .setText("Click this: http://www.example.com/detail")
        .setType("text/plain")
        .createChooserIntent()
        .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
    startActivity(context, shareIntent, null)
}