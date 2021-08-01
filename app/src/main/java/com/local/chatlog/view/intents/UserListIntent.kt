package com.local.chatlog.view.intents

import com.local.chatlog.model.ChatUser
import com.local.chatlog.view.states.UserListStates

sealed class UserListIntent {
    class StartChat(val user: ChatUser, val partner: ChatUser) : UserListIntent()
}