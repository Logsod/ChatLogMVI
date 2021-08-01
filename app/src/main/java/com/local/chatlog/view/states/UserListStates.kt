package com.local.chatlog.view.states

import com.local.chatlog.model.ChatRoom
import com.local.chatlog.view.intents.UserListIntent
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

sealed class UserListStates{
    object Idle : UserListStates()
    object CurrentUserDataReceived : UserListStates()
    class EnterRoom(val chatRoom: ChatRoom) : UserListStates()
    class Fail(val e :Exception) : UserListStates()
}