package com.local.chatlog.view.intents

import android.net.Uri

sealed class RegisterIntent {
    class RegisterWithEmailAndPassword(
        val name: String,
        val email: String,
        val password: String,
        val profileImage: Uri?
    ) : RegisterIntent()

    object Idle : RegisterIntent()
}