package com.bernovia.zajel.helpers

import retrofit2.HttpException
import javax.net.ssl.HttpsURLConnection


object NetworkUtil {

    fun handleApiError(error: Throwable) {
        if (error is HttpException) {
            when (error.code()) {
                HttpsURLConnection.HTTP_UNAUTHORIZED -> BroadcastReceiversUtil.generalBroadcastIntent(BroadcastReceiversUtil.TOKEN_MISMATCH)
                HttpsURLConnection.HTTP_FORBIDDEN -> {
                }
                HttpsURLConnection.HTTP_INTERNAL_ERROR -> {
                }
                HttpsURLConnection.HTTP_BAD_REQUEST -> {
                }


            }
        }
    }

}