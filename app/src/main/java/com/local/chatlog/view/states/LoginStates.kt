package com.local.chatlog.view.states

sealed class LoginStates {
    class Fail(val e: Exception) : LoginStates()
    object Idle : LoginStates()
    object Success : LoginStates()
}