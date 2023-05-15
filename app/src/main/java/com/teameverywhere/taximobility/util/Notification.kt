package com.teameverywhere.taximobility.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaMetadataRetriever
import android.media.RingtoneManager
import android.net.Uri
import android.os.Handler
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.teameverywhere.taximobility.R
import com.teameverywhere.taximobility.view.activity.HomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


//진동모드on -> 어떤 상황에서도 벨소리없이 진동만
//Notification.notificationSetting(applicationContext, pendingIntent, resources.getString(R.string.app_name), "테스트중입니다", true, CHANNEL_ID2)
//+   Notification.noSoundChannelSetting(this)

object Notification {
    const val TAG = "Notification"

    lateinit var audioAttributes: AudioAttributes
    lateinit var soundUri: Uri
    lateinit var channelId: String

    fun channelSetting(context: Context) {
        channelId = HomeActivity.CHANNEL_ID
        val channel = NotificationChannel(channelId, HomeActivity.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
            lightColor = Color.BLUE
            enableLights(true)
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun noSoundChannelSetting(context: Context) {
        channelId = HomeActivity.CHANNEL_ID2
        soundUri =
            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.nosound)
        audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()

        val channel = NotificationChannel(channelId, HomeActivity.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
            lightColor = Color.BLUE
            enableLights(true)
            setSound(soundUri, audioAttributes)
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun notificationSetting(context: Context, title: String, description: String, channelID: String) {
        var notif = NotificationCompat.Builder(context, channelID)

        var vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        var vibrationPattern = longArrayOf(0, 500, 250, 500)

        notif.apply {
            setContentTitle(title)
            setContentText(description)
            setSmallIcon(R.drawable.car_color)
            setVibrate(vibrationPattern)
            priority = NotificationCompat.PRIORITY_HIGH
            setAutoCancel(true)
            setOnlyAlertOnce(false)
        }

        vibrator.vibrate(vibrationPattern, -1)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(HomeActivity.NOTIF_ID, notif.build())
    }
}