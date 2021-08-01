package com.local.chatlog.interfaces

import com.local.chatlog.model.ChatUser
import com.local.chatlog.model.DatabaseChatUser

interface FirebaseChatUserMapper {
    fun fromEntity(entity: DatabaseChatUser): ChatUser
    fun fromData(data: ChatUser): DatabaseChatUser
}