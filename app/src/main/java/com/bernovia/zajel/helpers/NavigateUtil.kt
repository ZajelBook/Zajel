package com.bernovia.zajel.helpers

import android.content.Context
import android.content.Intent

object NavigateUtil {
    inline fun <reified T> start(context: Context) {
        context.startActivity(Intent(context, T::class.java))
    }
}