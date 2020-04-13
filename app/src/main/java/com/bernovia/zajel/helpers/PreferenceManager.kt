package com.bernovia.zajel.helpers

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager private constructor(context: Context) {
    private val mPref: SharedPreferences


    var accessToken: String?
        get() = mPref.getString(ACCESS_TOKEN, "")
        set(value) = mPref.edit().putString(ACCESS_TOKEN, value).apply()
    var client: String?
        get() = mPref.getString(CLIENT, "")
        set(value) = mPref.edit().putString(CLIENT, value).apply()
    var expiry: String?
        get() = mPref.getString(EXPIRY, "")
        set(value) = mPref.edit().putString(EXPIRY, value).apply()
    var uid: String?
        get() = mPref.getString(UID, "")
        set(value) = mPref.edit().putString(UID, value).apply()
    var tokenType: String?
        get() = mPref.getString(TOKEN_TYPE, "")
        set(value) = mPref.edit().putString(TOKEN_TYPE, value).apply()
    var userId: Int
        get() = mPref.getInt(USER_ID, 0)
        set(value) = mPref.edit().putInt(USER_ID, value).apply()
    var userName: String?
        get() = mPref.getString(USER_NAME, "")
        set(value) = mPref.edit().putString(USER_NAME, value).apply()


    var latitude: Float
        get() = mPref.getFloat(LATITUDE, 0f)
        set(value) = mPref.edit().putFloat(LATITUDE, value).apply()

    var longitude: Float
        get() = mPref.getFloat(LONGITUDE, 0f)
        set(value) = mPref.edit().putFloat(LONGITUDE, value).apply()

    init {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }


    fun clear(): Boolean {
        return mPref.edit().clear().commit()
    }

    companion object {

        private val PREF_NAME = " com.bernovia.zajel.PREF"
        private const val ACCESS_TOKEN = "access_token"
        private const val CLIENT = "client"
        private const val EXPIRY = "expiry"
        private const val UID = "uid"
        private const val TOKEN_TYPE = "token_type"
        private const val USER_ID = "user_id"
        private const val USER_NAME = "user_name"
        private const val LATITUDE = "latitude"
        private const val LONGITUDE = "longitude"


        private var sInstance: PreferenceManager? = null

        @Synchronized
        fun initializeInstance(context: Context) {
            if (sInstance == null) {
                sInstance = PreferenceManager(context)
            }
        }

        val instance: PreferenceManager
            @Synchronized get() {
                if (sInstance == null) {
                    throw IllegalStateException(PreferenceManager::class.java.simpleName + " is not initialized, call initializeInstance(..) method first.")
                }
                return sInstance as PreferenceManager
            }
    }
}