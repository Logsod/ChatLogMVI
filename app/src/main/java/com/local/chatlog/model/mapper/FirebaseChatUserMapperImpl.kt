package com.local.chatlog.model.mapper

import com.local.chatlog.interfaces.FirebaseChatUserMapper
import com.local.chatlog.model.ChatUser
import com.local.chatlog.model.DatabaseChatUser

class FirebaseChatUserMapperImpl : FirebaseChatUserMapper {
    override fun fromEntity(entity: DatabaseChatUser): ChatUser {
        return ChatUser(
            uid = entity.uid,
            userName = entity.userName,
            userImageUri = entity.userImageUri
        )
    }

    override fun fromData(data: ChatUser): DatabaseChatUser {
        return DatabaseChatUser(
            uid = data.uid,
            userName = data.userName,
            userImageUri = data.userImageUri
        )
    }
}