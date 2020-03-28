package com.bernovia.zajel.helpers

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bernovia.zajel.App

object BroadcastReceiversUtil {

    const val TOKEN_MISMATCH = "com.bernovia.zajel.tokenMismatch"


    fun registerTheReceiver(broadcastReceiver: BroadcastReceiver, filter: String) {
        val intentFilter = IntentFilter(filter)
        if (App.context != null) LocalBroadcastManager.getInstance(App.context).registerReceiver(broadcastReceiver, intentFilter)
    }

    fun generalBroadcastIntent(action: String) {
        val intent = Intent()
        intent.action = action

        // We should use LocalBroadcastManager when we want INTRA app
        // communication
        if (App.context != null) LocalBroadcastManager.getInstance(App.context).sendBroadcast(intent)
    }


}