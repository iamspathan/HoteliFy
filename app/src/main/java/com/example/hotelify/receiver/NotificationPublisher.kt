package com.example.hotelify.receiver

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationPublisher : BroadcastReceiver() {

    companion object{
        val NOTIFICATION_ID  = "NOTIFICATION_ID"
        val NOTIFICATION  = "NOTIFICATION"
    }


    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = intent?.getParcelableExtra<Notification>(NOTIFICATION)
        val id = intent?.getIntExtra(NOTIFICATION_ID,0)
        if (id != null) {
            notificationManager.notify(id,notification)
        }

    }
}