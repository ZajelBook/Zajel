package com.bernovia.zajel.helpers

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bernovia.zajel.R

object NavigateUtil {


    inline fun <reified T> start(context: Context) {
        context.startActivity(Intent(context, T::class.java).setFlags(FLAG_ACTIVITY_NEW_TASK))
    }

    fun closeFragment(fragmentManager: FragmentManager, fragment: Fragment) {
        val trans = fragmentManager.beginTransaction()
        trans.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_right, R.anim.slide_in_from_left, R.anim.slide_out_from_right)
        trans.remove(fragment)
        trans.commit()
        fragmentManager.popBackStack()
    }
}