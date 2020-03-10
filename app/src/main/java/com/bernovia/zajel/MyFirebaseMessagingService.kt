package com.bernovia.zajel

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

    }


    val NOTIFICATION_ID = 239


    override fun onMessageReceived(remoteMessage: RemoteMessage) {


        //Calling method to generate notification
        sendNotification(
            remoteMessage.notification!!.body!!,
            remoteMessage.notification!!.title!!
        )
    }

    private fun sendNotification(
        messageBody: String,
        messageTitle: String
    ) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val inboxStyle = NotificationCompat.InboxStyle()
        val defaultSoundUri =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_launcher_background) //.setColor(getColor(R.color.black))
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setGroupSummary(true)
            .setStyle(inboxStyle)
            .setContentIntent(pendingIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify("Zajel", NOTIFICATION_ID, notificationBuilder.build())
    }
}
