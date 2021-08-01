package com.local.chatlog.view.states

sealed class ChatLogState {
    object Idle : ChatLogState()
    object SendSuccess : ChatLogState()
    class Fail(val e : Exception) : ChatLogState()
}