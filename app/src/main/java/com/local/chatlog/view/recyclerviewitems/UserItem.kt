package com.local.chatlog.view.recyclerviewitems

import androidx.core.content.ContextCompat
import com.local.chatlog.R
import com.local.chatlog.databinding.RowUserListBinding
import com.squareup.picasso.Picasso
import com.xwray.groupie.databinding.BindableItem

class UserItem(val uid: String, val name: String, val profileImg: String) :
    BindableItem<RowUserListBinding>() {


    override fun getLayout(): Int {
        return R.layout.row_user_list
    }

    override fun bind(viewBinding: RowUserListBinding, position: Int) {
        viewBinding.textViewUserNameRowUserList.text = name
        if (profileImg.isNotEmpty())
            Picasso.get().load(profileImg).into(viewBinding.imageViewUserImageUserList)
        else
            viewBinding.imageViewUserImageUserList.setImageDrawable(
                ContextCompat.getDrawable(
                    viewBinding.root.context,
                    R.drawable.profile
                )
            )
    }

}