package com.example.infrastructure.di

import com.example.core_domain.repository.GroupRepository
import com.example.infrastructure.repository.GroupRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class GroupRepositoryModule {
    @Binds
    abstract fun bindGroupRepository(
        impl: GroupRepositoryImpl
    ): GroupRepository
}