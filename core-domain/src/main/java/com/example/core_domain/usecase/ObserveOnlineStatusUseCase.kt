package com.example.core_domain.usecase

import com.example.core_domain.repository.PresenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveOnlineStatusUseCase @Inject constructor(
    private val presenceRepository: PresenceRepository
) {
    operator fun invoke(userId: String): Flow<Boolean> {
        return presenceRepository.observeOnlineStatus(userId)
    }
}