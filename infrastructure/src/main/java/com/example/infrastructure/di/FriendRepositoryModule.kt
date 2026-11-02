package com.example.infrastructure.di

import com.example.core_domain.repository.FriendshipRepository
import com.example.infrastructure.repository.FriendshipRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FriendRepositoryModule {
    @Binds
    abstract fun bindFriendRepository(
        impl: FriendshipRepositoryImpl
    ): FriendshipRepository
}