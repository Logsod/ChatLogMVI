package com.local.chatlog.repository

import com.local.chatlog.model.DatabaseChatRoom
import com.local.chatlog.model.DatabaseChatUser

sealed class UserListData {
    class Added(val chatUser: DatabaseChatUser) : UserListData()
    class Remove(val chatUser: DatabaseChatUser) : UserListData()
    class Fail(val e: Exception) : UserListData()
}

sealed class UserMessageData {
    object Success : UserMessageData()
    class Fail(val e: Exception) : UserMessageData()
}

sealed class UserAuthData {
    //object LoginDataReceivedSuccess : UserAuthData()
    object Success : UserAuthData()
    object ImageLoadSuccess : UserAuthData()
    object ImageLoadFail : UserAuthData()
    class Fail(val e: Exception) : UserAuthData()
}

sealed class UserRoomData {
    class Success(val chatRoom: DatabaseChatRoom) : UserRoomData()
    class Fail(val e: Exception) : UserRoomData()
}
