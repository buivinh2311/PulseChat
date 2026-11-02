package com.example.infrastructure.di

import com.example.core_domain.repository.PresenceRepository
import com.example.infrastructure.repository.PresenceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PresenceModule {
    @Binds
    @Singleton
    abstract fun bindPresenceRepository(
        impl: PresenceRepositoryImpl
    ): PresenceRepository
}