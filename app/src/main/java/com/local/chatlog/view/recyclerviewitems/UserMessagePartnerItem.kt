package com.local.chatlog.view.recyclerviewitems

import com.local.chatlog.R
import com.local.chatlog.databinding.RowUserPartnerMessageChatLogBinding
import com.local.chatlog.model.ChatUser
import com.local.chatlog.model.FlowChatMessage
import com.squareup.picasso.Picasso
import com.xwray.groupie.databinding.BindableItem

class UserMessagePartnerItem(
    val chatUser: ChatUser,
    val flowMessage: FlowChatMessage.FlowChatNewMessage
) :
    BindableItem<RowUserPartnerMessageChatLogBinding>() {
    override fun bind(viewBinding: RowUserPartnerMessageChatLogBinding, position: Int) {
        viewBinding.textViewMessageRowUserPartnerMessage.text = flowMessage.chatMessage.message
        val image = chatUser.userImageUri
        if (image.isNotEmpty()) {
            Picasso.get().load(image).into(viewBinding.imageViewProfileImageRowUserPartnerMessage)
        }
    }

    override fun getLayout(): Int {
        return R.layout.row_user_partner_message_chat_log
    }

}