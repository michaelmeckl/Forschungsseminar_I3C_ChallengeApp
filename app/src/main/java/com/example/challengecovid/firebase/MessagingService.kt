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

class MessagingService : FirebaseMessagingService() {

    init {
        Timber.tag(TOKEN_TAG).d("in init in Messaging Service")

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

        //oder:
        /*
        if (remoteMessage.data[ACTION_TYPE_KEY] != null) {
            handleRemoteMessage(remoteMessage);
        } else {
            LogUtil.logError(
                TAG,
                "onMessageReceived()",
                new RuntimeException ("FCM remoteMessage doesn't contains Action Type")
            );
        }

         */
    }

    /*
    //TODO:
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW))
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.get(key)
                Log.d(TAG, "Key: $key Value: $value")
            }
        }
        // [END handle_data_extras]

        binding.subscribeButton.setOnClickListener {
            Log.d(TAG, "Subscribing to weather topic")
            // [START subscribe_topics]
            FirebaseMessaging.getInstance().subscribeToTopic("weather")
                    .addOnCompleteListener { task ->
                        var msg = getString(R.string.msg_subscribed)
                        if (!task.isSuccessful) {
                            msg = getString(R.string.msg_subscribe_failed)
                        }
                        Log.d(TAG, msg)
                        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    }
            // [END subscribe_topics]
        }

        binding.logTokenButton.setOnClickListener {
            // Get token
            // [START retrieve_current_token]
            FirebaseInstanceId.getInstance().instanceId
                    .addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.w(TAG, "getInstanceId failed", task.exception)
                            return@OnCompleteListener
                        }

                        // Get new Instance ID token
                        val token = task.result?.token

                        // Log and toast
                        val msg = getString(R.string.msg_token_fmt, token)
                        Log.d(TAG, msg)
                        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    })
            // [END retrieve_current_token]
        }
     */

    /*
    //TODO:
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Messages", "Messages", importance)
            channel.description = "All messages."
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        createNotificationChannel()

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            val manager = NotificationManagerCompat.from(this)
            val notification = NotificationCompat.Builder(this, "Messages")
                    .setContentText(remoteMessage.data["text"])
                    .setContentTitle("New message")
                    .setSmallIcon(R.drawable.ic_stat_notification)
                    .build()
            manager.notify(0, notification)
        }
    }
     */

    /*
    //TODO:
    private fun handleRemoteMessage(remoteMessage: RemoteMessage) {
        val receivedActionType = remoteMessage.data[ACTION_TYPE_KEY]
        LogUtil.logDebug(TAG, "Message Notification Action Type: $receivedActionType")
        when (receivedActionType) {
            ACTION_TYPE_NEW_LIKE -> parseCommentOrLike(Channel.NEW_LIKE, remoteMessage)
            ACTION_TYPE_NEW_COMMENT -> parseCommentOrLike(Channel.NEW_COMMENT, remoteMessage)
            ACTION_TYPE_NEW_POST -> handleNewPostCreatedAction(remoteMessage)
        }
    }

    private fun handleNewPostCreatedAction(remoteMessage: RemoteMessage) {
        val postAuthorId = remoteMessage.data[AUTHOR_ID_KEY]
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        //Send notification for each users except author of post.
        if (firebaseUser != null && firebaseUser.uid != postAuthorId) {
            PostManager.getInstance(this.applicationContext).incrementNewPostsCounter()
        }
    }

    private fun parseCommentOrLike(channel: Channel, remoteMessage: RemoteMessage) {
        val notificationTitle = remoteMessage.data[TITLE_KEY]
        val notificationBody = remoteMessage.data[BODY_KEY]
        val notificationImageUrl = remoteMessage.data[ICON_KEY]
        val postId = remoteMessage.data[POST_ID_KEY]
        val backIntent = Intent(this, MainActivity::class.java)
        val intent = Intent(this, PostDetailsActivity::class.java)
        intent.putExtra(PostDetailsActivity.POST_ID_EXTRA_KEY, postId)
        val bitmap = getBitmapFromUrl(notificationImageUrl)
        sendNotification(channel, notificationTitle, notificationBody, bitmap, intent, backIntent)
        LogUtil.logDebug(TAG, "Message Notification Body: " + remoteMessage.data[BODY_KEY])
    }

    fun getBitmapFromUrl(imageUrl: String?): Bitmap? {
        return ImageUtil.loadBitmap(
            GlideApp.with(this),
            imageUrl,
            Constants.PushNotification.LARGE_ICONE_SIZE,
            Constants.PushNotification.LARGE_ICONE_SIZE
        )
    }

    private fun sendNotification(
        channel: Channel,
        notificationTitle: String,
        notificationBody: String,
        bitmap: Bitmap,
        intent: Intent
    ) {
        sendNotification(channel, notificationTitle, notificationBody, bitmap, intent, null)
    }

    private fun sendNotification(
        channel: Channel,
        notificationTitle: String?,
        notificationBody: String?,
        bitmap: Bitmap,
        intent: Intent,
        backIntent: Intent?
    ) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent: PendingIntent
        pendingIntent = if (backIntent != null) {
            backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val intents = arrayOf(backIntent, intent)
            PendingIntent.getActivities(this, notificationId++, intents, PendingIntent.FLAG_ONE_SHOT)
        } else {
            PendingIntent.getActivity(this, notificationId++, intent, PendingIntent.FLAG_ONE_SHOT)
        }
        val notificationManager = getSystemService<Any>(Context.NOTIFICATION_SERVICE) as NotificationManager
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder = Builder(this, channel.id)
        notificationBuilder.setAutoCancel(true) //Automatically delete the notification
            .setSmallIcon(R.drawable.ic_push_notification_small) //Notification icon
            .setContentIntent(pendingIntent)
            .setContentTitle(notificationTitle)
            .setContentText(notificationBody)
            .setLargeIcon(bitmap)
            .setSound(defaultSoundUri)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(channel.id, getString(channel.name), importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = ContextCompat.getColor(this, R.color.primary)
            notificationChannel.enableVibration(true)
            notificationBuilder.setChannelId(channel.id)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(notificationId++, notificationBuilder.build())
    }
    */

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
        // im moment wird dieser service hier zwischen on Create von Splash und on Create von Character Creation ausgeführt!
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

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        //TODO: ProfileInteractor.getInstance(applicationContext).updateRegistrationToken(token)
    }

    companion object {
        const val MSG_TAG = "FIREBASE_MESSAGE"
        const val TOKEN_TAG = "FIREBASE_TOKEN"
    }
}