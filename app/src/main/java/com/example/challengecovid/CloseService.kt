package com.example.challengecovid

import android.app.Service
import android.content.Intent
import android.os.IBinder

class CloseService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

        // Handle application closing
        App.instance.clearViewModelStore()

        // Destroy the service
        stopSelf()
    }

}