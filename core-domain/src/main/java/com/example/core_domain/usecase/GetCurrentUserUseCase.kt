package com.example.core_domain.usecase

import com.example.core_domain.repository.AuthRepository
import com.example.core_domain.repository.UserRepository
import com.example.core_model.User
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<User> {
        val currentUserId = authRepository.getCurrentUserId()
            ?: return Result.failure(IllegalStateException("User not logged in"))
        return userRepository.getUserById(currentUserId)
            .fold(
                onSuccess = { user ->
                    user?.let {
                        Result.success(it)
                    } ?: Result.failure(
                        IllegalStateException("User not found")
                    )
                },
                onFailure = {
                    Result.failure(it)
                }
            )
    }
}