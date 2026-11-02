package com.example.feature_conversation.domain.usecase

import com.example.core_domain.repository.PresenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveLastSeenUseCase @Inject constructor(
    private val presenceRepository: PresenceRepository
) {
    operator fun invoke(userId: String): Flow<Long?> {
        return presenceRepository.observeLastSeen(userId)
    }
}