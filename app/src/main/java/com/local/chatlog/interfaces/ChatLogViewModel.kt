package com.local.chatlog.interfaces

import kotlinx.coroutines.ExperimentalCoroutinesApi

interface ChatLogViewModel {
    fun getCurrentUid(): String
    fun handleIntent()

    @ExperimentalCoroutinesApi
    fun receiveMessages(roomId: String)
}