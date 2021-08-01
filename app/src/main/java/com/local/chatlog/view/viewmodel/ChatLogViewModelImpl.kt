package com.local.chatlog.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.local.chatlog.interfaces.*
import com.local.chatlog.repository.UserMessageData
import com.local.chatlog.view.intents.ChatLogIntent
import com.local.chatlog.model.*
import com.local.chatlog.view.states.ChatLogState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatLogViewModelImpl @Inject constructor(
    private val repository: Repository,
    private val chatRoomMapper: FirebaseChatRoomMapper,
    private val chatMessageMapper: FirebaseChatMessageMapper
) :
    ViewModel(), ChatLogViewModel {
    private val _messages = MutableLiveData<FlowChatMessage>()
    val messages: LiveData<FlowChatMessage>
        get() = _messages

    private val _state = MutableStateFlow<ChatLogState>(ChatLogState.Idle)
    val state: StateFlow<ChatLogState>
        get() = _state

    val userIntent = Channel<ChatLogIntent>(Channel.UNLIMITED)

    init {
        handleIntent()
    }

    override fun getCurrentUid() = repository.getCurrentUid()

    override fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is ChatLogIntent.SendMessage -> {
                        viewModelScope.launch {
                            val data = repository.sendMessage(
                                chatRoomMapper.fromData(it.chatRoom),
                                chatMessageMapper.fromData(it.chatMessage)
                            )
                            when (data) {
                                is UserMessageData.Success -> {
                                    _state.value = ChatLogState.SendSuccess
                                }
                                is UserMessageData.Fail -> {
                                    _state.value = ChatLogState.Fail(data.e)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    override fun receiveMessages(roomId: String) {
        viewModelScope.launch {
            repository.subscribeToNewMessages(roomId).collect {
                when (it) {
                    is FlowChatMessage.FlowChatNewMessage -> {
                        _messages.value = it
                    }
                    is FlowChatMessage.FlowChatRemoveMessage -> {
                        _messages.value = it
                    }
                    is FlowChatMessage.FlowChatError -> {
                        _messages.value = it
                    }
                }

            }
        }
    }

}