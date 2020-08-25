package com.example.challengecovid.firebase

import android.widget.Toast
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class MessagingService: FirebaseMessagingService() {

    init {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.tag(TOKEN_TAG).d("getInstanceId failed ${task.exception}")
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                val msg = "Token retrieved successfully: $token"
                Timber.tag(TOKEN_TAG).d(msg)
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            })
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Timber.tag(MSG_TAG).d("From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Timber.tag(MSG_TAG).d("Message data payload: ${remoteMessage.data}")

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow()
            }
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Timber.tag(MSG_TAG).d("Message Notification Body: ${it.body}")
        }
    }

    /**
     * Schedule async work using WorkManager.
     */
    private fun scheduleJob() {
        val workRequest = OneTimeWorkRequestBuilder<FirebaseWorkManager>().build()
        WorkManager.getInstance(this).beginWith(workRequest).enqueue()
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Timber.tag(MSG_TAG).d("Short lived task is done.")
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.tag(TOKEN_TAG).d("Refreshed token: $token")

        // If you want to send messages to this application instance or manage this apps subscriptions on the server side,
        // send the Instance ID token to your app server.

        //sendRegistrationToServer(token)

        //TODO should this be in the splash activity instead?
        //subscribe to a topic after receiving your token
        //if the topic doesn't exist yet it will be created automatically!
        FirebaseMessaging.getInstance().subscribeToTopic("new_challenges")
            .addOnCompleteListener { task ->
                var msg = "Successfully subscribed to topic!"
                if (!task.isSuccessful) {
                    msg = "Subscribing to topic failed!"
                }
                Timber.tag(MSG_TAG).d(msg)
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        const val MSG_TAG = "FIREBASE_MESSAGE"
        const val TOKEN_TAG = "FIREBASE_TOKEN"
    }
}