package com.example.feature_settings.usecase

import com.example.core_domain.repository.AuthRepository
import com.example.core_domain.repository.PresenceRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val presenceRepository: PresenceRepository
) {
    suspend operator fun invoke() {
        presenceRepository.stopPresence()
        repository.logout()
    }
}