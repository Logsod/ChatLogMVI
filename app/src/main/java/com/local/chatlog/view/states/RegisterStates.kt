package com.local.chatlog.view.states

sealed class RegisterStates {

    class Fail(val e: Exception) : RegisterStates()

    object Idle : RegisterStates()
    object Success : RegisterStates()
}