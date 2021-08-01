package com.local.chatlog.di.viewmodel

import androidx.lifecycle.ViewModel
import com.local.chatlog.view.viewmodel.ChatLogViewModelImpl
import com.local.chatlog.view.viewmodel.LoginViewModelImpl
import com.local.chatlog.view.viewmodel.RegisterViewModelImpl
import com.local.chatlog.view.viewmodel.UserListViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Suppress
@Module
internal abstract class ViewModelModule {
    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModelImpl::class)
    internal abstract fun bindRegisterActivityViewModel(registerViewModel: RegisterViewModelImpl): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(UserListViewModelImpl::class)
    internal abstract fun bindUserListActivityViewModel(userListViewModelImpl: UserListViewModelImpl): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(ChatLogViewModelImpl::class)
    internal abstract fun bindChatMessageActivityViewModel(chatLogViewModelImpl: ChatLogViewModelImpl): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModelImpl::class)
    internal abstract fun bindLoginActivityViewModel(loginViewModelImpl: LoginViewModelImpl): ViewModel

}