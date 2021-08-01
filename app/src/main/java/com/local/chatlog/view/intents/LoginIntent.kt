package com.local.chatlog.view.intents

import android.util.Log
import kotlinx.coroutines.channels.Channel

sealed class LoginIntent {
    class Login(val email:String,val password : String) : LoginIntent()
    object Idle : LoginIntent()
}