package com.example.infrastructure.di

import com.example.core_domain.repository.FriendRequestRepository
import com.example.infrastructure.repository.FriendRequestRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FriendRequestRepositoryModule {
    @Binds
    abstract fun bindFriendRequestRepository(
        impl: FriendRequestRepositoryImpl
    ): FriendRequestRepository
}