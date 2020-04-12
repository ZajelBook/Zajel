package com.bernovia.zajel

import android.app.Application
import android.content.Context
import com.bernovia.zajel.di.AppKoinModules
import com.bernovia.zajel.helpers.PreferenceManager
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class App : Application() {


    override fun onCreate() {
        super.onCreate()

        initializeInjector()
        PreferenceManager.initializeInstance(this)

        instance = this

        FirebaseApp.initializeApp(this)


    }

    private fun initializeInjector() {
        startKoin {
            // declare used Android context
            androidLogger()
            androidContext(this@App)
            modules(AppKoinModules.getModules())
        }
    }

    companion object {

        /**
         * Gets an instance of the main application.
         */
        var instance: App? = null
            private set

        /**
         * Gets a context of the main application.
         */
        val context: Context
            get() = instance!!.applicationContext
    }


}