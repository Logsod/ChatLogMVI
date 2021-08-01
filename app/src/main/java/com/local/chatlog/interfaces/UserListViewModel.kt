package com.local.chatlog.interfaces

import com.local.chatlog.model.ChatUser

interface UserListViewModel {
    fun getCurrentUser(): ChatUser
    fun getCurrentUid(): String
    fun fetchAllUser()
    fun handleIntents()
}