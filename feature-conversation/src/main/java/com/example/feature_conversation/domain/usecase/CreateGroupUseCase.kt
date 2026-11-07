package com.example.feature_conversation.domain.usecase

import com.example.core_domain.repository.AuthRepository
import com.example.core_domain.repository.ConversationRepository
import com.example.core_domain.repository.GroupRepository
import com.example.core_domain.repository.StorageRepository
import com.example.core_model.Conversation
import com.example.core_model.Group
import com.example.core_model.User
import com.example.core_model.enum.ConversationType
import com.google.firebase.Timestamp
import java.io.File
import javax.inject.Inject

class CreateGroupUseCase @Inject constructor(
    private val groupRepository: GroupRepository,
    private val storageRepository: StorageRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        name: String,
        avatar: File? = null,
        otherUser: List<User>
    ): Result<String> {
        val currentUser = authRepository.getCurrentUser()
            ?: return Result.failure(IllegalStateException("User not logged in"))

        val participants = buildList {
            add(currentUser.uid)
            addAll(otherUser.map { it.id })
        }

        val type = if(otherUser.size < 2) {
            ConversationType.DIRECT
        } else {
            ConversationType.GROUP
        }

        val conversation = Conversation(
            id = "",
            type = type,
            participants = participants,
            createdAt = Timestamp.now()
        )

        val avatarUrl = if(avatar != null) {
            storageRepository
                .upload(avatar)
                .getOrElse { return Result.failure(it) }
                .url
        } else {
            null
        }

        val group = Group(
            id = "",
            name = name,
            avatarUrl = avatarUrl,
            conversationId = "",
            leaderId = currentUser.uid,
            createdAt = Timestamp.now()
        )

        val conversationId = groupRepository
            .createGroup(
                conversation = conversation,
                group = group
            )
            .getOrElse { return Result.failure(it) }

        return Result.success(conversationId)
    }
}