package com.example.architecture

import android.app.Application
import android.content.Context
import com.example.architecture.ioc.ApplicationComponent

/**
 * Custom Application class allows to hold reference to [applicationComponent]
 * as long as application lives.
 */
class App : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = ApplicationComponent()
    }

    companion object {
        /**
         * Shortcut to get [App] instance from any context, e.g. from Activity.
         */
        fun get(context: Context): App = context.applicationContext as App
    }
}
