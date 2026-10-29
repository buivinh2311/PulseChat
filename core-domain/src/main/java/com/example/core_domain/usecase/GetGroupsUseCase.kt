package com.example.core_domain.usecase

import com.example.core_domain.repository.AuthRepository
import com.example.core_domain.repository.ConversationRepository
import com.example.core_domain.repository.GroupRepository
import com.example.core_model.Group
import javax.inject.Inject

class GetGroupsUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val conversationRepository: ConversationRepository,
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(): Result<List<Group>> {
        val currentUserId = authRepository.getCurrentUserId()
            ?: return Result.failure(IllegalStateException("User not logged in"))

        val conversationIds = conversationRepository
            .getConversationGroupsByUserId(currentUserId)
            .getOrElse { return Result.failure(it) }
            .map { it.id }

        return groupRepository.getGroupsByConversationIds(conversationIds)
    }
}