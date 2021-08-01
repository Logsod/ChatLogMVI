package com.local.chatlog.utils

import android.content.Context
import android.util.Log
import com.local.chatlog.model.DatabaseChatUser

fun DatabaseChatUser.getRoomIdByOtherUser(uid1 : String) : String?
{

    var roomId: String? = null

    loop@ for (i: Int in this.uid.indices) {
        if (roomId != null) break@loop
        roomId = if (this.uid[i].code > uid1[i].code)
            this.uid + uid1
        else
            uid1 + this.uid

    }
    return  roomId
}

fun String.fromLastDot(): String {
    return this.substring(this.lastIndexOf('.') + 1)
}

fun String.log(context: Context, TAG: String = "", level: Char = 'e') {
    val tagContext = context::class.java.toString().fromLastDot()
    when (level) {
        'e' -> Log.e("$tagContext $TAG", this)
        'd' -> Log.e("$tagContext $TAG", this)
    }
}

fun String.log(context: String, TAG: String = "", level: Char = 'e') {
    val tagContext = context.fromLastDot()
    when (level) {
        'e' -> Log.e("$tagContext $TAG", this)
        'd' -> Log.e("$tagContext $TAG", this)
    }
}