package com.bernovia.zajel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val type = remoteMessage.data["type"]
        val conversationId = remoteMessage.data["conversation_id"]

        if (remoteMessage.notification != null && remoteMessage.notification!!.body != null && remoteMessage.notification!!.title != null) {
            sendNotification(remoteMessage.notification?.body!!, remoteMessage.notification?.title!!, type, conversationId)
        }
    }


    private fun sendNotification(title: String, messageBody: String, type: String?, conversationId: String?) {
        val mBuilder = NotificationCompat.Builder(applicationContext, "za")
        val pendingIntent: PendingIntent
        val num = System.currentTimeMillis().toInt()

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("type", type)
        intent.putExtra("conversation_id", conversationId)

        pendingIntent = PendingIntent.getActivity(this, num, intent, PendingIntent.FLAG_ONE_SHOT)

        val bigText = NotificationCompat.BigTextStyle()
        bigText.bigText(title)
        bigText.setBigContentTitle(messageBody)
        mBuilder.setContentIntent(pendingIntent)
        mBuilder.setSmallIcon(R.drawable.app_logo)
        mBuilder.setContentTitle(title)
        mBuilder.setContentText(messageBody)
        mBuilder.setAutoCancel(true)
        mBuilder.setGroupSummary(true)
        mBuilder.setGroup("Zajel")
        mBuilder.setStyle(bigText)
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("za", "zajel", NotificationManager.IMPORTANCE_HIGH)
            mNotificationManager.createNotificationChannel(channel)
        }
        mNotificationManager.notify(num, mBuilder.build())
    }

}
