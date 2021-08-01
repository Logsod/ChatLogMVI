package com.local.chatlog.view.recyclerviewitems

import com.local.chatlog.R
import com.local.chatlog.databinding.RowUserMessageChatLogBinding
import com.local.chatlog.model.ChatUser
import com.local.chatlog.model.FlowChatMessage
import com.squareup.picasso.Picasso
import com.xwray.groupie.databinding.BindableItem

class UserMessageItem(val chatUser: ChatUser, val flowMessage: FlowChatMessage.FlowChatNewMessage) :
    BindableItem<RowUserMessageChatLogBinding>() {
    override fun bind(viewBinding: RowUserMessageChatLogBinding, position: Int) {
        viewBinding.textViewMessageRowUserMessage.text = flowMessage.chatMessage.message

        val image = chatUser.userImageUri
        if (image.isNotEmpty()) {
            Picasso.get().load(image).into(viewBinding.imageViewProfileImageRowUserMessage)
        }
    }

    override fun getLayout(): Int {
        return R.layout.row_user_message_chat_log
    }

}