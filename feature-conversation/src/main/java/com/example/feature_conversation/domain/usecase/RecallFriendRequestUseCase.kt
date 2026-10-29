package com.example.feature_conversation.domain.usecase

import com.example.core_domain.repository.AuthRepository
import com.example.core_domain.repository.FriendRequestRepository
import com.example.core_model.FriendRequest
import javax.inject.Inject

class RecallFriendRequestUseCase @Inject constructor(
    private val friendRequestRepository: FriendRequestRepository
) {
    suspend operator fun invoke(request: FriendRequest): Result<Unit> {
        return friendRequestRepository.recallRequest(request)
    }
}