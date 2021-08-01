package com.local.chatlog.model

import java.lang.Exception

sealed class FlowChatMessage() {
    class FlowChatNewMessage(val key : String, val chatMessage: DatabaseChatMessage) : FlowChatMessage()
    class FlowChatRemoveMessage(val key : String, val chatMessage: DatabaseChatMessage) : FlowChatMessage()
    class FlowChatError(val e : Exception) : FlowChatMessage()
}