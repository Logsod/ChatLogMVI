package com.local.chatlog.interfaces

import com.local.chatlog.model.ChatRoom
import com.local.chatlog.model.DatabaseChatRoom

interface FirebaseChatRoomMapper {
    fun fromEntity(entity: DatabaseChatRoom): ChatRoom
    fun fromData(data: ChatRoom): DatabaseChatRoom
}