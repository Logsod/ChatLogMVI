package com.local.chatlog.model

data class ChatMessage(val fromUid: String, val message: String, val timestamp: Long) {
    constructor() : this("", "", 0)
}