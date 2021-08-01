package com.local.chatlog.view.viewmodel

import androidx.lifecycle.*
import com.local.chatlog.repository.UserAuthData
import com.local.chatlog.view.intents.LoginIntent
import com.local.chatlog.interfaces.LoginViewModel
import com.local.chatlog.interfaces.Repository
import com.local.chatlog.view.states.LoginStates
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LoginViewModelImpl @Inject constructor(private val repository : Repository) : ViewModel(), LoginViewModel {

    val userIntent = Channel<LoginIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<LoginStates>(LoginStates.Idle)
    val state: StateFlow<LoginStates>
        get() = _state

    init {
        handleIntent()
    }

    @ExperimentalCoroutinesApi
    override fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is LoginIntent.Idle -> {
                    }

                    is LoginIntent.Login -> {
                        viewModelScope.launch {
                            val data = repository.loginWithEmailAndPassword(intent.email,intent.password)
                            when (data) {
                                is UserAuthData.Success -> {
                                    _state.value = LoginStates.Success
                                }
                                is UserAuthData.Fail -> {
                                    _state.value = LoginStates.Fail(data.e)
                                }
                                else -> {
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}