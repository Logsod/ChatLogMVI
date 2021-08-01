package com.local.chatlog.view.intents

import com.local.chatlog.model.ChatMessage
import com.local.chatlog.model.ChatRoom

sealed class ChatLogIntent {
    object Idle : ChatLogIntent()
    class SendMessage(val chatRoom: ChatRoom, val chatMessage: ChatMessage) : ChatLogIntent()
}