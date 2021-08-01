package com.local.chatlog.model.mapper

import com.local.chatlog.interfaces.FirebaseChatRoomMapper
import com.local.chatlog.model.ChatRoom
import com.local.chatlog.model.DatabaseChatRoom

class FirebaseChatRoomMapperImpl() : FirebaseChatRoomMapper {
    override fun fromEntity(entity: DatabaseChatRoom): ChatRoom {
        return ChatRoom(
            roomId = entity.roomId,
            user1 = FirebaseChatUserMapperImpl().fromEntity(entity = entity.user1),
            user2 = FirebaseChatUserMapperImpl().fromEntity(entity = entity.user2),
            createdOn = entity.createdOn
        )
    }

    override fun fromData(data: ChatRoom): DatabaseChatRoom {
        return DatabaseChatRoom(
            roomId = data.roomId,
            user1 = FirebaseChatUserMapperImpl().fromData(data.user1),
            user2 = FirebaseChatUserMapperImpl().fromData(data.user2),
            createdOn = data.createdOn
        )
    }
}