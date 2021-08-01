package com.local.chatlog.di.module

import com.local.chatlog.interfaces.*
import com.local.chatlog.model.*
import com.local.chatlog.model.mapper.FirebaseChatMessageMapperImpl
import com.local.chatlog.model.mapper.FirebaseChatRoomMapperImpl
import com.local.chatlog.model.mapper.FirebaseChatUserMapperImpl
import com.local.chatlog.repository.FirebaseRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module()
class Module {
    @Provides
    @Singleton
    fun provideRepository(): Repository {
        return FirebaseRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideFirebaseChatMessageMapper(): FirebaseChatMessageMapper {
        return FirebaseChatMessageMapperImpl()
    }

    @Provides
    @Singleton
    fun provideFirebaseChatUserMapper(): FirebaseChatUserMapper {
        return FirebaseChatUserMapperImpl()
    }

    @Provides
    @Singleton
    fun provideFirebaseChatRoomMapper(): FirebaseChatRoomMapper {
        return FirebaseChatRoomMapperImpl()
    }
}