package com.local.chatlog.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.local.chatlog.repository.UserAuthData
import com.local.chatlog.view.intents.RegisterIntent
import com.local.chatlog.interfaces.RegisterViewModel
import com.local.chatlog.interfaces.Repository
import com.local.chatlog.view.states.RegisterStates
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class RegisterViewModelImpl @Inject constructor(private val repository: Repository) : ViewModel(),
    RegisterViewModel {
    val userIntent = Channel<RegisterIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<RegisterStates>(RegisterStates.Idle)
    val state: StateFlow<RegisterStates>
        get() = _state

    init {
        handleIntent()
    }


    @ExperimentalCoroutinesApi
    override fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is RegisterIntent.Idle -> {
                    }
                    is RegisterIntent.RegisterWithEmailAndPassword -> {
                        viewModelScope.launch {
                            when (val data = repository.registerWithEmailAndPassword(
                                intent.name,
                                intent.email,
                                intent.password,
                                intent.profileImage
                            )) {
                                is UserAuthData.Success -> _state.value = RegisterStates.Success
                                is UserAuthData.Fail -> _state.value = RegisterStates.Fail(data.e)
                                else -> {
                                }
                            }

                        }//.invokeOnCompletion { "invokeOnCompletion".Log(this.toString()) }
                    }
                }
            }
        }
    }
}