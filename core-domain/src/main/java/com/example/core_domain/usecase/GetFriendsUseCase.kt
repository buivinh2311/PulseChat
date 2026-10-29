package com.example.core_domain.usecase

import com.example.core_domain.repository.AuthRepository
import com.example.core_domain.repository.FriendshipRepository
import com.example.core_domain.repository.UserRepository
import com.example.core_model.User
import javax.inject.Inject

class GetFriendsUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val friendshipRepository: FriendshipRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<List<User>> {
        val currentUserId = authRepository
            .getCurrentUserId()
            ?: return Result.failure(IllegalStateException("User not logged in"))

        val friendIds = friendshipRepository
            .getFriendIdsByUserId(currentUserId)
            .getOrElse { return Result.failure(it) }

        return userRepository.getUsersByIds(friendIds)
    }
}