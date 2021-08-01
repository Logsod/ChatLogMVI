package com.local.chatlog.interfaces

import kotlinx.coroutines.ExperimentalCoroutinesApi

interface LoginViewModel {
    @ExperimentalCoroutinesApi
    fun handleIntent()
}