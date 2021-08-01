package com.local.chatlog.interfaces

import com.local.chatlog.model.ChatMessage
import com.local.chatlog.model.DatabaseChatMessage

interface FirebaseChatMessageMapper {
    fun fromEntity(entity: DatabaseChatMessage): ChatMessage
    fun fromData(data: ChatMessage): DatabaseChatMessage
}