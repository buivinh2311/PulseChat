package com.example.feature_contacts.domain.usecase

import com.example.core_domain.repository.FriendRequestRepository
import javax.inject.Inject

class RejectFriendRequestUseCase @Inject constructor(
    private val friendRequestRepository: FriendRequestRepository
) {
    suspend operator fun invoke(requestId: String): Result<Unit> {
        return friendRequestRepository.rejectRequest(requestId)
    }
}