package com.local.chatlog.interfaces

import android.net.Uri
import com.local.chatlog.model.*
import com.local.chatlog.repository.UserAuthData
import com.local.chatlog.repository.UserListData
import com.local.chatlog.repository.UserMessageData
import com.local.chatlog.repository.UserRoomData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface Repository {

    var user : DatabaseChatUser
    fun fetchAllUser(): Flow<UserListData>

    fun getCurrentUser(): DatabaseChatUser
    suspend fun registerWithEmailAndPassword(
        name: String,
        email: String,
        password: String,
        profileImage: Uri?
    ): UserAuthData

    suspend fun loginWithEmailAndPassword(email: String, password: String): UserAuthData
    suspend fun fetchCurrentUser()
    suspend fun createOrReceiveChatRoom(user: DatabaseChatUser, partner: DatabaseChatUser): UserRoomData
    suspend fun sendMessage(
        chatRoom: DatabaseChatRoom,
        chatMessage: DatabaseChatMessage
    ): UserMessageData

    fun subscribeToNewMessages(roomId: String) = callbackFlow<FlowChatMessage> { }


    fun signOut()
    fun getCurrentUid(): String
}


