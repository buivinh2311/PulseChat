package com.example.feature_conversation.domain.usecase

import com.example.core_domain.repository.AuthRepository
import com.example.core_domain.repository.GroupRepository
import com.example.core_domain.repository.UserRepository
import com.example.core_model.Group
import com.example.core_model.Message
import com.example.core_model.enum.MessageStatus
import com.example.core_model.enum.SystemMessageType
import com.google.firebase.Timestamp
import javax.inject.Inject

class ChangeGroupNameUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(group: Group, groupName: String): Result<Unit> {
        val currentUserId = authRepository.getCurrentUserId()
            ?: return Result.failure(IllegalStateException("User not logged in"))

        if(currentUserId != group.leaderId) {
            return Result.failure(
                SecurityException("Only the group leader can pin conversation")
            )
        }

        val currentUser = userRepository.getUserById(currentUserId)
            .getOrElse { return Result.failure(it) }
            ?: return Result.failure(IllegalStateException("User not found"))

        val systemMessage = Message.System(
            id = "",
            conversationId = group.conversationId,
            senderId = currentUserId,
            status = MessageStatus.DELIVERED,
            createdAt = Timestamp.now(),
            systemMessageType = SystemMessageType.CHANGE_GROUP_NAME,
            text = groupName
        )

        return groupRepository.changeGroupName(
            group = group,
            groupName = groupName,
            systemMessage = systemMessage,
            actorUserName = currentUser.fullName
        )
    }
}