package com.local.chatlog.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.local.chatlog.interfaces.*
import com.local.chatlog.repository.UserListData
import com.local.chatlog.repository.UserRoomData
import com.local.chatlog.view.intents.UserListIntent
import com.local.chatlog.model.*
import com.local.chatlog.view.states.UserListStates
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserListViewModelImpl @Inject constructor(
    val repository: Repository,
    private val chatUserMapper: FirebaseChatUserMapper,
    private val chatRoomMapper: FirebaseChatRoomMapper

) : ViewModel(),
    UserListViewModel {

    val userIntent = Channel<UserListIntent>(Channel.UNLIMITED)

    private val _user = MutableLiveData<FlowUserList>()
    val user: LiveData<FlowUserList>
        get() = _user

    private val _state = MutableStateFlow<UserListStates>(UserListStates.Idle)
    val state: StateFlow<UserListStates>
        get() = _state

    private val _userName = MutableLiveData<String>("")
    val userName: LiveData<String>
        get() = _userName

    init {
        viewModelScope.launch {
            repository.fetchCurrentUser()
            _userName.value = repository.user.userName
            _state.value = UserListStates.CurrentUserDataReceived
        }
        handleIntents()

    }

    override fun getCurrentUser(): ChatUser {
        return chatUserMapper.fromEntity(repository.getCurrentUser())
    }

    override fun getCurrentUid() = repository.getCurrentUid()
    override fun fetchAllUser() {
        viewModelScope.launch {
            repository.fetchAllUser().collect {
                when (it) {
                    is UserListData.Added -> {
                        _user.value =
                            FlowUserList.FlowUserListAddUser(chatUserMapper.fromEntity(it.chatUser))
                    }
                    is UserListData.Remove -> {
                        _user.value =
                            FlowUserList.FlowUserListRemoveUser(chatUserMapper.fromEntity(it.chatUser))
                    }
                    is UserListData.Fail -> {
                        _user.value = FlowUserList.Fail(it.e)
                    }
                }
            }

        }
    }

    override fun handleIntents() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is UserListIntent.StartChat -> {
                        viewModelScope.launch {
                            when (val data =
                                repository.createOrReceiveChatRoom(
                                    chatUserMapper.fromData(intent.user),
                                    chatUserMapper.fromData(intent.partner)
                                )) {
                                is UserRoomData.Success -> {
                                    _state.value = UserListStates.EnterRoom(
                                        chatRoomMapper.fromEntity(data.chatRoom)
                                    )
                                }
                                is UserRoomData.Fail -> {
                                    _state.value = UserListStates.Fail(data.e)
                                }
                            }

                        }
                    }
                }
            }
        }
    }


}