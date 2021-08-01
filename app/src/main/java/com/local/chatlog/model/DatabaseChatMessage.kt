package com.local.chatlog.model


data class DatabaseChatMessage(val fromUid: String, val message: String, val timestamp: Long) {
    constructor() : this("", "", 0)
}