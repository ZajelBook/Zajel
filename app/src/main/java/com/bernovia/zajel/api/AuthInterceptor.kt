package com.bernovia.zajel.api

import android.content.pm.PackageManager
import com.bernovia.zajel.App
import com.bernovia.zajel.helpers.StringsUtil.validateString
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * A {@see RequestInterceptor} that adds the headers to requests
 */
class AuthInterceptor : Interceptor {

    @Throws(IOException::class) override fun intercept(chain: Interceptor.Chain): Response {
        var UAString = ""
        val UA = System.getProperty("http.agent")
        if (UA != null) {
            UAString = UA
        }

        var pInfo: String? = null
        try {
            pInfo = App.context.packageManager.getPackageInfo(App.context.packageName, 0).versionName
        }
        catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        val request = chain.request().newBuilder().addHeader("User-Agent", UAString).addHeader("app-version", validateString(pInfo)).build()


        return chain.proceed(request)
    }


}
