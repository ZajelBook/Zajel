package com.bernovia.zajel.helpers

import android.content.Context
import android.util.TypedValue

object ViewUtil {
    fun dpToPx(dp: Int, context: Context): Int {
        val r = context.resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
    }

}