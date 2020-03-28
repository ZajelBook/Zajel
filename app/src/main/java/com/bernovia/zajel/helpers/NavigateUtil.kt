package com.bernovia.zajel.helpers

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.bernovia.zajel.AskForLocationActivity
import com.bernovia.zajel.auth.logIn.ui.LoginActivity
import com.bernovia.zajel.helpers.ZajelUtil.preferenceManager

object NavigateUtil {


    inline fun <reified T> start(context: Context) {
        context.startActivity(Intent(context, T::class.java).setFlags(FLAG_ACTIVITY_NEW_TASK))
    }


}