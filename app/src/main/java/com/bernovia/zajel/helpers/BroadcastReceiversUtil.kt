package com.bernovia.zajel.helpers

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bernovia.zajel.App.Companion.context

object BroadcastReceiversUtil {

    const val TOKEN_MISMATCH = "com.bernovia.zajel.tokenMismatch"
    const val BOTTOM_SHEET_SELECT_VALUE = "com.bernovia.zajel.bottomSheetSelectValue"


    fun registerTheReceiver(broadcastReceiver: BroadcastReceiver, filter: String) {
        val intentFilter = IntentFilter(filter)
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, intentFilter)
    }

    fun generalBroadcastIntent(action: String) {
        val intent = Intent()
        intent.action = action

        // We should use LocalBroadcastManager when we want INTRA app
        // communication
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }


     fun bottomSheetSelectedBroadcastIntent(title: String, value: String) {
        val intent = Intent()
        intent.action = BOTTOM_SHEET_SELECT_VALUE
        intent.putExtra("title", title)
        intent.putExtra("value", value)
        // We should use LocalBroadcastManager when we want INTRA app
        // communication
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }


}