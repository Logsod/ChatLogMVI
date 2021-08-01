package com.local.chatlog.di.component

import com.local.chatlog.di.module.Module
import com.local.chatlog.di.viewmodel.ViewModelModule
import com.local.chatlog.view.activity.ChatLogActivity
import com.local.chatlog.view.activity.LoginActivity
import com.local.chatlog.view.activity.RegisterActivity
import com.local.chatlog.view.activity.UserListActivity
import dagger.Component
import javax.inject.Singleton

//@Suppress("unused")
@Singleton
@Component(modules = [Module::class, ViewModelModule::class])
interface AppComponent {
    fun inject(activity: RegisterActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: UserListActivity)
    fun inject(activity: ChatLogActivity)
}