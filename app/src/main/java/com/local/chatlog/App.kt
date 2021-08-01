package com.local.chatlog

import android.app.Application
import com.local.chatlog.di.component.AppComponent
import com.local.chatlog.di.component.DaggerAppComponent

class App : Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
}