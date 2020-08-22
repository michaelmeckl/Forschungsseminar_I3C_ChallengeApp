package com.example.challengecovid

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat.startActivity


// Helper function for calling a share functionality.
// Should be used when user presses a share button/menu item.
fun createShareIntent(context: Activity) {
    val shareText =
        "Hello! Do you want to download this amazing new challenge app: " + "\n\n" + BuildConfig.APPLICATION_ID + " \n\n"

    val shareIntent = ShareCompat.IntentBuilder.from(context)
        .setText(shareText)
        .setType("text/plain")
        .createChooserIntent()
        .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
    startActivity(context, shareIntent, null)
}

/**
 * called like:
 * shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.action_share_app))
 * shareActionProvider.setShareIntent(getShareIntent())
 */
fun getShareIntent(): Intent {
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.type = "text/plain"
    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "ChallengeCovid App")
    val shareMessage =
        "Hi! Do you want to download this amazing new challenge app:" + "\n\n" + BuildConfig.APPLICATION_ID + " \n\n"
    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
    return shareIntent
}

//TODO: des geht net:
fun shareImageWithText(context: Context) {
    val contentUri: Uri = Uri.parse("android.resource://" + context.packageName + "/drawable/" + "ic_coronavirus")
    val msg = "Hey, Download this awesome app!"

    val shareIntent = Intent()
    shareIntent.action = Intent.ACTION_SEND
    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
    shareIntent.type = "*/*"
    shareIntent.putExtra(Intent.EXTRA_TEXT, msg)
    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)

    try {
        startActivity(context, Intent.createChooser(shareIntent, "Share via"), null)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No suitable App available!", Toast.LENGTH_SHORT).show()
    }
}