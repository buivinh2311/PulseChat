package com.example.core_domain.repository

import kotlinx.coroutines.flow.Flow

interface PresenceRepository {

    fun observeOnlineStatus(userId: String): Flow<Boolean>

    fun observeLastSeen(userId: String): Flow<Long?>

    suspend fun startPresence()

    suspend fun stopPresence()
}