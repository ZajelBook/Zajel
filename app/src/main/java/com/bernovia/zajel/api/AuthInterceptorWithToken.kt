package com.bernovia.zajel.api

import android.content.pm.PackageManager
import com.bernovia.zajel.App.Companion.context
import com.bernovia.zajel.helpers.StringsUtil.validateString
import com.bernovia.zajel.helpers.ZajelUtil.preferenceManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthInterceptorWithToken : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var UAString = ""
        val UA = System.getProperty("http.agent")
        if (UA != null) {
            UAString = UA
        }

        var pInfo: String? = null
        try {
            pInfo = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        val request = chain.request().newBuilder()
            .addHeader("Access-Token", validateString(preferenceManager.accessToken))
            .addHeader("Client", validateString(preferenceManager.client))
            .addHeader("Expiry", validateString(preferenceManager.expiry))
            .addHeader("Uid", validateString(preferenceManager.uid))
            .addHeader("Token-Type", validateString(preferenceManager.tokenType))
            .addHeader("User-Agent", UAString)
            .addHeader("app-version", validateString(pInfo))
            .build()


        return chain.proceed(request)
    }


}
