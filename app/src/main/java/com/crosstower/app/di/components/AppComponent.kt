package com.crosstower.app.di.components

import com.crosstower.libraryapi.di.modules.OSApiModule
import com.crosstower.libraryapi.interfaces.OSApi
import android.app.Application
import dagger.Component
import javax.inject.Singleton

/***
 * Application component for DI
 */
@Singleton
@Component( modules = [OSApiModule::class])
interface AppComponent {

    fun inject(application: Application)

    fun getImgApi(): OSApi

}