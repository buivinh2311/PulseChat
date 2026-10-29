package com.example.core_domain.usecase

import com.example.core_domain.repository.UserRepository
import com.example.core_model.User
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(uid: String): Result<User?> {
        return userRepository.getUserById(uid)
    }
}