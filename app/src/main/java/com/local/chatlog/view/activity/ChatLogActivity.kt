package com.local.chatlog.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.local.chatlog.App
import com.local.chatlog.R
import com.local.chatlog.databinding.ActivityChatLogBinding
import com.local.chatlog.di.viewmodel.ViewModelFactory
import com.local.chatlog.model.*
import com.local.chatlog.view.intents.ChatLogIntent
import com.local.chatlog.view.recyclerviewitems.UserMessageItem
import com.local.chatlog.view.recyclerviewitems.UserMessagePartnerItem
import com.local.chatlog.view.states.ChatLogState
import com.local.chatlog.view.viewmodel.ChatLogViewModelImpl
import com.xwray.groupie.GroupieAdapter
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject





class ChatLogActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val chatLogViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            ChatLogViewModelImpl::class.java
        )
    }

    lateinit var binding: ActivityChatLogBinding
    lateinit var adapter: GroupieAdapter

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_log)


        val chatRoom: ChatRoom = intent.getParcelableExtra(UserListActivity.ROOM_KEY)
            ?: throw RuntimeException("Parcelable parse error")
        binding(chatRoom)
        observeState()
        startReceiveMessages(chatRoom)

        supportActionBar?.title = chatRoom.user2.userName
    }

    @ExperimentalCoroutinesApi
    private fun startReceiveMessages(chatRoom: ChatRoom) {
        chatLogViewModel.receiveMessages(chatRoom.roomId)
        chatLogViewModel.messages.observe(this, Observer {
            when (it) {
                is FlowChatMessage.FlowChatNewMessage -> {
                    if (chatRoom.user1.uid == it.chatMessage.fromUid)
                        adapter.add(UserMessageItem(chatRoom.user1, it))
                    else
                        adapter.add(UserMessagePartnerItem(chatRoom.user2, it))
                }
                is FlowChatMessage.FlowChatRemoveMessage -> {
                    loop@ for (i in 0 until adapter.itemCount) {

                        when (val item = adapter.getItem(i)) {
                            is UserMessageItem -> {
                                if (item.flowMessage.key == it.key) {
                                    adapter.remove(item)
                                    break@loop
                                }
                            }
                            is UserMessagePartnerItem -> {
                                if (item.flowMessage.key == it.key) {
                                    adapter.remove(item)
                                    break@loop
                                }
                            }
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                else -> {
                }
            }
            binding.recyclerViewMessageLogChatLog.smoothScrollToPosition(adapter.itemCount - 1)
        })
    }

    private fun observeState() {
        lifecycleScope.launch {
            chatLogViewModel.state.collect {
                when (it) {
                    is ChatLogState.SendSuccess -> {
                    }
                    is ChatLogState.Fail -> {
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun binding(chatRoom: ChatRoom) {
        adapter = GroupieAdapter()
        binding.recyclerViewMessageLogChatLog.adapter = adapter

        binding.buttonSendMessageChatLog.setOnClickListener {
            if (binding.editTextMessageChatLog.text.isEmpty()) return@setOnClickListener
            lifecycleScope.launch {
                chatLogViewModel.userIntent.send(
                    ChatLogIntent.SendMessage(
                        chatRoom, ChatMessage(
                            chatLogViewModel.getCurrentUid(),
                            binding.editTextMessageChatLog.text.toString().trim(),
                            System.currentTimeMillis() / 1000
                        )
                    )
                )
            }

            binding.editTextMessageChatLog.text.clear()
        }
    }
}