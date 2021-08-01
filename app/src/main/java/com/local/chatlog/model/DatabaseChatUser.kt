package com.local.chatlog.model

data class DatabaseChatUser(val uid : String, val userName: String, val userImageUri: String) {
    constructor() : this( "","", "")
}