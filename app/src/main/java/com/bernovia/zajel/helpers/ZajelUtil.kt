package com.bernovia.zajel.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import okhttp3.Headers

object ZajelUtil {
    val preferenceManager: PreferenceManager = PreferenceManager.instance

    private const val CLICK_TIME_INTERVAL: Long = 1000
    private var mLastClickTime = System.currentTimeMillis()

    fun singleItemClick(): Boolean {
        val now = System.currentTimeMillis()
        if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
            return true
        }
        mLastClickTime = now
        return false
    }


    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    fun hideKeyboard(activity: Activity?) {
        if (activity != null && activity.window != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
        }
    }

    fun shareButton(context: Context) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Join Zajel")
        intent.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=" + context.packageName)
        context.startActivity(Intent.createChooser(intent, "Send Invite link"))

    }

    fun setHeaders(headers: Headers, preferenceManager: PreferenceManager) {
        preferenceManager.accessToken = headers["Access-Token"]
        preferenceManager.client = headers["Client"]
        preferenceManager.expiry = headers["Expiry"]
        preferenceManager.uid = headers["Uid"]
        preferenceManager.tokenType = headers["Token-Type"]
    }
}