package com.crosstower.app

import android.app.Application
import com.crosstower.app.di.components.DaggerAppComponent
import com.crosstower.libraryapi.di.modules.OSApiModule

/***
 * Application class for Crosstower app
 */
class CrossTowerApp : Application() {

    public val appComponent = DaggerAppComponent.builder()
        .oSApiModule(OSApiModule())
        .build()

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }
}