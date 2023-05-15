package com.teameverywhere.taximobility.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.teameverywhere.taximobility.MyApplication
import com.teameverywhere.taximobility.R
import com.teameverywhere.taximobility.util.Constant

class MyService: Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, Constant.CHANNEL_ID)
            .setContentTitle("TAXIMOBILITY")
            .setContentText("영업을 시작합니다.")
            .setSmallIcon(R.drawable.car_color)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
        startForeground(Constant.NOTIFICATION_ID, notification)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotificationChannel(){
        val notificationChannel = NotificationChannel(Constant.CHANNEL_ID, "MyApp notification", NotificationManager.IMPORTANCE_LOW)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.description = "TaxiMobility Notification"

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}