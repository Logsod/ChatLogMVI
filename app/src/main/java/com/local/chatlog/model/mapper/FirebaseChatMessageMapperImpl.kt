package com.local.chatlog.model.mapper

import com.local.chatlog.interfaces.FirebaseChatMessageMapper
import com.local.chatlog.model.ChatMessage
import com.local.chatlog.model.DatabaseChatMessage

class FirebaseChatMessageMapperImpl() : FirebaseChatMessageMapper {
    override fun fromEntity(entity: DatabaseChatMessage): ChatMessage {
        return ChatMessage(
            fromUid = entity.fromUid, message = entity.message, timestamp = entity.timestamp
        )
    }

    override fun fromData(data: ChatMessage): DatabaseChatMessage {
        return DatabaseChatMessage(
            fromUid = data.fromUid,
            message = data.message,
            timestamp = data.timestamp
        )
    }
}