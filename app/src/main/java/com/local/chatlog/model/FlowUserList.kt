package com.local.chatlog.model

sealed class FlowUserList {
    class FlowUserListAddUser(val chatUser: ChatUser) : FlowUserList()
    class FlowUserListRemoveUser(val chatUser: ChatUser) : FlowUserList()
    class Fail(val e : Exception) : FlowUserList()
}