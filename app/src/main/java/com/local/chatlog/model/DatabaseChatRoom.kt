package com.local.chatlog.model

data class DatabaseChatRoom(
    val roomId: String,
    val user1: DatabaseChatUser,
    val user2: DatabaseChatUser,
    val createdOn: Long
) {
    constructor() : this("", DatabaseChatUser(), DatabaseChatUser(), 0)
}
