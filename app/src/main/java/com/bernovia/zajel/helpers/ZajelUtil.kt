package com.bernovia.zajel.helpers

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import okhttp3.Headers

object ZajelUtil {
    fun hideKeyboard(activity: Activity?) {
        if (activity != null && activity.window != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
        }
    }


    fun setHeaders(headers: Headers, preferenceManager: PreferenceManager) {
        preferenceManager.accessToken = headers.get("Access-Token")
        preferenceManager.client = headers.get("Client")
        preferenceManager.expiry = headers.get("Expiry")
        preferenceManager.uid = headers.get("Uid")
        preferenceManager.tokenType = headers.get("Token-Type")
    }
}