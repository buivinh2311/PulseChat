package com.example.feature_conversation.domain.usecase

import com.example.core_domain.repository.AuthRepository
import com.example.core_domain.repository.ConversationRepository
import com.example.core_domain.repository.GroupRepository
import javax.inject.Inject

class PinConversationUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val groupRepository: GroupRepository,
    private val conversationRepository: ConversationRepository
) {
    suspend operator fun invoke(conversationId: String): Result<Unit> {
        val currentUserId = authRepository.getCurrentUserId()
            ?: return Result.failure(IllegalStateException("User not logged in"))

        val group = groupRepository
            .getGroupByConversationId(conversationId)
            .getOrElse { return Result.failure(it) }
            ?: return Result.failure(IllegalStateException("Group not found"))

        if(currentUserId != group.leaderId) {
            return Result.failure(
                SecurityException("Only the group leader can pin conversation")
            )
        }
        return conversationRepository.pinConversation(conversationId)
    }
}