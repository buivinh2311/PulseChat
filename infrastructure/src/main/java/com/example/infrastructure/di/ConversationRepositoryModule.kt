package com.example.infrastructure.di

import com.example.core_domain.repository.ConversationRepository
import com.example.infrastructure.repository.ConversationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ConversationRepositoryModule {
    @Binds
    abstract fun bindConversationRepository(
        impl: ConversationRepositoryImpl
    ): ConversationRepository
}